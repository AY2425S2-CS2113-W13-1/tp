package javatro.storage;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FileIntegrity {

    // Method to generate SHA-256 hash
    public static String generateSHA256Hash(String data) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hashBytes = digest.digest(data.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashBytes) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    // Verify that the hash matches the expected hash
    public static boolean verifyHash(String originalData, String expectedHash)
            throws NoSuchAlgorithmException {
        String generatedHash = generateSHA256Hash(originalData);
        return generatedHash.equals(expectedHash);
    }
}
