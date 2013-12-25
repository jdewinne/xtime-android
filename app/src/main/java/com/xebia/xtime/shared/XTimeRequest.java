package com.xebia.xtime.shared;

import android.util.Log;

import java.security.GeneralSecurityException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public abstract class XTimeRequest {

    private static final String TAG = "XTimeRequest";

    /**
     * Trust every server - do not check for any certificate
     */
    // TODO: Solve issue with security certificate for HTTPS.
    protected void trustAllCertificates() throws GeneralSecurityException {
        Log.w(TAG, "Careful: disabling SSL certificate checks!");

        // Create a trust manager that does not validate certificate chains
        TrustManager trustAllCerts = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                // do not check
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
                // do not check
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        };

        // Install the all-trusting trust manager
        SSLContext sc = SSLContext.getInstance("TLS");
        sc.init(null, new TrustManager[]{trustAllCerts}, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    }
}
