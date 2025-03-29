package javatro.storage;

import javatro.core.JavatroException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.*;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

// Defined as a singleton class
public class Storage implements PropertyChangeListener {
    /** Path to the task storage file. */
    private static final String SAVEFILE_LOCATION = "./savefile.dat";

    private static final String SECRET_KEY = "mySecretEncrypti"; // Use Github Environment Variable
    private static final String ALGORITHM = "AES";
    private static Storage instance; // Private static instance variable
    /** Indicates if the save file is valid and can be used. */
    private static boolean saveFileValid = true;

    private static final Path saveFilePath = Paths.get(SAVEFILE_LOCATION);

    private static String decryptedDataRaw = "";

    private static List<List<String>> runData = new ArrayList<>();

    private void parseDecryptedRawData() {
        // Each row is 1 run
        // Columns: Round, Ante, Deck

        // Each row is separated by "\n"
        // Each column is separated by commas

        String[] runs = decryptedDataRaw.split("\n");

        for (String run : runs) {
            String[] currentRunInfo = run.split(",");
            runData.add(List.of(currentRunInfo));
        }
    }

    private void convertRunDataIntoRawData() {
        decryptedDataRaw = "";
        for (List<String> run : runData) {
            String runDataRaw = "";
            for (String runAttribute : run) {
                runDataRaw = runAttribute + ",";
            }
            // Remove last ","
            runDataRaw = runDataRaw.substring(0, runDataRaw.length() - 1);
            runDataRaw = runDataRaw + "\n";
            decryptedDataRaw = decryptedDataRaw + runDataRaw;
        }
    }

    private Storage() {
        // Initialize resources
        try {
            initialiseTaskfile();
        } catch (JavatroException javatroException) {
            // Means either the task file could not be created or task file was present, but
            // corrupted
            if (saveFileValid) {
                // Delete existing savefile and replace with new empty save file
                try {
                    Files.deleteIfExists(saveFilePath);
                } catch (IOException e) {
                    // There is no file to delete/ failed to delete
                    saveFileValid = false;
                }
            }
        }

        saveSampleData(); // Add test sample data
        // parseDecryptedRawData(); // Convert decryptedDataRaw into runData (Basically initalise
        // runData here)
    }

    private void createTaskFile() throws JavatroException {
        try {
            // Create the file if it doesn't exist
            Files.createFile(saveFilePath);
        } catch (IOException e) {
            saveFileValid = false;
            throw new JavatroException(
                    "Save File could not be created, current session will not have saving"
                            + " features.");
        }
    }

    private void initialiseTaskfile() throws JavatroException {

        // Check if the file exists
        if (Files.exists(saveFilePath)) {
            try {
                // Read and decrypt the content if the file exists
                byte[] fileData = Files.readAllBytes(saveFilePath);

                if (fileData.length < 64) return; // Empty file, no need to decrypt

                byte[] encryptedData =
                        Arrays.copyOfRange(
                                fileData, 0, fileData.length - 64); // SHA-256 hash is 64 bytes

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

                decryptedDataRaw = decrypt(encryptedData);

            } catch (Exception e) {
                throw new JavatroException("Error reading/decrypting save file: " + e.getMessage());
            }
        } else {
            createTaskFile();
        }
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    // Encrypt a String and return the encrypted bytes
    public byte[] encrypt(String data) throws JavatroException {
        SecretKeySpec keySpec = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        Cipher cipher = null;
        try {
            cipher = Cipher.getInstance(ALGORITHM);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new JavatroException(e.getMessage());
        }

        try {
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        } catch (InvalidKeyException e) {
            throw new JavatroException(e.getMessage());
        }

        try {
            return cipher.doFinal(data.getBytes());
        } catch (IllegalBlockSizeException | BadPaddingException e) {
            throw new JavatroException(e.getMessage());
        }
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
    public void saveSampleData() {
        decryptedDataRaw = "5,10,Deck 1\n20,80,Deck 2\n";
        parseDecryptedRawData();
        //        String sampleData = "This is a sample task data."; // Sample data to be saved in
        // the file
        //        byte[] encryptedData;
        //        try {
        //            encryptedData = encrypt(sampleData);
        //        } catch (Exception ex) {
        //            throw new JavatroException(ex.getMessage());
        //        }
        //
        //        // Generate a hash of the encrypted data
        //        String dataHash = null;
        //        try {
        //            dataHash = FileIntegrity.generateSHA256Hash(new String(encryptedData));
        //        } catch (NoSuchAlgorithmException e) {
        //            throw new JavatroException(e.getMessage());
        //        }
        //
        //        try {
        //            Path path = Paths.get(SAVEFILE_LOCATION);
        //            Files.write(path, encryptedData, StandardOpenOption.CREATE,
        // StandardOpenOption.APPEND);
        //            Files.write(
        //                    path,
        //                    dataHash.getBytes(),
        //                    StandardOpenOption.APPEND); // Append the hash after the data
        //        } catch (IOException e) {
        //            throw new JavatroException("SAVING ISSUE: " + e.getMessage());
        //        }
        //        System.out.println("Encrypted sample data saved successfully.");
    }

    public void setDecryptedDataRaw(String decryptedDataRaw) {
        Storage.decryptedDataRaw = decryptedDataRaw;
    }

    public List<List<String>> getRunData() {
        return runData;
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

        //        String propertyName = evt.getPropertyName();
        //        Object newValue = evt.getNewValue();
        //
        //        // Map for property handlers
        //        Map<String, Consumer<Object>> propertyHandlers = new HashMap<>();
        //
        //        propertyHandlers.put("roundName", value -> roundName = value.toString());
        //        propertyHandlers.put("remainingPlays", value -> handsLeft = (Integer) value);
        //        propertyHandlers.put("remainingDiscards", value -> discardsLeft = (Integer)
        // value);
        //        propertyHandlers.put("currentScore", value -> roundScore = (Integer) value);
        //        propertyHandlers.put("roundDescription", value -> roundDescription =
        // value.toString());
        //        propertyHandlers.put("blindScore", value -> blindScore = (Integer) value);
        //        propertyHandlers.put(
        //                "holdingHand",
        //                value -> {
        //                    List<?> list = (List<?>) value;
        //                    holdingHand =
        //                            list.stream()
        //                                    .filter(
        //                                            Card.class
        //                                                    ::isInstance) // Ensures only Card
        // instances are
        //                                    // collected
        //                                    .map(Card.class::cast) // Safely cast to Card
        //                                    .collect(Collectors.toList());
        //                });
        //        propertyHandlers.put(
        //                "roundComplete",
        //                value -> {
        //                    roundOver = (Integer) value;
        //                    if (roundOver != 0) {
        //                        commandMap.clear();
        //                        commandMap.add(new MainMenuOption());
        //                        commandMap.add(new ExitGameOption());
        //                    }
        //                });
        //
        //        // Execute the appropriate handler if it exists and update its value
        //        propertyHandlers.getOrDefault(propertyName, v -> {}).accept(newValue);
    }
}
