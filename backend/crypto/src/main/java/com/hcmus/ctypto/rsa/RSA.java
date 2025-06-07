package com.hcmus.ctypto.rsa;

import com.hcmus.hashing.sha256.SHA256;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;

public class RSA {
    private static final byte[] SHA256_DER_PREFIX = new byte[] {
            0x30, 0x31,
            0x30, 0x0d,
            0x06, 0x09,
            0x60, (byte)0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x01,
            0x05, 0x00,
            0x04, 0x20
    };

    public static byte[] sign(String data, String keyPem) throws Exception {
        PrivateKey privateKey = getPrivateKeyFromString(keyPem);
        return sign(data, privateKey);
    }

    public static byte[] sign(String data, PrivateKey key) {
        if (!(key instanceof RSAPrivateKey rsaKey)) {
            throw new IllegalArgumentException("Key must be RSAPrivateKey");
        }

        BigInteger d = rsaKey.getPrivateExponent();
        BigInteger n = rsaKey.getModulus();

        byte[] hash = SHA256.hash(data.getBytes(StandardCharsets.UTF_8));

        byte[] digestInfo = new byte[SHA256_DER_PREFIX.length + hash.length];
        System.arraycopy(SHA256_DER_PREFIX, 0, digestInfo, 0, SHA256_DER_PREFIX.length);
        System.arraycopy(hash, 0, digestInfo, SHA256_DER_PREFIX.length, hash.length);

        int k = (n.bitLength() + 7) / 8;
        int psLen = k - digestInfo.length - 3;
        if (psLen < 8) throw new IllegalArgumentException("Key too short");

        byte[] padded = new byte[k];
        padded[0] = 0x00;
        padded[1] = 0x01;
        Arrays.fill(padded, 2, 2 + psLen, (byte) 0xFF);
        padded[2 + psLen] = 0x00;
        System.arraycopy(digestInfo, 0, padded, 3 + psLen, digestInfo.length);

        BigInteger m = new BigInteger(1, padded);
        BigInteger s = m.modPow(d, n);

        byte[] sig = s.toByteArray();
        if (sig.length == k) return sig;
        if (sig.length == k + 1 && sig[0] == 0x00) return Arrays.copyOfRange(sig, 1, sig.length);
        byte[] result = new byte[k];
        System.arraycopy(sig, 0, result, k - sig.length, sig.length);
        return result;
    }

    public static void verifySignature(String data, byte[] signature, String keyPem) throws Exception {
        PublicKey publicKey = getPublicKeyFromString(keyPem);
        verifySignature(data, signature, publicKey);
    }

    public static void verifySignature(String data, byte[] signature, PublicKey key) {
        if (!(key instanceof RSAPublicKey rsaKey)) {
            throw new IllegalArgumentException("Key must be RSAPublicKey");
        }

        BigInteger e = rsaKey.getPublicExponent();
        BigInteger n = rsaKey.getModulus();

        BigInteger s = new BigInteger(1, signature);
        BigInteger m = s.modPow(e, n);

        int k = (n.bitLength() + 7) / 8;
        byte[] padded = m.toByteArray();
        if (padded.length < k) {
            byte[] tmp = new byte[k];
            System.arraycopy(padded, 0, tmp, k - padded.length, padded.length);
            padded = tmp;
        } else if (padded.length > k && padded[0] == 0x00) {
            padded = Arrays.copyOfRange(padded, 1, padded.length);
        }

        if (padded[0] != 0x00 || padded[1] != 0x01) {
            throw new SecurityException("Invalid padding");
        }

        int i = 2;
        while (i < padded.length && padded[i] == (byte) 0xFF) i++;
        if (padded[i] != 0x00) throw new SecurityException("Invalid padding (missing 0x00)");
        i++;

        int digestInfoLen = SHA256_DER_PREFIX.length + 32;
        if (i + digestInfoLen > padded.length) throw new SecurityException("Invalid DigestInfo length");

        byte[] digestInfo = Arrays.copyOfRange(padded, i, i + digestInfoLen);

        for (int j = 0; j < SHA256_DER_PREFIX.length; j++) {
            if (digestInfo[j] != SHA256_DER_PREFIX[j]) {
                throw new SecurityException("Invalid digest prefix");
            }
        }

        byte[] hashFromSig = Arrays.copyOfRange(digestInfo, SHA256_DER_PREFIX.length, digestInfo.length);
        byte[] expectedHash = SHA256.hash(data.getBytes(StandardCharsets.UTF_8));

        if (!Arrays.equals(hashFromSig, expectedHash)) {
            throw new SecurityException("Invalid hash in signature");
        }
    }

    private static PrivateKey getPrivateKeyFromString(String keyPem) throws Exception {
        String privateKeyPEM = keyPem
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(privateKeyPEM);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(keySpec);
    }

    private static PublicKey getPublicKeyFromString(String keyPem) throws Exception {
        String publicKeyPEM = keyPem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
        java.security.spec.X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(keySpec);
    }
}
