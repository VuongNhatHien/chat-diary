package com.hcmus.ctypto.base64url;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class Base64UrlEncoder {
    public static String encode(String str) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(str.getBytes(StandardCharsets.UTF_8));
    }

    public static String encode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}
