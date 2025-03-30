package javatro.storage;

import javatro.core.Card;
import javatro.core.Deck;
import javatro.core.JavatroException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    public static int chosenRun = -1;

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
            StringBuilder runDataRawBuilder =
                    new StringBuilder(); // Use StringBuilder for efficient string concatenation
            for (String runAttribute : run) {
                runDataRawBuilder
                        .append(runAttribute)
                        .append(","); // Append the attribute and a comma
            }
            // Remove the last comma if run is not empty
            if (!runDataRawBuilder.isEmpty()) {
                runDataRawBuilder.setLength(
                        runDataRawBuilder.length() - 1); // Remove the trailing comma
            }
            runDataRawBuilder.append("\n"); // Add a newline after each run

            // Append the run data to decryptedDataRaw
            decryptedDataRaw += runDataRawBuilder.toString();
        }
        System.out.println("RAW: " + decryptedDataRaw);
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

        // saveSampleData(); // Add test sample data
        if (!decryptedDataRaw.isEmpty())
            parseDecryptedRawData(); // Convert decryptedDataRaw into runData (Basically initalise
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
                System.out.println("DATA: " + decryptedDataRaw);

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

    public static void addNewRun(List<String> newRun) {
        runData.add(newRun);
    }

    public void updateSaveFile() throws JavatroException {
        convertRunDataIntoRawData();

        byte[] encryptedData;
        try {
            encryptedData = encrypt(decryptedDataRaw);
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
            Files.write(
                    path,
                    encryptedData,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING);
            Files.write(
                    path,
                    dataHash.getBytes(),
                    StandardOpenOption.APPEND); // Append the hash after the data
        } catch (IOException e) {
            throw new JavatroException("SAVING ISSUE: " + e.getMessage());
        }
        System.out.println("Encrypted sample data saved successfully.");
    }

    // Method to save sample data into the task file (encrypted)
    public void saveSampleData() {
        // Round,Ante,Deck (3-10 are decks)
        decryptedDataRaw = "5,5,Checkered,2D,2S,3S,4S,5S,6S,7S,8S\n20,80,Deck 2\n";
        parseDecryptedRawData();
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

    public static Deck.DeckType createDeckFromString(String deckTypeString) {
        Deck.DeckType deckType;
        try {
            // Convert the string to the corresponding DeckType enum
            deckType = Deck.DeckType.valueOf(deckTypeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Handle the case where the string doesn't match a valid DeckType
            deckType = Deck.DeckType.DEFAULT; // Defaulting to "DEFAULT" Decktype
        }

        // Return a new Deck initialized with the valid DeckType
        return deckType;
    }

    public static Card parseCardString(String cardString) {
        // Ensure the string is not null or empty
        if (cardString == null || cardString.length() < 2) {
            throw new IllegalArgumentException("Invalid card string");
        }

        // Extract the rank and suit from the string
        String rankStr =
                cardString.substring(0, cardString.length() - 1); // All but the last character
        char suitChar = cardString.charAt(cardString.length() - 1); // Last character

        // Parse the rank
        Card.Rank rank =
                switch (rankStr) {
                    case "2" -> Card.Rank.TWO;
                    case "3" -> Card.Rank.THREE;
                    case "4" -> Card.Rank.FOUR;
                    case "5" -> Card.Rank.FIVE;
                    case "6" -> Card.Rank.SIX;
                    case "7" -> Card.Rank.SEVEN;
                    case "8" -> Card.Rank.EIGHT;
                    case "9" -> Card.Rank.NINE;
                    case "10" -> Card.Rank.TEN;
                    case "J" -> Card.Rank.JACK;
                    case "Q" -> Card.Rank.QUEEN;
                    case "K" -> Card.Rank.KING;
                    case "A" -> Card.Rank.ACE;
                    default -> throw new IllegalArgumentException("Invalid rank: " + rankStr);
                };
        //        try {
        //            // Attempt to convert the rank string to a Rank enum
        //            rank = Card.Rank.valueOf(rankStr.toUpperCase());
        //        } catch (IllegalArgumentException e) {
        //            // If rank is not valid, throw an exception or handle accordingly
        //            throw new IllegalArgumentException("Invalid rank: " + rankStr);
        //        }

        // Parse the suit
        Card.Suit suit =
                switch (Character.toUpperCase(suitChar)) {
                    case 'H' -> Card.Suit.HEARTS;
                    case 'C' -> Card.Suit.CLUBS;
                    case 'S' -> Card.Suit.SPADES;
                    case 'D' -> Card.Suit.DIAMONDS;
                    default -> throw new IllegalArgumentException("Invalid suit: " + suitChar);
                };

        // Return the constructed Card
        return new Card(rank, suit);
    }

    public static String cardToString(Card card) {
        // Get the rank and suit from the card
        String rankStr = card.rank().getSymbol(); // Get the symbol (e.g., "A", "K", "10")
        String suitStr =
                switch (card.suit()) {
                    case HEARTS -> "H";
                    case CLUBS -> "C";
                    case SPADES -> "S";
                    case DIAMONDS -> "D";
                };

        // Combine rank and suit to form the card string
        return rankStr + suitStr;
    }
}
