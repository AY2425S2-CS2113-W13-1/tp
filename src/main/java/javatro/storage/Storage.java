package javatro.storage;

import javatro.core.JavatroException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

// Defined as a singleton class
public class Storage {
    /** Path to the task storage file. */
    private static final String SAVEFILE_LOCATION = "./savefile.dat";

    private static final String SECRET_KEY = "mySecretEncrypti"; // Use Github Environment Variable
    private static final String ALGORITHM = "AES";
    private static Storage instance; // Private static instance variable
    /** Indicates if the save file is valid and can be used. */
    private static boolean saveFileValid = true;

    private static final Path path = Paths.get(SAVEFILE_LOCATION);

    private Storage() throws JavatroException {
        // Initialize resources
        initialiseTaskfile();
    }

    private void createTaskFile() throws JavatroException {
        try {
            // Create the file if it doesn't exist
            Files.createFile(path);
            System.out.println("Task File created at: " + SAVEFILE_LOCATION);
        } catch (IOException e) {
            saveFileValid = false;
            throw new JavatroException(
                    "Save File could not be created, current session will not have saving"
                        + " features.");
        }
    }

    private void initialiseTaskfile() throws JavatroException {

        Path path = Paths.get(SAVEFILE_LOCATION);

        // Check if the file exists
        if (Files.exists(path)) {
            System.out.println(
                    "File exists, reading and decrypting task file: " + SAVEFILE_LOCATION);
            try {
                // Read and decrypt the content if the file exists
                byte[] fileData = Files.readAllBytes(path);

                if (fileData.length < 64) return;

                byte[] encryptedData =
                        Arrays.copyOfRange(
                                fileData,
                                0,
                                fileData.length - 64); // Assuming SHA-256 hash is 64 bytes
                byte[] savedHash =
                        Arrays.copyOfRange(fileData, fileData.length - 64, fileData.length);

                String savedHashString = new String(savedHash);
                // Verify the integrity of the data
                boolean isDataValid =
                        FileIntegrity.verifyHash(new String(encryptedData), savedHashString);
                if (!isDataValid) {
                    throw new JavatroException(
                            "Data integrity check failed: file has been tampered with.");
                }

                String decryptedData = decrypt(encryptedData);
                System.out.println(
                        "Decrypted data: "
                                + decryptedData); // Replace with actual task data processing

            } catch (Exception e) {
                throw new JavatroException("Error reading/decrypting save file: " + e.getMessage());
            }
        } else {
            createTaskFile();
        }
    }

    public static Storage getInstance() throws JavatroException {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    // Encrypt a String and return the encrypted bytes
    public byte[] encrypt(String data) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        return cipher.doFinal(data.getBytes());
    }

    // Decrypt the byte array and return the decrypted String
    public String decrypt(byte[] encryptedData) throws JavatroException {
        if (encryptedData.length == 0) {
            return "";
        }

        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new JavatroException("Cannot Get Algorithm");
        }

        try {
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
        } catch (InvalidKeyException e) {
            throw new JavatroException("Cannot initalise: " + e.getMessage());
        }

        byte[] decryptedBytes = null;
        try {
            decryptedBytes = cipher.doFinal(encryptedData);
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new RuntimeException("Decryption Failed");
        }

        return new String(decryptedBytes);
    }

    // Method to save sample data into the task file (encrypted)
    public void saveSampleData() throws JavatroException {
        String sampleData = "This is a sample task data."; // Sample data to be saved in the file
        byte[] encryptedData;
        try {
            encryptedData = encrypt(sampleData);
        } catch (Exception ex) {
            throw new JavatroException(ex.getMessage());
        }

        // Generate a hash of the encrypted data
        String dataHash = null;
        try {
            dataHash = FileIntegrity.generateSHA256Hash(new String(encryptedData));
        } catch (NoSuchAlgorithmException e) {
            throw new JavatroException(e.getMessage());
        }

        try {
            Path path = Paths.get(SAVEFILE_LOCATION);
            Files.write(path, encryptedData, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            Files.write(
                    path,
                    dataHash.getBytes(),
                    StandardOpenOption.APPEND); // Append the hash after the data
        } catch (IOException e) {
            throw new JavatroException("SAVING ISSUE: " + e.getMessage());
        }
        System.out.println("Encrypted sample data saved successfully.");
    }
}
