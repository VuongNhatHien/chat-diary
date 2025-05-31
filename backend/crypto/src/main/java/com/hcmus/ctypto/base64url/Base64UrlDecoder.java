package com.hcmus.ctypto.base64url;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64UrlDecoder {
    public static String decode(String str) {
        return new String(Base64.getUrlDecoder().decode(str), StandardCharsets.UTF_8);
    }

    public static byte[] decodeToBytes(String str) {
        return Base64.getUrlDecoder().decode(str);
    }
}
