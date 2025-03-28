package javatro.storage;

import javatro.Javatro;
import javatro.core.JavatroException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;


//Defined as a singleton class
public class Storage {
    /** Path to the task storage file. */
    private static final String SAVEFILE_LOCATION = "./savefile.dat";
    private static final String SECRET_KEY = "mySecretEncrypti";  //Use Github Environment Variable
    private static final String ALGORITHM = "AES";
    private static Storage instance; //Private static instance variable
    /** Indicates if the save file is valid and can be used. */
    private static boolean saveFileValid = true;


    private Storage() throws JavatroException {
        // Initialize resources
        initialiseTaskfile();
    }

    private void loadFile(String filePath) throws Exception {
        // Read the encrypted data from the file
        byte[] encryptedData = Files.readAllBytes(Path.of(filePath));

        // Decrypt the data
        String decryptedData = decrypt(encryptedData);

        // Convert the decrypted data back into a GameState object (you can use serialization or JSON for this)
        //return GameState.fromString(decryptedData);  // Assuming you have a method to convert the string back to a GameState object
    }


    private void initialiseTaskfile() throws JavatroException {
        Path path = Paths.get(SAVEFILE_LOCATION);

        // Check if the file exists
        if (Files.exists(path)) {
            System.out.println("File exists, reading and decrypting task file: " + SAVEFILE_LOCATION);
            try {
                // Read and decrypt the content if the file exists
                byte[] encryptedData = Files.readAllBytes(path);
                String decryptedData = decrypt(encryptedData);
                System.out.println("Decrypted data: " + decryptedData);  // Replace with actual task data processing
            } catch (IOException e) {
                throw new JavatroException("Error reading save file: " + e.getMessage());
            } catch (Exception e) {
                throw new JavatroException("Error decrypting save file: " + e.getMessage());
            }
        } else {
            try {
                // Create the file if it doesn't exist
                Files.createFile(path);
                System.out.println("Task File created at: " + SAVEFILE_LOCATION);
            } catch (IOException e) {
                throw new JavatroException("Save File could not be created, current session will not have saving features.");
            }
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
        String sampleData = "This is a sample task data.";  // Sample data to be saved in the file
        byte[] encryptedData;
        try {
            encryptedData = encrypt(sampleData);
        } catch (Exception ex) {
            throw new JavatroException(ex.getMessage());
        }
        try {
            Files.write(Paths.get(SAVEFILE_LOCATION), encryptedData);
        } catch (IOException e) {
            throw new JavatroException("SAVING ISSUE: " + e.getMessage());
        }
        System.out.println("Encrypted sample data saved successfully.");
    }





}
