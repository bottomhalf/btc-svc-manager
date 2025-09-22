package bt.conference.service;

import bt.conference.entity.MeetingDetail;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class ManageMeetingService {
    private static final String PREFIX = "btc";
    private static final String SECRET = "MySecretKey12345"; // 16 bytes key (AES-128)

    private static SecretKeySpec getKey() {
        return new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "AES");
    }

    // Generate random IV
    private static byte[] generateIV() {
        byte[] iv = new byte[16]; // AES block size
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    // Encrypt with random IV
    private static String encrypt(String input) {
        try {
            byte[] iv = generateIV();
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, getKey(), new IvParameterSpec(iv));

            byte[] encrypted = cipher.doFinal(input.getBytes(StandardCharsets.UTF_8));

            // Store IV + Ciphertext together (IV first)
            byte[] combined = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, combined, 0, iv.length);
            System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);

            return Base64.getUrlEncoder().withoutPadding().encodeToString(combined);
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting", e);
        }
    }

    // Decrypt (extract IV + ciphertext)
    private static String decrypt(String encrypted) {
        try {
            byte[] decoded = Base64.getUrlDecoder().decode(encrypted);

            byte[] iv = new byte[16];
            byte[] ciphertext = new byte[decoded.length - 16];
            System.arraycopy(decoded, 0, iv, 0, 16);
            System.arraycopy(decoded, 16, ciphertext, 0, ciphertext.length);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, getKey(), new IvParameterSpec(iv));

            return new String(cipher.doFinal(ciphertext), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Error decrypting", e);
        }
    }

    // Generate token
    public static String generateToken(long userId, String customKey) {
        String userPart = encrypt(String.valueOf(userId));
        String keyPart = encrypt(customKey);
        return PREFIX + "-" + userPart + "-" + keyPart;
    }

    // Decode token
    public static MeetingDetail decodeToken(String token) {
        String[] parts = token.split("-");
        if (parts.length != 3) throw new IllegalArgumentException("Invalid token format");

        MeetingDetail meetingDetail = new MeetingDetail();
        meetingDetail.setOrganizedBy(Long.parseLong(decrypt(parts[1])));
        meetingDetail.setOrganizerName(decrypt(parts[2]));
        return meetingDetail;
    }
}