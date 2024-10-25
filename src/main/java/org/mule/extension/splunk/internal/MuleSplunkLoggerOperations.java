package org.mule.extension.splunk.internal;

import static org.mule.runtime.extension.api.annotation.param.MediaType.ANY;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.mule.runtime.extension.api.annotation.error.ErrorTypes;
import org.mule.runtime.extension.api.annotation.param.MediaType;
import org.mule.runtime.extension.api.annotation.param.Config;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;


import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.security.*;
import java.security.cert.X509Certificate;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.TrustManagerFactory;
import java.util.HashMap;
import java.util.Map;

public class MuleSplunkLoggerOperations {

    @DisplayName("Secured Log to Splunk")
    @MediaType(value = ANY, strict = false)
    public void logToSplunkSecured(@Config MuleSplunkLoggerConfiguration config, SplunkLogPayloadRequest payload) {
        processLogToSplunk(config, payload, true);

    }

    @DisplayName("Unsecured Log to Splunk")
    @MediaType(value = ANY, strict = false)
    public void logToSplunkUnSecured(@Config MuleSplunkLoggerConfiguration config, SplunkLogPayloadRequest payload) {
        processLogToSplunk(config, payload, false);
    }

    private void processLogToSplunk(MuleSplunkLoggerConfiguration config, SplunkLogPayloadRequest payload, boolean tlsFlag) {

        try {
            HttpsURLConnection connection;
            if (tlsFlag) {
                connection = securedConnection(config);
            } else {
                connection = unSecuredConnection(config);
            }
            invokeSplunkHttpEventCollector(connection, payload);


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void invokeSplunkHttpEventCollector(HttpsURLConnection connection, SplunkLogPayloadRequest payload) throws IOException {
        Map < String, Object > request = new HashMap < > ();
        request.put("event", payload);

        String logEvent = new ObjectMapper().writeValueAsString(request);

        System.out.println(logEvent);

        try (OutputStream os = connection.getOutputStream()) {
            os.write(logEvent.getBytes());
            os.flush();
        }

        int responseCode = connection.getResponseCode();

        System.out.println(responseCode);
    }

    private HttpsURLConnection securedConnection(MuleSplunkLoggerConfiguration config) throws IOException, KeyManagementException, NoSuchAlgorithmException, KeyStoreException {

        String jksFilePath = config.getSplunkTsConfigDto().getTrustStoreLocation(); // Replace with the actual path to your JKS file
        String jksPassword = config.getSplunkTsConfigDto().getTrustStorePassword();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        try (InputStream trustStoreStream = getClass().getResourceAsStream(jksFilePath)) {
            keyStore.load(trustStoreStream, jksPassword.toCharArray());
        } catch (Exception ex) {
            System.out.println("Exception occured ---- File Not Found on location");
        }

        // Create a TrustManagerFactory using the JKS
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(keyStore);

        // Create an SSLContext that uses the TrustManagerFactory
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);

        // Set the default SSLContext for HttpsURLConnection
        HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());

        // Now open the connection to Splunk
        URL url = new URL(config.getSplunkUrl() + "/services/collector");
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(); // Use HttpsURLConnection for SSL support
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Splunk " + config.getAuthToken());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);
        return connection;
    }

    private HttpsURLConnection unSecuredConnection(MuleSplunkLoggerConfiguration config) throws IOException, KeyManagementException, NoSuchAlgorithmException {
        TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) {}

                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) {}

                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }
                }
        };

        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new SecureRandom());

        // Set default SSL context
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

        // Now open the connection to Splunk
        URL url = new URL(config.getSplunkUrl());
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection(); // Use HttpsURLConnection for SSL support
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Authorization", "Splunk " + config.getAuthToken());
        connection.setRequestProperty("Content-Type", "application/json");
        connection.setDoOutput(true);

        return connection;
    }
}