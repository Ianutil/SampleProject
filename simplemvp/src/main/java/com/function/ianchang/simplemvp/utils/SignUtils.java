/*
 * Copyright (c) 2018 Ian All copyright reserved.
 * Author  Ian
 * Project name  MyApplication
 * Class name  SignUtils
 * Created by  ianchang on 2018-01-30 14:57:27
 * Last modify date   2017-12-20 14:00:27
 */

package com.function.ianchang.simplemvp.utils;

import android.util.Base64;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * Created by martin.du on 2017/9/21.
 */

public class SignUtils {
    private static final String RSA_KEY = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBALEt75/1H4KXKLWOkoy6GPZl0jbG+CzfZwIioIjxp+dfvoEL0aGLipZSIK1p5xJHqoLH1k33ySVBMWzg7xF81lVSKs0ZSblx5vCnQ5trNnqsKiyyH+3zfofshPmDGEHUfZXAbSWYk/17j0d8FdUR4Uh8fZCtbvmEFZ4urBcZ/BPvAgMBAAECgYBnKFSk8CXZdcLo+kqPDubrxtq+s9fY4HVsA9cuBGz6xH75SXVTNhiJbuXBsbAsr3cyT8GRgZwmSa/KMahfqSxsZqqGvZck1vYs5rm8GUSEdTAi+pkKClJigun22P/ZpqH4iM5c4ZHhyut8xy7AMXXT97MlakIXkGeB7iy/oFY1kQJBANn/1g/v7jf09dupHi4GvJKipXzkKLwYRyLFYzM8flHbqPelzrU7XKkZQJradQ9Ir3f7vCiQ4hZYrRHzhfXqv3kCQQDQEIWH4rXzdYa/kCNiOupEkjb0h9PR2p+6FFMxsj5Is6fxh9OEZnkxUceN+0l3UIaEWv2CUI6bL2do2f4+e4ynAkBDuX8/JELVj3xx6XA+zEj5JR5UVm1xQfmXi8rtt/VmaN9tUE2PcAmxXu77LLMBNIuOst4ZnCkg2BusYANWCr/BAkBR9oAzxIp6ysx3QDfAJYdN1e+SKsO1wpLzgbg4Nog0qZk6G5CmcpvBYv7inqAVQYKMNy/wmv1igq0Q7/co9BC3AkA2Oh2dBCgAYmNhOANRm+PWQ92P4uRYtZ8dtYiNB2DP9YTWQu4z+WhlCcMaxa9yfBjW/+Kwhj7tCNKPApTiAtyK";
//    private static final String RSA_KEY = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALX4Lo/0juTNZR9RFFvTmvZ1DaTdLE2HWS5k+izx1RNrTbd7Vg7/slYYhmRY/TcU1J9/Tdb9KChE9TSEMQPU1LpbaJhT5BxACulkLZDj2FufubhzIgbCYdBFkFy3xW0BVvTjLaIQDY4Qr1thg97KRa8ZiJ85mw/bt2NQQVnzAwUxAgMBAAECgYA2cMzFZr5Fd6m0R9aWbmVjLLvrQfxaKY8l0TIDtkowKB81MKIblKYvGeFDLlh7XTELktOf6VdVTOQrVQ0w/0CxCeyhSUfxATamgEcmpxPdqmbVtSkzBoGy8kaAGUCB672b2gPcJ5V21XloY+RfOVNakeb0R55C6vLdpLBblNfNwQJBAPipyKAsy5XtH5cEuUvkGnit0gqnGt3dfU1Pou9OJRdNjoSEjlYPIqTfqCkJIbdH/B8NdU9JuZgJSZeSwUx1p/kCQQC7VqSx4ptfRIE1NjneCPPXVGN9c0E78n1MqF0jDrjCQm+vGw8B4Atqw350hEFaqftY/Z9dWh32NIyZTp0tuMT5AkEA6mSgiNuwzBJIxMHfKHpLuZWfeAsseBZgFpAKtiijLeQdgyywPs7liSSKDqRc87cXIO4+tg54s6eNhyL+smP1gQJBAJYYSUQspwehP1R+6cY3rgZsGno8iZuaIUH18wlPlkAuMoU9TLzX4M3da8e23xXg8vzN141X0oGcgLmj/tLPIyECQGHCODCsAuZbKLkhUYPVbtebdqJgizbfte/YqbVj1ywQrmZh+3AV8uTyvnSa5Kv5VB2G8yjM5B/iyMLyP6nzQ68=";

    private static final String SIGN_ALGORITHMS = "SHA1WithRSA";

    public static String sign(String content) {
        return sign(content, RSA_KEY);
    }

    public static String sign(String content, String privateKey) {
        try {
            PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey, Base64.DEFAULT));
            KeyFactory keyf = KeyFactory.getInstance("RSA");
            PrivateKey priKey = keyf.generatePrivate(priPKCS8);
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);
            signature.initSign(priKey);
            signature.update(content.getBytes());
            byte[] signed = signature.sign();
            return new String(Base64.encode(signed, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
