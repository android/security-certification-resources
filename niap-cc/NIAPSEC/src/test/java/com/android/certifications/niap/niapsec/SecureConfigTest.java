package com.android.certifications.niap.niapsec;

import android.security.keystore.KeyProperties;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;


/**
 * Unit tests for the {@link SecureConfig} class.
 * These tests run on the local JVM using Robolectric.
 */
@RunWith(RobolectricTestRunner .class)
public class SecureConfigTest {

    @Test
    public void getDefault_returnsNonNullConfig() {
        // Act
        SecureConfig config = SecureConfig.getDefault();

        // Assert
        org.junit.Assert.assertNotNull("The default SecureConfig should not be null.", config);
    }

    @Test
    public void getStrongConfig_hasCorrectDefaultValues() {
        // Act
        SecureConfig config = SecureConfig.getStrongConfig();

        // Assert
        org.junit.Assert.assertNotNull(config);

        // Verify some of the "strong" default values
        org.junit.Assert.assertEquals("Asymmetric key algorithm should be RSA",
                KeyProperties.KEY_ALGORITHM_RSA, config.getAsymmetricKeyPairAlgorithm());

        org.junit.Assert.assertEquals("Asymmetric key size should be 3072",
                3072, config.getAsymmetricKeySize());

        org.junit.Assert.assertEquals("Asymmetric padding should be OAEP",
                KeyProperties.ENCRYPTION_PADDING_RSA_OAEP, config.getAsymmetricPaddings());

        org.junit.Assert.assertEquals("Symmetric key algorithm should be AES",
                KeyProperties.KEY_ALGORITHM_AES, config.getSymmetricKeyAlgorithm());

        org.junit.Assert.assertEquals("Symmetric block mode should be GCM",
                KeyProperties.BLOCK_MODE_GCM, config.getSymmetricBlockModes());

        org.junit.Assert.assertEquals("Symmetric key size should be 256",
                256, config.getSymmetricKeySize());


    }

    @Test
    public void builder_canModifySymmetricKeySize() {
        // Arrange
        int customKeySize = 128;

        // Act
        SecureConfig customConfig = new SecureConfig.Builder()
                .setSymmetricKeySize(customKeySize)
                .build();

        // Assert
        org.junit.Assert.assertEquals("Builder should correctly set the symmetric key size.",
                customKeySize, customConfig.getSymmetricKeySize());
    }

    @Test
    public void getStrongDeviceCredentialConfig_setsValiditySeconds() {
        // Act
        SecureConfig config = SecureConfig.getStrongDeviceCredentialConfig(null);

        // Assert
        org.junit.Assert.assertEquals("Asymmetric auth validity should be 10 seconds",
                10, config.getAsymmetricRequireUserValiditySeconds());
        org.junit.Assert.assertEquals("Symmetric auth validity should be 10 seconds",
                10, config.getSymmetricRequireUserValiditySeconds());
    }
}