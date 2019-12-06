package com.ian.example.https.service;

import android.util.Log;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * Created by ianchang on 2018/9/17.
 */

public class ServiceHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify(String hostname, SSLSession session) {
        Log.d("ServiceHostnameVerifier", "hostname:"+hostname);

        return true;
    }




}
