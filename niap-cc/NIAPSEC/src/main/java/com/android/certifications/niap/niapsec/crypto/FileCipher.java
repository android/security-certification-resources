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

import android.util.Base64;
import android.util.Log;
import android.util.Pair;

import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.concurrent.Executor;

import androidx.annotation.NonNull;
import com.android.certifications.niap.niapsec.SecureConfig;
import com.android.certifications.niap.niapsec.context.SecureContextCompat;

/**
 * Combines Cipher and File to allow for easy to write and read encrypted files.
 *
 * As per NIAP, this class encrypts the file contents with an ephemeral symmetric data encryption
 * key and encodes the necessary information for decryption. The ephemeral key is encrypted with an
 * asymmetric key encryption key.
 */
public class FileCipher {

    private String mFileName;
    private String mKeyPairAlias;
    private FileInputStream mFileInputStream;
    private FileOutputStream mFileOutputStream;

    private Executor mExecutor;
    private SecureContextCompat.EncryptedFileInputStreamListener mListener;

    private SecureConfig mSecureConfig;

    /**
     * Instantiates a FileCipher to handle read. Encryption and decryption is
     * handled internally.
     *
     * @param fileName The file path of the file to open
     * @param fileInputStream The input stream of the File to read
     * @param secureConfig The secure configuration used, which specifies algorithms, key sizes, etc
     * @param executor An executor
     * @param listener The listener for callbacks, this is necessary for key auth using
     *                 BiometricPrompt
     * @throws IOException When there is a file read issue
     */
    public FileCipher(String fileName, FileInputStream fileInputStream,
                      SecureConfig secureConfig, Executor executor,
                      SecureContextCompat.EncryptedFileInputStreamListener listener)
            throws IOException {
        mFileName = fileName;
        mFileInputStream = fileInputStream;
        mSecureConfig = secureConfig;
        EncryptedFileInputStream encryptedFileInputStream =
                new EncryptedFileInputStream(mFileInputStream);
        setEncryptedFileInputStreamListener(executor, listener);
        encryptedFileInputStream.decrypt(listener);
    }

    /**
     * Instantiates a FileCipher to handle read. Encryption and decryption is
     * handled internally.
     *
     * @param keyPairAlias The RSA key pair alias of the key stored in the AndroidKeyStore
     * @param fileOutputStream The output stream for writing
     * @param secureConfig The secure configuration used, which specifies algorithms, key sizes, etc
     */
    public FileCipher(String keyPairAlias, FileOutputStream fileOutputStream,
                      SecureConfig secureConfig) {
        mKeyPairAlias = keyPairAlias;
        mFileOutputStream = new EncryptedFileOutputStream(
                mFileName,
                mKeyPairAlias,
                fileOutputStream);
        mSecureConfig = secureConfig;
    }

    /**
     * Set the executor and listener for keystore backed authenticated requests
     *
     * @param executor The executor for the callback
     * @param listener The listener which is called after a biometric authorization
     */
    public void setEncryptedFileInputStreamListener(@NonNull Executor executor,
                                                    @NonNull
                                                            SecureContextCompat
                                                                    .EncryptedFileInputStreamListener
                                                            listener) {
        mExecutor = executor;
        mListener = listener;
    }

    /**
     * @return The file output stream for writing
     */
    public FileOutputStream getFileOutputStream() {
        return mFileOutputStream;
    }

    /**
     * @return The input stream for reading
     */
    public FileInputStream getFileInputStream() {
        return mFileInputStream;
    }

    /**
     * Internal class adding encryption to writes
     */
    class EncryptedFileOutputStream extends FileOutputStream {
        private static final String WRITE_NOT_SUPPORTED = "For encrypted files, you must write " +
                "all data simultaneously. Call #write(byte[]).";

        private static final String TAG = "EncryptedFOS";

        private FileOutputStream fileOutputStream;
        private String keyPairAlias;

        EncryptedFileOutputStream(String name,
                                  String keyPairAlias,
                                  FileOutputStream fileOutputStream) {
            super(new FileDescriptor());
            this.keyPairAlias = keyPairAlias;
            this.fileOutputStream = fileOutputStream;
        }

        private String getAsymKeyPairAlias() {
            return this.keyPairAlias;
        }

        @Override
        public void write(@NonNull byte[] b) {
            SecureKeyStore secureKeyStore = SecureKeyStore.getDefault(mSecureConfig);
            if (!secureKeyStore.keyExists(getAsymKeyPairAlias())) {
                SecureKeyGenerator keyGenerator = SecureKeyGenerator.getInstance(mSecureConfig);
                keyGenerator.generateAsymmetricKeyPair(getAsymKeyPairAlias());
            }
            SecureKeyGenerator secureKeyGenerator = SecureKeyGenerator.getInstance(mSecureConfig);
            EphemeralSecretKey secretKey = secureKeyGenerator.generateEphemeralDataKey();
            if(mSecureConfig.isDebugLoggingEnabled()) {
                Log.i("FileCipher", "Calling: " + SecureRandom.class.getSimpleName() +
                        " EphemeralSecretKey: Base64:\n" +
                        Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT));
            }
            SecureCipher secureCipher = SecureCipher.getDefault(mSecureConfig);
            Pair<byte[], byte[]> encryptedData = secureCipher.encryptEphemeralData(secretKey, b,
                    keyPairAlias);
            secureCipher.encryptSensitiveDataAsymmetric(
                    getAsymKeyPairAlias(),
                    secretKey.getEncoded(),
                    (byte[] encryptedEphemeralKey) -> {
                        byte[] encodedData = secureCipher.encodeEphemeralData(
                                getAsymKeyPairAlias().getBytes(),
                                encryptedEphemeralKey,
                                encryptedData.first,
                                encryptedData.second);
                        secretKey.destroy();
                        try {
                            fileOutputStream.write(encodedData);
                        } catch (IOException e) {
                            Log.e(TAG, "Failed to write secure file.");
                            e.printStackTrace();
                        }
                    });
        }

        @Override
        public void write(int b) throws IOException {
            throw new UnsupportedOperationException(WRITE_NOT_SUPPORTED);
        }

        @Override
        public void write(@NonNull byte[] b, int off, int len) throws IOException {
            throw new UnsupportedOperationException(WRITE_NOT_SUPPORTED);
        }

        @Override
        public void close() throws IOException {
            fileOutputStream.close();
        }

        @NonNull
        @Override
        public FileChannel getChannel() {
            throw new UnsupportedOperationException(WRITE_NOT_SUPPORTED);
        }

        @Override
        protected void finalize() throws IOException {
            super.finalize();
        }

        @Override
        public void flush() throws IOException {
            fileOutputStream.flush();
        }
    }

    /**
     * Internal class adding encryption to reads
     */
    class EncryptedFileInputStream extends FileInputStream {
        private static final String READ_NOT_SUPPORTED = "For encrypted files, you must read all " +
                "data simultaneously. Call #read(byte[]).";

        // Was 25 characters, truncating to fix compile error
        private static final String TAG = "EncryptedFIS";

        private FileInputStream fileInputStream;
        private byte[] decryptedData;
        private int readStatus = 0;

        EncryptedFileInputStream(FileInputStream fileInputStream) {
            super(new FileDescriptor());
            this.fileInputStream = fileInputStream;
        }

        @Override
        public int read() throws IOException {
            throw new UnsupportedOperationException(READ_NOT_SUPPORTED);
        }

        void decrypt(SecureContextCompat.EncryptedFileInputStreamListener listener)
                throws IOException {
            if (this.decryptedData == null) {
                try {
                    byte[] encodedData = new byte[fileInputStream.available()];
                    readStatus = fileInputStream.read(encodedData);
                    SecureCipher secureCipher = SecureCipher.getDefault(mSecureConfig);
                    secureCipher.decryptEncodedData(encodedData, decryptedData -> {
                        this.decryptedData = decryptedData;
                        //Binder.clearCallingIdentity();
                        listener.onEncryptedFileInput(this);
                    });
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        private void destroyCache() {
            if (decryptedData != null) {
                Arrays.fill(decryptedData, (byte) 0);
                decryptedData = null;
            }
        }

        @Override
        public int read(@NonNull byte[] b) {
            System.arraycopy(decryptedData, 0, b, 0, decryptedData.length);
            return readStatus;
        }

        // TODO, implement this
        @Override
        public int read(@NonNull byte[] b, int off, int len) throws IOException {
            throw new UnsupportedOperationException(READ_NOT_SUPPORTED);
        }

        // TODO, implement this
        @Override
        public long skip(long n) throws IOException {
            throw new UnsupportedOperationException(READ_NOT_SUPPORTED);
        }

        @Override
        public int available() {
            return decryptedData.length;
        }

        @Override
        public void close() throws IOException {
            destroyCache();
            fileInputStream.close();
        }

        @Override
        public FileChannel getChannel() {
            throw new UnsupportedOperationException(READ_NOT_SUPPORTED);
        }

        @Override
        protected void finalize() throws IOException {
            destroyCache();
            super.finalize();
        }

        @Override
        public synchronized void mark(int readlimit) {
            throw new UnsupportedOperationException(READ_NOT_SUPPORTED);
        }

        @Override
        public synchronized void reset() throws IOException {
            throw new UnsupportedOperationException(READ_NOT_SUPPORTED);
        }

        @Override
        public boolean markSupported() {
            return false;
        }

    }

}
