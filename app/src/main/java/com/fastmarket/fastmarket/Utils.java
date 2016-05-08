package com.fastmarket.fastmarket;

import android.util.Base64;
import android.util.Xml;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.Formatter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Sebastian on 5/05/2016.
 */
public class Utils {
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    public static String calculateRFC2104HMAC(String data, String key)
            throws SignatureException, NoSuchAlgorithmException, InvalidKeyException {
        SecretKeySpec signingKey = new SecretKeySpec(key.getBytes(), HMAC_SHA1_ALGORITHM);
        Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
        mac.init(signingKey);
        return Base64.encode(mac.doFinal(data.getBytes()),Base64.DEFAULT).toString();
    }
}
