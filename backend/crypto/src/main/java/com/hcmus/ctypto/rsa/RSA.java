package com.hcmus.ctypto.rsa;

import org.springframework.security.oauth2.jwt.JwtException;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

public class RSA {
    public static byte[] sign(String data, PrivateKey key) throws Exception {
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initSign(key);
        signature.update(data.getBytes(StandardCharsets.UTF_8));
        return signature.sign();
    }

    public static void verifySignature(byte[] data, byte[] signature, PublicKey key) throws Exception {
        Signature sig = Signature.getInstance("SHA256withRSA");
        sig.initVerify(key);
        sig.update(data);
        if (!sig.verify(signature)) {
            throw new JwtException("Invalid JWT signature");
        }
    }
}
