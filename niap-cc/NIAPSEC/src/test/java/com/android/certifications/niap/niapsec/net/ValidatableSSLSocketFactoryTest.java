package com.android.certifications.niap.niapsec.net;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.android.certifications.niap.niapsec.SecureConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.robolectric.RobolectricTestRunner;

import java.io.IOException;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

@RunWith(RobolectricTestRunner.class)
public class ValidatableSSLSocketFactoryTest {

    @Mock
    private SecureURL mockSecureURL;

    @Mock
    private SSLSocket mockSslSocket;
    @Mock
    private SSLSocketFactory mockSslSocketFactory;

    private SecureConfig secureConfig;


    private ValidatableSSLSocketFactory factory;

    @Before
    public void setUp() throws Exception {
        // Initialize Mockito annotations
        MockitoAnnotations.initMocks(this);

        // Use a real instance for SecureConfig
        secureConfig = SecureConfig.getDefault();

        // Configure the mock SecureURL
        when(mockSecureURL.getHostname()).thenReturn("example.com");
        // Configure isValid to return true (for the valid certificate case)
        when(mockSecureURL.isValid(anyString(), any(SSLSocket.class))).thenReturn(true);

        // Configure the mock SSLSocket
        // Do nothing when startHandshake() is called
        doNothing().when(mockSslSocket).startHandshake();

        when(mockSslSocketFactory.getSupportedCipherSuites()).thenReturn(new String[0]);
        when(mockSslSocketFactory.createSocket(any(Socket.class), anyString(), any(int.class), any(boolean.class)))
                .thenReturn(mockSslSocket);

        // Initialize the factory
        factory = new ValidatableSSLSocketFactory(mockSecureURL, mockSslSocketFactory,secureConfig);
    }

    @Test
    public void createSocket_returnsValidatableSSLSocketInstance() throws IOException {
        // Act
        // Call the factory's createSocket method, which internally calls
        // new ValidatableSSLSocket()
        Socket socket = factory.createSocket(mockSslSocket, "example.com", 443, true);

        // Assert
        // Verify that the returned socket is not null
        assertNotNull(socket);
        // Verify that the returned socket is an instance of ValidatableSSLSocket
        assertTrue(socket instanceof ValidatableSSLSocket);
    }

    @Test(expected = IOException.class)
    public void createSocket_withInvalidCertificate_throwsIOException() throws IOException {
        // Arrange
        // Configure SecureURL#isValid to return false (for the invalid certificate case)
        when(mockSecureURL.isValid(anyString(), any(SSLSocket.class))).thenReturn(false);

        // Act & Assert
        // Expect an IOException to be thrown from the constructor when createSocket is called,
        // as it invokes isValid()
        factory.createSocket(mockSslSocket, "example.com", 443, true);
    }
}