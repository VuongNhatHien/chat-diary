package com.hcmus.ctypto.base64url;

public class Base64UrlDecoder {
    public static String decode(String str) {
        return new String(java.util.Base64.getUrlDecoder().decode(str), java.nio.charset.StandardCharsets.UTF_8);
    }

    public static byte[] decodeToBytes(String str) {
        return java.util.Base64.getUrlDecoder().decode(str);
    }
}
