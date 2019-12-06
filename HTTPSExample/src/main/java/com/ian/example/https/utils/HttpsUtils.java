package com.ian.example.https.utils;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

/**
 * Created by ianchang on 2018/9/17.
 */

public class HttpsUtils {

    public static String CER_12306 = "-----BEGIN CERTIFICATE-----\n" +
            "MIICmjCCAgOgAwIBAgIIbyZr5/jKH6QwDQYJKoZIhvcNAQEFBQAwRzELMAkGA1UE\n" +
            "BhMCQ04xKTAnBgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24gQXV0aG9yaXR5\n" +
            "MQ0wCwYDVQQDEwRTUkNBMB4XDTA5MDUyNTA2NTYwMFoXDTI5MDUyMDA2NTYwMFow\n" +
            "RzELMAkGA1UEBhMCQ04xKTAnBgNVBAoTIFNpbm9yYWlsIENlcnRpZmljYXRpb24g\n" +
            "QXV0aG9yaXR5MQ0wCwYDVQQDEwRTUkNBMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCB\n" +
            "iQKBgQDMpbNeb34p0GvLkZ6t72/OOba4mX2K/eZRWFfnuk8e5jKDH+9BgCb29bSo\n" +
            "tqPqTbxXWPxIOz8EjyUO3bfR5pQ8ovNTOlks2rS5BdMhoi4sUjCKi5ELiqtyww/X\n" +
            "gY5iFqv6D4Pw9QvOUcdRVSbPWo1DwMmH75It6pk/rARIFHEjWwIDAQABo4GOMIGL\n" +
            "MB8GA1UdIwQYMBaAFHletne34lKDQ+3HUYhMY4UsAENYMAwGA1UdEwQFMAMBAf8w\n" +
            "LgYDVR0fBCcwJTAjoCGgH4YdaHR0cDovLzE5Mi4xNjguOS4xNDkvY3JsMS5jcmww\n" +
            "CwYDVR0PBAQDAgH+MB0GA1UdDgQWBBR5XrZ3t+JSg0Ptx1GITGOFLABDWDANBgkq\n" +
            "hkiG9w0BAQUFAAOBgQDGrAm2U/of1LbOnG2bnnQtgcVaBXiVJF8LKPaV23XQ96HU\n" +
            "8xfgSZMJS6U00WHAI7zp0q208RSUft9wDq9ee///VOhzR6Tebg9QfyPSohkBrhXQ\n" +
            "envQog555S+C3eJAAVeNCTeMS3N/M5hzBRJAoffn3qoYdAO1Q8bTguOi+2849A==\n" +
            "-----END CERTIFICATE-----";


    /********
     *
     * 载入一个默认的证书
     * @param trustManager
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(X509TrustManager trustManager) {
        SSLSocketFactory socketFactory = null;

        try {

            SSLContext sslContext = SSLContext.getInstance("SSL");

            sslContext.init(null, new X509TrustManager[]{trustManager}, null);
            socketFactory = sslContext.getSocketFactory();
        } catch (GeneralSecurityException e) {
            throw new RuntimeException(e);
        }

        return socketFactory;
    }


    /**
     * 载入证书
     * @param certificates
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(InputStream... certificates) {

        SSLSocketFactory socketFactory = null;

        try {
            // 用我们的证书创建一个keystore
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");

            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());

            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                // 证书别名
                String alias = Integer.toString(index++);

                keyStore.setCertificateEntry(alias, certificateFactory.generateCertificate(certificate));
                try {
                    if (certificate != null) {
                        certificate.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 创建一个TrustManager，只信任我们创建的KeyStore
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());

            socketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return socketFactory;
    }

}
