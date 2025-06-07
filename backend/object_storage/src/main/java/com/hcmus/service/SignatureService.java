package com.hcmus.service;

import com.hcmus.ctypto.base64url.Base64UrlDecoder;
import com.hcmus.ctypto.base64url.Base64UrlEncoder;
import com.hcmus.property.ObjectStorageProperties;
import com.hcmus.utils.SignatureHelper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class SignatureService {
    private final String endpoint;
    private final SignatureHelper signatureHelper;

    public SignatureService(
        @Value("${domain.object-storage}") String domain,
        SignatureHelper signatureHelper
    ) {
        this.endpoint = "http://" + domain + "/pre-signed-url";
        this.signatureHelper = signatureHelper;
    }

    public String generateSignedUrl(ObjectStorageProperties properties, String key, String method, int expiresInSeconds) throws Exception {
        String canonicalUri = "/" + properties.getBucket() + "/" + key;

        ZonedDateTime now = ZonedDateTime.now();
        String amzDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss'Z'"));

        Map<String, String> queryParams = new TreeMap<>();
        queryParams.put("X-Amz-Date", amzDate);
        queryParams.put("X-Amz-Expires", String.valueOf(expiresInSeconds));

        String canonicalQueryString = queryParams.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .reduce((a, b) -> a + "&" + b)
                .orElse("");

        String canonicalRequest = method + "\n" +
                canonicalUri + "\n" +
                canonicalQueryString;

        String signature = Base64UrlEncoder.encode(signatureHelper.sign(canonicalRequest));

        return endpoint + canonicalUri + "?" + canonicalQueryString + "&X-Amz-Signature=" + signature;
    }

    public boolean verifySignature(HttpServletRequest request, String method, String key) throws Exception {
        String queryString = request.getQueryString();
        if (queryString == null) return true;

        Map<String, String> queryParams = Arrays.stream(queryString.split("&"))
                .map(str -> str.split("=", 2))
                .collect(Collectors.toMap(
                        s -> s[0],
                        s -> s.length > 1 ? URLDecoder.decode(s[1], StandardCharsets.UTF_8) : ""
                ));

        String signature = queryParams.get("X-Amz-Signature");
        String amzDate = queryParams.get("X-Amz-Date");
        String expire = queryParams.get("X-Amz-Expires");

        if (signature == null || amzDate == null || expire == null) return true;

        Instant signedTime = OffsetDateTime.parse(amzDate, DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmssX")).toInstant();
        Instant now = Instant.now();
        if (signedTime.plusSeconds(Long.parseLong(expire)).isBefore(now)) {
            return true;
        }


        String canonicalUri = "/" + key;
        Map<String, String> canonicalQuery = new TreeMap<>(queryParams);
        canonicalQuery.remove("X-Amz-Signature");

        String canonicalQueryString = canonicalQuery.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        String canonicalRequest = method + "\n" +
                canonicalUri + "\n" +
                canonicalQueryString;


        signatureHelper.verify(canonicalRequest, Base64UrlDecoder.decodeToBytes(signature));
        return false;
    }
}

