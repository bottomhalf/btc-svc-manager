package bt.conference.service;

import bt.conference.entity.MeetingDetail;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Service
public class ManageMeetingService {
    private static final String PREFIX = "btc";

    // === AES Key (must be 16, 24, or 32 bytes) ===
    // In real app, load this from config/env
    private static final String SECRET = "MySecretKey12345";

    private static SecretKeySpec getKey() {
        return new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "AES");
    }

    // Encrypt
    private static String encrypt(String input) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, getKey());
            byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting", e);
        }
    }

    // Decrypt
    private static String decrypt(String encrypted) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, getKey());
            byte[] decoded = Base64.getUrlDecoder().decode(encrypted);
            return new String(cipher.doFinal(decoded), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting", e);
        }
    }

    // Generate token with your own values
    public static String generateToken(long userId, String customKey) {
        String userPart = encrypt(String.valueOf(userId));
        String keyPart = encrypt(customKey);
        return PREFIX + "-" + userPart + "-" + keyPart;
    }

    // Decode token back to values
    public static MeetingDetail decodeToken(String token) {
        String[] parts = token.split("-");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid token format");
        MeetingDetail meetingDetail = new MeetingDetail();
        meetingDetail.setOrganizedBy(Long.parseLong(decrypt(parts[1])));
        meetingDetail.setOrganizerName(decrypt(parts[2]));

        return meetingDetail;
    }
}