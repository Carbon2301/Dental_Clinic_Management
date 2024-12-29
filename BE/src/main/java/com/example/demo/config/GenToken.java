package com.example.demo.config;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.Date;


public class GenToken {
    private static final String SECRET_KEY = "dental-clinic-HKHAK"; // Khóa bí mật cho chữ ký
    private static final long EXPIRATION_TIME = 604800000L; // Thời gian hết hạn (7 ngày)

    // Hàm tạo JWT token với email và password
    public static String generateToken(String email, String password) {
        try {
            // Tạo phần Header (Base64)
            String header = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString("{\"alg\":\"HS256\",\"typ\":\"JWT\"}".getBytes(StandardCharsets.UTF_8));

            // Tạo hash cho password
            String passwordHash = hashPassword(password);

            // Tạo phần Payload (Base64)
            long expiration = new Date().getTime() + EXPIRATION_TIME;
            String payloadJson = String.format("{\"sub\":\"%s\",\"passwordHash\":\"%s\",\"exp\":%d}", email, passwordHash, expiration);
            String payload = Base64.getUrlEncoder().withoutPadding()
                    .encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));

            // Kết hợp header và payload
            String headerPayload = header + "." + payload;

            // Tạo chữ ký (Signature)
            String signature = generateHMAC(headerPayload, SECRET_KEY);

            // Kết hợp header, payload và signature để tạo JWT token
            return headerPayload + "." + signature;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo token", e);
        }
    }

    // Hàm tạo HMAC SHA-256 cho chữ ký
    private static String generateHMAC(String data, String secret) throws Exception {
        Mac hmacSHA256 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        hmacSHA256.init(secretKey);
        byte[] hash = hmacSHA256.doFinal(data.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }

    // Hàm hash password bằng SHA-256
    private static String hashPassword(String password) throws Exception {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash);
    }
}
