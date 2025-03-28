package javatro.storage;

import javatro.core.JavatroException;

import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

// Use this class to generate a key used for encryption and decryption.
public class KeyGeneration {

    public static void generateKey() throws JavatroException {

        // Create a KeyGenerator instance for AES encryption
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException n) {
            throw new JavatroException(n.getMessage());
        }

        // Initialize the KeyGenerator with a key size (128 bits)
        keyGenerator.init(128);

        // Generate the key
        SecretKey secretKey = keyGenerator.generateKey();

        // Print the key as a Base64 string (you can use it for storage)
        String encodedKey = java.util.Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println("Generated AES Key (Base64): " + encodedKey);
    }
}
