package com.android.certifications.niap.niapsec.net;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.android.certifications.niap.niapsec.SecureConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;

import javax.net.ssl.SSLSocket;

@RunWith(RobolectricTestRunner.class)
public class ValidatableSSLSocketTest {

    @Mock
    private SecureURL mockSecureURL;

    @Mock
    private SSLSocket mockSslSocket;

    private SecureConfig secureConfig;
    private ValidatableSSLSocket validatableSocket;

    // Cipher suites to use for testing
    private final String[] strongCipherSuites = new String[]{"TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384"};

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        // Configure the mock SecureURL
        when(mockSecureURL.getHostname()).thenReturn("example.com");
        // Assume the certificate is valid by default
        when(mockSecureURL.isValid(mockSecureURL.getHostname(), mockSslSocket)).thenReturn(true);

        // Configure the mock SSLSocket
        doNothing().when(mockSslSocket).startHandshake();
    }

    @Test
    public void constructor_whenStrongCiphersEnabled_setsEnabledCipherSuites() throws IOException {
        // Arrange
        // Configure to enable strong cipher suites
        secureConfig = new SecureConfig.Builder().
                setUseStrongSSLCiphers(true).build();

        // Act
        validatableSocket = new ValidatableSSLSocket(mockSecureURL, mockSslSocket, secureConfig, strongCipherSuites);

        // Assert
        // Verify that setEnabledCipherSuites was called with the specified cipher suites
        verify(mockSslSocket, times(1)).setEnabledCipherSuites(strongCipherSuites);
    }

    @Test
    public void constructor_whenStrongCiphersDisabled_doesNotSetEnabledCipherSuites() throws IOException {
        // Arrange
        // Configure to disable strong cipher suites
        secureConfig = new SecureConfig.Builder().setUseStrongSSLCiphers(false).build();

        // Act
        validatableSocket = new ValidatableSSLSocket(mockSecureURL, mockSslSocket, secureConfig, strongCipherSuites);

        // Assert
        // Verify that setEnabledCipherSuites was not called
        verify(mockSslSocket, times(0)).setEnabledCipherSuites(strongCipherSuites);
    }

    @Test
    public void constructor_performsHandshakeAndValidation() throws IOException {
        // Arrange
        secureConfig = SecureConfig.getDefault();

        // Act
        validatableSocket = new ValidatableSSLSocket(mockSecureURL, mockSslSocket, secureConfig, strongCipherSuites);

        // Assert
        // Verify that startHandshake was called once
        verify(mockSslSocket, times(1)).startHandshake();
        // Verify that SecureURL#isValid was called once
        verify(mockSecureURL, times(1)).isValid(mockSecureURL.getHostname(), mockSslSocket);
    }

    @Test
    public void startHandshake_isIdempotent() throws IOException {
        // Arrange
        secureConfig = SecureConfig.getDefault();
        validatableSocket = new ValidatableSSLSocket(mockSecureURL, mockSslSocket, secureConfig, strongCipherSuites);

        // Act
        // Call startHandshake multiple times
        validatableSocket.startHandshake();
        validatableSocket.startHandshake();

        // Assert
        // The internal sslSocket.startHandshake() should only be called once in the constructor,
        // because the handshakeStarted flag is set to true. Subsequent manual calls should do nothing.
        verify(mockSslSocket, times(1)).startHandshake();
    }

    @Test
    public void close_delegatesToWrappedSocket() throws IOException {
        // Arrange
        secureConfig = SecureConfig.getDefault();
        validatableSocket = new ValidatableSSLSocket(mockSecureURL, mockSslSocket, secureConfig, strongCipherSuites);

        // Act
        validatableSocket.close();

        // Assert
        // Verify that the close() method of the wrapped mockSslSocket was called once
        verify(mockSslSocket, times(1)).close();
    }
}