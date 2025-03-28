package javatro.storage;

import javatro.Javatro;
import javatro.core.JavatroException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

//Defined as a singleton class
public class Storage {
    /** Path to the task storage file. */
    private static final String SAVEFILE_LOCATION = "./savefile.csv";
    private static Storage instance; //Private static instance variable



    /** Indicates if the save file is valid and can be used. */
    private static boolean saveFileValid = true;

    private Storage() throws JavatroException {
        // Initialize resources
        initaliseTaskfile();
    }

    private void initaliseTaskfile() throws JavatroException {
        Path path = Paths.get(SAVEFILE_LOCATION);
        if (Files.exists(path)) {
            System.out.println("File exists, reading task file: " + SAVEFILE_LOCATION);
        } else {
            try {
                Files.createFile(path);
            }
            catch (IOException i) {
                //Save file cannot be created, inform user game will not have saving features
                throw new JavatroException("Save File could not be created, current session will not have saving features.");
            }
            System.out.println("Task File created at: " + SAVEFILE_LOCATION);
        }
    }

    public static Storage getInstance() throws JavatroException {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }


}
