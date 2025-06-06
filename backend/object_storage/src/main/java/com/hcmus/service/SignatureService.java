package com.hcmus.service;

import com.hcmus.utils.AwsSignatureHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class SignatureService {

    private final String secretKey;
    private final String accessKey;
    private final String region;
    private final String service;
    private final String bucket;

    SignatureService(
            @Value("${aws.secret-key}") String secretKey,
            @Value("${aws.access-key}") String accessKey,
            @Value("${aws.region}") String region,
            @Value("${aws.service}") String service,
            @Value("${aws.bucket}") String bucket
    ) {
        this.secretKey = secretKey;
        this.accessKey = accessKey;
        this.region = region;
        this.service = service;
        this.bucket = bucket;
    }

    private final String algorithm = "AWS4-HMAC-SHA256";

    public String generateSignedUrl(String key, String method, int expiresInSeconds) throws Exception {
        String host = "localhost:8081";
        String canonicalUri = "/" + bucket + "/" + key;

        ZonedDateTime now = ZonedDateTime.now();
        String amzDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));
        String date = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        String credentialScope = date + "/" + region + "/" + service + "/aws4_request";
        String credential = accessKey + "/" + credentialScope;
        String signedHeaders = "host";

        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("X-Amz-Algorithm", algorithm);
        queryParams.put("X-Amz-Credential", URLEncoder.encode(credential, StandardCharsets.UTF_8));
        queryParams.put("X-Amz-Date", amzDate);
        queryParams.put("X-Amz-Expires", String.valueOf(expiresInSeconds));
        queryParams.put("X-Amz-SignedHeaders", signedHeaders);

        String canonicalQueryString = queryParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");

        String canonicalRequest = method + "\n" +
                canonicalUri + "\n" +
                canonicalQueryString + "\n" +
                "host:" + host + "\n" +
                "\n" +
                signedHeaders + "\n" +
                AwsSignatureHelper.sha256Hex("");

        String stringToSign = algorithm + "\n" +
                amzDate + "\n" +
                credentialScope + "\n" +
                AwsSignatureHelper.sha256Hex(canonicalRequest);

        byte[] signingKey = AwsSignatureHelper.getSignatureKey(secretKey, date, region, service);
        String signature = AwsSignatureHelper.bytesToHex(AwsSignatureHelper.hmacSHA256(signingKey, stringToSign));

        return "http://" + host + canonicalUri + "?" + canonicalQueryString + "&X-Amz-Signature=" + signature;
    }

    public boolean verifySignature(HttpServletRequest request, String method, String key) throws Exception {
        String queryString = request.getQueryString();
        if (queryString == null) return true;

        Map<String, String> queryParams = Arrays.stream(queryString.split("&"))
                .map(s -> s.split("=", 2))
                .collect(Collectors.toMap(
                        s -> s[0],
                        s -> s.length > 1 ? URLDecoder.decode(s[1], StandardCharsets.UTF_8) : ""
                ));

        String signature = queryParams.get("X-Amz-Signature");
        String credential = queryParams.get("X-Amz-Credential");
        String amzDate = queryParams.get("X-Amz-Date");
        String signedHeaders = queryParams.get("X-Amz-SignedHeaders");

        if (signature == null || credential == null || amzDate == null || signedHeaders == null) return true;

        String[] credParts = credential.split("/");
        if (credParts.length != 5 || !credParts[0].equals(accessKey)) return true;

        String date = credParts[1];
        String region = credParts[2];
        String service = credParts[3];

        String canonicalUri = "/" + key;
        Map<String, String> canonicalQuery = new TreeMap<>(queryParams);
        canonicalQuery.remove("X-Amz-Signature");

        String canonicalQueryString = canonicalQuery.entrySet().stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));

        String canonicalHeaders = "host:" + request.getServerName() + "\n";
        String payloadHash = AwsSignatureHelper.sha256Hex("");

        String canonicalRequest = method + "\n" +
                canonicalUri + "\n" +
                canonicalQueryString + "\n" +
                canonicalHeaders + "\n" +
                signedHeaders + "\n" +
                payloadHash;

        String credentialScope = date + "/" + region + "/" + service + "/aws4_request";
        String stringToSign = algorithm + "\n" +
                amzDate + "\n" +
                credentialScope + "\n" +
                AwsSignatureHelper.sha256Hex(canonicalRequest);

        byte[] signingKey = AwsSignatureHelper.getSignatureKey(secretKey, date, region, service);
        String expectedSig = AwsSignatureHelper.bytesToHex(AwsSignatureHelper.hmacSHA256(signingKey, stringToSign));

        return !expectedSig.equals(signature);
    }
}

