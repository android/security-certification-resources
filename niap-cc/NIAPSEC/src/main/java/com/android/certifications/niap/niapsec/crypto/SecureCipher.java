/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.certifications.niap.niapsec.crypto;


import android.os.Build;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.util.Pair;

import androidx.annotation.NonNull;

import com.android.certifications.niap.niapsec.SecureConfig;
import com.android.certifications.niap.niapsec.biometric.BiometricSupport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

/**
 * Wraps {@link Cipher} to provide NIAP Sensitive data protection.
 * <p>
 * Adds encryption options to encrypt data using both a data and key encryption key.
 */
public class SecureCipher {

    private static final String TAG = "SecureCipher";

    private SecureConfig secureConfig;

    public static int MODE_ENCRYPT = 1;
    public static int MODE_DECRYPT = 2;

    /**
     * Generic type used to handle callbacks from the SecureCipher
     */
    public interface SecureCallback {
    }

    /**
     * Callback interface for specifying authentication
     */
    public interface SecureAuthCallback extends SecureCallback {
        void authComplete(BiometricSupport.BiometricStatus status);
    }

    /**
     * Callback interface for specifying asynchronous and authenticated symmetric encryption
     */
    public interface SecureSymmetricEncryptionCallback extends SecureCallback {
        void encryptionComplete(byte[] cipherText, byte[] iv);
    }

    /**
     * Callback interface for specifying asynchronous and authenticated asymmetric encryption
     */
    public interface SecureAsymmetricEncryptionCallback extends SecureCallback {
        void encryptionComplete(byte[] cipherText);
    }

    /**
     * Callback interface for specifying asynchronous and authenticated decryption
     */
    public interface SecureDecryptionCallback extends SecureCallback {
        void decryptionComplete(byte[] clearText);
    }

    /**
     * Create and return a SecureCipher with NIAP recommended settings that requires biometric
     * authentication for key use
     *
     * @param secureConfig The config to use
     * @return A secure cipher with NIAP recommended settings
     */
    public static SecureCipher getDefault(SecureConfig secureConfig) {
        return new SecureCipher(secureConfig);
    }

    /**
     * Instantiates a SecureConfig with the provided config
     *
     * @param secureConfig The settings to build the cipher with
     */
    private SecureCipher(SecureConfig secureConfig) {
        this.secureConfig = secureConfig;
    }

    /**
     * Encoding types used internally
     */
    enum SecureFileEncodingType {
        SYMMETRIC(0),
        ASYMMETRIC(1),
        EPHEMERAL(2),
        NOT_ENCRYPTED(1000);

        private final int type;

        SecureFileEncodingType(int type) {
            this.type = type;
        }

        public int getType() {
            return this.type;
        }

        public static SecureFileEncodingType fromId(int id) {
            switch (id) {
                case 0:
                    return SYMMETRIC;
                case 1:
                    return ASYMMETRIC;
                case 2:
                    return EPHEMERAL;
            }
            return NOT_ENCRYPTED;
        }

    }

    /**
     * Encrypts data with an existing key alias from the AndroidKeyStore.
     *
     * @param keyAlias  The name of the existing SecretKey to retrieve from the AndroidKeyStore.
     * @param clearData The unencrypted data to encrypt
     * @return A Pair of byte[]'s, first is the encrypted data, second is the IV
     * (initialization vector) used to encrypt which is required for decryption
     */
    public void encryptSensitiveData(String keyAlias,
                                     byte[] clearData,
                                     SecureSymmetricEncryptionCallback callback) {
        try {
            KeyStore keyStore = KeyStore.getInstance(secureConfig.getAndroidKeyStore());
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(keyAlias, null);
            Cipher cipher = Cipher.getInstance(secureConfig.getSymmetricCipherTransformation());
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] iv = cipher.getIV();
            if (secureConfig.isSymmetricRequireUserAuthEnabled()) {
                secureConfig.getBiometricSupport().authenticate(
                        cipher,
                        (BiometricSupport.BiometricStatus status) -> {
                            switch (status) {
                                case SUCCESS:
                                    try {
                                        callback.encryptionComplete(
                                                cipher.doFinal(clearData),
                                                cipher.getIV());
                                    } catch (GeneralSecurityException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    Log.i(TAG, "Failure");
                                    callback.encryptionComplete(null, null);
                            }
                        });
            } else {
                callback.encryptionComplete(cipher.doFinal(clearData), cipher.getIV());
            }
        } catch (GeneralSecurityException | IOException ex) {
            Log.e(TAG, "Failure to encrypt data: " + ex.getLocalizedMessage());
            throw new SecurityException(ex);
        }
    }

    /**
     * Encrypts data with a public key from the cert in the AndroidKeyStore.
     *
     * @param keyAlias  The name of the existing KeyPair to retrieve the PublicKey from the
     *                  AndroidKeyStore.
     * @param clearData The unencrypted data to encrypt
     * @return A Pair of byte[]'s, first is the encrypted data, second is the IV
     * (initialization vector) used to encrypt which is required for decryption
     */
    public void encryptSensitiveDataAsymmetric(String keyAlias,
                                               byte[] clearData,
                                               SecureAsymmetricEncryptionCallback callback) {
        try {
            KeyStore keyStore = KeyStore.getInstance(secureConfig.getAndroidKeyStore());
            keyStore.load(null);
            PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();
            Cipher cipher = Cipher.getInstance(secureConfig.getAsymmetricCipherTransformation());
            if (secureConfig.getAsymmetricPaddings().equals(
                    KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)) {
                cipher.init(Cipher.ENCRYPT_MODE,
                        publicKey,
                        new OAEPParameterSpec("SHA-256",
                                "MGF1",
                                new MGF1ParameterSpec("SHA-1"),
                                PSource.PSpecified.DEFAULT));
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            }
            byte[] clearText = cipher.doFinal(clearData);
            callback.encryptionComplete(clearText);
        } catch (GeneralSecurityException | IOException ex) {
            Log.e(TAG, "Failure to encrypt data: " + ex.getLocalizedMessage());
            throw new SecurityException(ex);
        }
    }

    /**
     * Encrypts data with a public key from the cert in the AndroidKeyStore.
     *
     * @param keyAlias  The name of the existing KeyPair to retrieve the PublicKey from the
     *                  AndroidKeyStore.
     * @param clearData The unencrypted data to encrypt
     * @return A Pair of byte[]'s, first is the encrypted data, second is the IV
     * (initialization vector) used to encrypt which is required for decryption
     */
    public byte[] encryptSensitiveDataAsymmetric(String keyAlias,
                                                 byte[] clearData) {
        byte[] cipherText = null;
        try {
            KeyStore keyStore = KeyStore.getInstance(secureConfig.getAndroidKeyStore());
            keyStore.load(null);
            PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();
            Cipher cipher = Cipher.getInstance(secureConfig.getAsymmetricCipherTransformation());
            if (secureConfig.getAsymmetricPaddings().equals(
                    KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)) {
                cipher.init(Cipher.ENCRYPT_MODE,
                        publicKey,
                        new OAEPParameterSpec("SHA-256",
                                "MGF1",
                                new MGF1ParameterSpec("SHA-1"),
                                PSource.PSpecified.DEFAULT));
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            }
            cipherText = cipher.doFinal(clearData);
        } catch (GeneralSecurityException | IOException ex) {
            Log.e(TAG, "Failure to encrypt data: " + ex.getLocalizedMessage());
            throw new SecurityException(ex);
        }
        return cipherText;
    }

    /**
     * Encrypts data using an Ephemeral key, destroying any trace of the key from the Cipher used.
     *
     * @param ephemeralSecretKey The generated Ephemeral key
     * @param clearData          The unencrypted data to encrypt
     * @return A Pair of byte[]'s, first is the encrypted data, second is the IV
     * (initialization vector)
     * used to encrypt which is required for decryption
     */
    public Pair<byte[], byte[]> encryptEphemeralData(EphemeralSecretKey ephemeralSecretKey,
                                                     byte[] clearData, String keyPairAlias) {
        try {
            SecureRandom secureRandom = null;
            secureRandom = SecureRandom.getInstanceStrong();
            /*
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            } else {
                secureRandom = new SecureRandom();
            }
            */
            byte[] iv = new byte[SecureConfig.AES_IV_SIZE_BYTES];
            secureRandom.nextBytes(iv);
            GCMParameterSpec parameterSpec =
                    new GCMParameterSpec(secureConfig.getSymmetricGcmTagLength(), iv);
            final Cipher cipher =
                    Cipher.getInstance(secureConfig.getSymmetricCipherTransformation());
            cipher.init(Cipher.ENCRYPT_MODE, ephemeralSecretKey, parameterSpec);
            byte[] encryptedData = cipher.doFinal(clearData);
            ephemeralSecretKey.destroyCipherKey(cipher, Cipher.ENCRYPT_MODE, keyPairAlias);
            return new Pair<>(encryptedData, iv);
        } catch (GeneralSecurityException ex) {
            Log.e(TAG, "Failure to encrypt data: " + ex.getLocalizedMessage());
            throw new SecurityException(ex);
        }
    }

    /**
     * Decrypts a previously encrypted byte[]
     * <p>
     * Destroys all traces of the key data in the Cipher.
     *
     * @param ephemeralSecretKey   The generated Ephemeral key
     * @param encryptedData        The byte[] of encrypted data
     * @param initializationVector The IV of which the encrypted data was encrypted with
     * @return The byte[] of data that has been decrypted
     */
    public byte[] decryptEphemeralData(EphemeralSecretKey ephemeralSecretKey,
                                       byte[] encryptedData, byte[] initializationVector,
                                       String keyPairAlias) {
        try {
            final Cipher cipher =
                    Cipher.getInstance(secureConfig.getSymmetricCipherTransformation());
            cipher.init(Cipher.DECRYPT_MODE,
                    ephemeralSecretKey,
                    new GCMParameterSpec(secureConfig.getSymmetricGcmTagLength(),
                            initializationVector));
            byte[] decryptedData = cipher.doFinal(encryptedData);
            ephemeralSecretKey.destroyCipherKey(cipher, Cipher.DECRYPT_MODE, keyPairAlias);
            return decryptedData;
        } catch (GeneralSecurityException ex) {
            Log.e(TAG, "Failure to decrypt data: " + ex.getLocalizedMessage());
            throw new SecurityException(ex);
        }
    }

    /**
     * Decrypts a previously encrypted byte[]
     *
     * @param keyAlias             The name of the existing SecretKey to retrieve from the
     *                             AndroidKeyStore.
     * @param encryptedData        The byte[] of encrypted data
     * @param initializationVector The IV of which the encrypted data was encrypted with
     * @return The byte[] of data that has been decrypted
     */
    public byte[] decryptSensitiveData(String keyAlias, byte[] encryptedData,
                                       byte[] initializationVector) {
        byte[] decryptedData = new byte[0];
        try {
            KeyStore keyStore = KeyStore.getInstance(secureConfig.getAndroidKeyStore());
            keyStore.load(null);
            Key key = keyStore.getKey(keyAlias, null);
            Cipher cipher = Cipher.getInstance(secureConfig.getSymmetricCipherTransformation());
            GCMParameterSpec spec = new GCMParameterSpec(secureConfig.getSymmetricGcmTagLength(),
                    initializationVector);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            decryptedData = cipher.doFinal(encryptedData);
        } catch (GeneralSecurityException | IOException ex) {
            Log.e(TAG, "Failure to decrypt data: " + ex.getLocalizedMessage());
            throw new SecurityException(ex);
        }
        return decryptedData;
    }

    /**
     * Decrypts a previously encrypted byte[]
     *
     * @param keyAlias             The name of the existing SecretKey to retrieve from the
     *                             AndroidKeyStore.
     * @param encryptedData        The byte[] of encrypted data
     * @param initializationVector The IV of which the encrypted data was encrypted with
     * @return The byte[] of data that has been decrypted
     */
    public void decryptSensitiveData(String keyAlias, byte[] encryptedData,
                                     byte[] initializationVector,
                                     SecureDecryptionCallback callback) {
        byte[] decryptedData = new byte[0];
        try {
            KeyStore keyStore = KeyStore.getInstance(secureConfig.getAndroidKeyStore());
            keyStore.load(null);
            Key key = keyStore.getKey(keyAlias, null);
            Cipher cipher = Cipher.getInstance(secureConfig.getSymmetricCipherTransformation());
            GCMParameterSpec spec = new GCMParameterSpec(secureConfig.getSymmetricGcmTagLength(),
                    initializationVector);
            cipher.init(Cipher.DECRYPT_MODE, key, spec);
            if (secureConfig.isSymmetricRequireUserAuthEnabled()) {
                secureConfig.getBiometricSupport().authenticate(
                        cipher,
                        (BiometricSupport.BiometricStatus status) -> {
                            switch (status) {
                                case SUCCESS:
                                    try {
                                        callback.decryptionComplete(cipher.doFinal(encryptedData));
                                    } catch (GeneralSecurityException e) {
                                        Log.e(TAG, "Failure to decrypt data: " +
                                                e.getLocalizedMessage());
                                        e.printStackTrace();
                                    }
                                    break;
                                default:
                                    Log.e(TAG, "Failure to decrypt data: " +
                                            "Biometric authentication failed");
                                    callback.decryptionComplete(null);
                            }
                        });
            } else {
                callback.decryptionComplete(cipher.doFinal(encryptedData));
            }
        } catch (GeneralSecurityException ex) {
            Log.e(TAG, "Failure to decrypt data: " + ex.getLocalizedMessage());
            throw new SecurityException(ex);
        } catch (IOException ex) {
            Log.e(TAG, "Failure to decrypt data: " + ex.getLocalizedMessage());
            throw new SecurityException(ex);
        }
    }

    /**
     * Decrypts a previously encrypted byte[] with the PrivateKey
     *
     * @param keyAlias      The name of the existing KeyPair to retrieve from the AndroidKeyStore.
     * @param encryptedData The byte[] of encrypted data
     * @return The byte[] of data that has been decrypted
     */
    public byte[] decryptSensitiveDataAsymmetric(String keyAlias,
                                                 byte[] encryptedData) {
        byte[] clearData = new byte[0];
        try {
            KeyStore keyStore = KeyStore.getInstance(secureConfig.getAndroidKeyStore());
            keyStore.load(null);
            PrivateKey key = (PrivateKey) keyStore.getKey(keyAlias, null);
            Cipher cipher = Cipher.getInstance(secureConfig.getAsymmetricCipherTransformation());
            if (secureConfig.getAsymmetricPaddings().equals(
                    KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)) {
                cipher.init(Cipher.DECRYPT_MODE,
                        key,
                        new OAEPParameterSpec(
                                "SHA-256",
                                "MGF1",
                                new MGF1ParameterSpec("SHA-1"),
                                PSource.PSpecified.DEFAULT));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            clearData = cipher.doFinal(encryptedData);
        } catch (GeneralSecurityException | IOException ex) {
            Log.e(TAG, "Failure to decrypt data: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
        return clearData;
    }

    /**
     * Decrypts a previously encrypted byte[] with the PrivateKey
     *
     * @param keyAlias      The name of the existing KeyPair to retrieve from the AndroidKeyStore.
     * @param encryptedData The byte[] of encrypted data
     * @return The byte[] of data that has been decrypted
     */
    public void decryptSensitiveDataAsymmetric(String keyAlias,
                                               byte[] encryptedData,
                                               SecureDecryptionCallback callback) {
        byte[] decryptedData = new byte[0];
        try {
            KeyStore keyStore = KeyStore.getInstance(secureConfig.getAndroidKeyStore());
            keyStore.load(null);
            PrivateKey key = (PrivateKey) keyStore.getKey(keyAlias, null);
            Cipher cipher = Cipher.getInstance(secureConfig.getAsymmetricCipherTransformation());
            // If the key was created using the time-bound method, we must delay creation of the
            // Cipher object
            if (secureConfig.getAsymmetricRequireUserValiditySeconds() <= 0) {
                if (secureConfig.getAsymmetricPaddings().equals(
                        KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)) {
                    cipher.init(Cipher.DECRYPT_MODE,
                            key,
                            new OAEPParameterSpec(
                                    "SHA-256",
                                    "MGF1",
                                    new MGF1ParameterSpec("SHA-1"),
                                    PSource.PSpecified.DEFAULT));
                } else {
                    cipher.init(Cipher.DECRYPT_MODE, key);
                }
            }
            if (secureConfig.isAsymmetricRequireUserAuthEnabled()) {
                if (secureConfig.getAsymmetricRequireUserValiditySeconds() <= 0) {
                    secureConfig.getBiometricSupport().authenticate(
                            cipher,
                            (BiometricSupport.BiometricStatus status) -> {
                                switch (status) {
                                    case SUCCESS:
                                        try {
                                            byte[] clearData = cipher.doFinal(encryptedData);
                                            callback.decryptionComplete(clearData);

                                        } catch (Exception e) {
                                            Log.e(TAG, "Failure to decrypt data: " +
                                                    e.getLocalizedMessage());
                                            e.printStackTrace();
                                        }
                                        break;
                                    default:
                                        Log.e(TAG, "Failure to decrypt data: " +
                                                "Biometric authentication failed");
                                        callback.decryptionComplete(null);
                                        throw new SecurityException("Failure to decrypt data: " +
                                                "Biometric authentication failed");
                                }
                            });
                } else {
                    secureConfig.getBiometricSupport().authenticate(
                            cipher,
                            (BiometricSupport.BiometricStatus status) -> {
                                switch (status) {
                                    case SUCCESS:
                                        try {
                                            if (secureConfig.getAsymmetricPaddings().equals(
                                                    KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)) {
                                                cipher.init(Cipher.DECRYPT_MODE,
                                                        key,
                                                        new OAEPParameterSpec(
                                                                "SHA-256",
                                                                "MGF1",
                                                                new MGF1ParameterSpec("SHA-1"),
                                                                PSource.PSpecified.DEFAULT));
                                            } else {
                                                cipher.init(Cipher.DECRYPT_MODE, key);
                                            }
                                            byte[] clearData = cipher.doFinal(encryptedData);
                                            callback.decryptionComplete(clearData);

                                        } catch (Exception e) {
                                            Log.e(TAG, "Failure to decrypt data: " +
                                                    e.getLocalizedMessage());
                                            e.printStackTrace();
                                        }
                                        break;
                                    default:
                                        Log.e(TAG, "Failure to decrypt data: " +
                                                "Biometric authentication failed");
                                        callback.decryptionComplete(null);
                                        throw new SecurityException("Failure to decrypt data: " +
                                                "Biometric authentication failed");
                                }
                            });

                }


            } else {
                decryptedData = cipher.doFinal(encryptedData);
                callback.decryptionComplete(decryptedData);
            }
        } catch (GeneralSecurityException | IOException ex) {
            Log.e(TAG, "Failure to decrypt data: " + ex.getLocalizedMessage());
            ex.printStackTrace();
        }
    }

    /**
     * Encode ephemeral data
     *
     * @param keyPairAlias The key pair alias of the RSA AndroidKeyStore used in encryption
     * @param encryptedKey The encrypted key
     * @param cipherText   The encrypted data
     * @param iv           The IV created when the data was encrypted
     * @return The encoded bytes
     */
    public byte[] encodeEphemeralData(byte[] keyPairAlias, byte[] encryptedKey,
                                      byte[] cipherText, byte[] iv) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(((Integer.SIZE / 8) * 4) + iv.length +
                keyPairAlias.length + encryptedKey.length + cipherText.length);
        byteBuffer.putInt(SecureFileEncodingType.EPHEMERAL.getType());
        byteBuffer.putInt(encryptedKey.length);
        byteBuffer.put(encryptedKey);
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.putInt(keyPairAlias.length);
        byteBuffer.put(keyPairAlias);
        byteBuffer.put(cipherText);
        return byteBuffer.array();
    }

    /**
     * Encodes encrypted data
     *
     * @param keyAlias   The key pair alias of the RSA AndroidKeyStore key that the
     *                   data encryption key was encrypted with
     * @param cipherText The encrypted data
     * @param iv         The IV created during the initialal encrypt operation
     * @return The encoded data
     */
    public byte[] encodeSymmetricData(byte[] keyAlias, byte[] cipherText, byte[] iv) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(((Integer.SIZE / 8) * 3) + iv.length +
                keyAlias.length + cipherText.length);
        byteBuffer.putInt(SecureFileEncodingType.SYMMETRIC.getType());
        byteBuffer.putInt(iv.length);
        byteBuffer.put(iv);
        byteBuffer.putInt(keyAlias.length);
        byteBuffer.put(keyAlias);
        byteBuffer.put(cipherText);
        return byteBuffer.array();
    }

    /**
     * Encodes asymmetric data
     *
     * @param keyPairAlias The key pair alias of the RSA AndroidKeyStore key
     * @param cipherText   The cipher text that has been encrypted
     * @return The encoded bytes
     */
    public byte[] encodeAsymmetricData(byte[] keyPairAlias, byte[] cipherText) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(((Integer.SIZE / 8) * 2) +
                keyPairAlias.length + cipherText.length);
        byteBuffer.putInt(SecureFileEncodingType.ASYMMETRIC.getType());
        byteBuffer.putInt(keyPairAlias.length);
        byteBuffer.put(keyPairAlias);
        byteBuffer.put(cipherText);
        return byteBuffer.array();
    }

    /**
     * Decrypts previous encrypted/encoded {@link SecureCipher}
     *
     * @param encodedCipherText The encoded cipher text
     * @return decrypted data
     */
    public byte[] decryptEncodedData(byte[] encodedCipherText) {
        byte[] clearText = new byte[0];
        ByteBuffer byteBuffer = ByteBuffer.wrap(encodedCipherText);
        int encodingTypeVal = byteBuffer.getInt();
        SecureFileEncodingType encodingType = SecureFileEncodingType.fromId(encodingTypeVal);
        byte[] encodedEphKey = null;
        byte[] iv = null;
        String keyAlias = null;
        byte[] cipherText = null;

        switch (encodingType) {
            case EPHEMERAL:
                int encodedEphKeyLength = byteBuffer.getInt();
                encodedEphKey = new byte[encodedEphKeyLength];
                byteBuffer.get(encodedEphKey);
            case SYMMETRIC:
                int ivLength = byteBuffer.getInt();
                iv = new byte[ivLength];
                byteBuffer.get(iv);
            case ASYMMETRIC:
                int keyAliasLength = byteBuffer.getInt();
                byte[] keyAliasBytes = new byte[keyAliasLength];
                byteBuffer.get(keyAliasBytes);
                keyAlias = new String(keyAliasBytes);
                cipherText = new byte[byteBuffer.remaining()];
                byteBuffer.get(cipherText);
                break;
            case NOT_ENCRYPTED:
                throw new SecurityException("Cannot determine file type.");
        }
        switch (encodingType) {
            case EPHEMERAL:
                final byte[] ephemeralCipherText = cipherText;
                final byte[] ephemeralIv = iv;
                String finalKeyAlias = keyAlias;
                byte[] decryptedEphKey = decryptSensitiveDataAsymmetric(keyAlias,
                        encodedEphKey);
                if (decryptedEphKey != null) {
                    EphemeralSecretKey ephemeralSecretKey =
                            new EphemeralSecretKey(decryptedEphKey, secureConfig);
                    clearText = decryptEphemeralData(
                            ephemeralSecretKey,
                            ephemeralCipherText, ephemeralIv, finalKeyAlias);
                    ephemeralSecretKey.destroy();
                } else {
                    Log.i(TAG, "Key was null, this usually means there was an auth " +
                            "failure.");
                }
                break;
            case SYMMETRIC:
                clearText = decryptSensitiveData(
                        keyAlias,
                        cipherText, iv);
                break;
            case ASYMMETRIC:
                clearText = decryptSensitiveDataAsymmetric(
                        keyAlias,
                        cipherText);
                break;
            case NOT_ENCRYPTED:
                throw new SecurityException("File not encrypted.");
        }
        return clearText;
    }

    /**
     * Decrypts previous encrypted/encoded {@link SecureCipher}
     *
     * @param encodedCipherText The encoded cipher text
     * @param callback          The callback, when the data is complete.
     */
    public void decryptEncodedData(byte[] encodedCipherText, SecureDecryptionCallback callback) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(encodedCipherText);
        int encodingTypeVal = byteBuffer.getInt();
        SecureFileEncodingType encodingType = SecureFileEncodingType.fromId(encodingTypeVal);
        byte[] encodedEphKey = null;
        byte[] iv = null;
        String keyAlias = null;
        byte[] cipherText = null;

        switch (encodingType) {
            case EPHEMERAL:
                int encodedEphKeyLength = byteBuffer.getInt();
                encodedEphKey = new byte[encodedEphKeyLength];
                byteBuffer.get(encodedEphKey);
            case SYMMETRIC:
                int ivLength = byteBuffer.getInt();
                iv = new byte[ivLength];
                byteBuffer.get(iv);
            case ASYMMETRIC:
                int keyAliasLength = byteBuffer.getInt();
                byte[] keyAliasBytes = new byte[keyAliasLength];
                byteBuffer.get(keyAliasBytes);
                keyAlias = new String(keyAliasBytes);
                cipherText = new byte[byteBuffer.remaining()];
                byteBuffer.get(cipherText);
                break;
            case NOT_ENCRYPTED:
                throw new SecurityException("Cannot determine file type.");
        }
        switch (encodingType) {
            case EPHEMERAL:
                final byte[] ephemeralCipherText = cipherText;
                final byte[] ephemeralIv = iv;
                String finalKeyAlias = keyAlias;
                decryptSensitiveDataAsymmetric(keyAlias,
                        encodedEphKey,
                        (byte[] decryptedEphKey) -> {
                            if (decryptedEphKey != null) {
                                EphemeralSecretKey ephemeralSecretKey =
                                        new EphemeralSecretKey(decryptedEphKey, secureConfig);
                                byte[] decrypted = decryptEphemeralData(
                                        ephemeralSecretKey,
                                        ephemeralCipherText, ephemeralIv, finalKeyAlias);
                                callback.decryptionComplete(decrypted);
                                ephemeralSecretKey.destroy();
                            } else {
                                Log.i(TAG, "Key was null, this usually means there was an auth " +
                                        "failure.");
                            }
                        });

                break;
            case SYMMETRIC:
                decryptSensitiveData(
                        keyAlias,
                        cipherText, iv, callback);
                break;
            case ASYMMETRIC:
                decryptSensitiveDataAsymmetric(
                        keyAlias,
                        cipherText, callback);
                break;
            case NOT_ENCRYPTED:
                throw new SecurityException("File not encrypted.");
        }
    }

}
