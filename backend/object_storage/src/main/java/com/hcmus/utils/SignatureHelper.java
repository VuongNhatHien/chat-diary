package com.hcmus.utils;

import com.hcmus.property.SignatureProperties;
import com.hcmus.ctypto.rsa.RSA;
import org.springframework.stereotype.Component;

@Component
public class SignatureHelper {
    private final SignatureProperties properties;

    public SignatureHelper(SignatureProperties properties) {
        this.properties = properties;
    }

    public byte[] sign(String data) throws Exception {
        return RSA.sign(data, properties.getRsaPrivateKey());
    }

    public void verify(String data, byte[] signature) throws Exception {
        RSA.verifySignature(data, signature, properties.getRsaPublicKey());
    }
}
