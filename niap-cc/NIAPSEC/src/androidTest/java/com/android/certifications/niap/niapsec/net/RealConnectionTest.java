package com.android.certifications.niap.niapsec.net;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import android.content.Context;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.android.certifications.niap.niapsec.SecureConfig;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * Instrumented test for real network connections using ValidatableSSLSocketFactory.
 * This test requires an active internet connection and runs on an Android device or emulator.
 */
@RunWith(AndroidJUnit4.class)
public class RealConnectionTest {

    private Context context;
    private SecureConfig secureConfig;

    @Before
    public void setUp() {
        // Get the context of the app under test.
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        // Use the default secure configuration.
        secureConfig = SecureConfig.getDefault();
    }

    /**
     * Tests a successful connection to a trusted endpoint (google.com).
     * The TLS handshake and certificate validation should complete without errors.
     */
    @Test
    public void connectToTrustedServer_shouldSucceed() {
        HttpsURLConnection urlConnection = null;

        //We can use badssl.com for testing ssl https://badssl.com/
        //String trustedHost = "https://tls-v1-2.badssl.com:1012/";
        //String trustedHost = "https://www.google.com";
        //String trustedHost = "https://wikipedia.org";
        //revoked.grc.com
        //String trustedHost = "https://revoked.grc.com";
        //String trustedHost = "https://www.grc.com";
        String trustedHost = "https://www.netscaler.com:443";
        try {
            // 1. Create a SecureURL instance for a trusted site.
            SecureURL secureURL = new SecureURL(trustedHost,null ,secureConfig);

            // 2. Create the custom SSLSocketFactory.
            // This factory will perform the certificate validation.
            SSLSocketFactory factory = new ValidatableSSLSocketFactory(secureURL);

            // 3. Create a URL object and open a standard HttpsURLConnection.
            URL url = new URL(trustedHost);
            urlConnection = (HttpsURLConnection) url.openConnection();

            // 4. Set our custom factory on the connection. This is the key step.
            urlConnection.setSSLSocketFactory(factory);

            // 5. Connect and get the response code. This triggers the handshake via our factory.
            int responseCode = urlConnection.getResponseCode();

            // 6. Read some data to ensure the connection is fully established.
            InputStream in = urlConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int bytesRead = in.read(buffer);
            in.close();

            // Assert that the connection was successful.
            System.out.println("Successfully connected to google.com with response code: " + responseCode);
            System.out.println("Read " + bytesRead + " bytes.");

            assertNotNull(in);

        } catch (IOException e) {
            // If any IOException occurs, the test fails.
            e.printStackTrace();
            fail("Connection to trusted server failed with IOException: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

}