package Utility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by victor on 11/6/2015.
 *
 */

/**
 * A class which handles reading and writing of project files.
 */
public class FileHandler {

    /**
     * Reads from a file in the given path.
     * @param path - the path to read from.
     * @return - A string containing the content of the read file.
     */
    public static String readFile(String path)
    {
        byte[] encoded;
        try {
            encoded = Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            Logger.logERROR("Failed to read from file " + path + "!", e.getMessage());
            return null;
        }
        return new String(encoded);
    }

    /**
     * Write the given content to a file at the given path.
     * @param path - the path of the relevant file.
     * @param content - the content which should be written to the file.
     */
    public static void writeFile(String path, String content){
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)){
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                Logger.logERROR("Failed to write to file " + path + "!", e.getMessage());
                return;
            }
        }
        try {
            Files.write(filePath, content.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Logger.logERROR("Failed to write to file " + path + "!", e.getMessage());
        }
    }
}
