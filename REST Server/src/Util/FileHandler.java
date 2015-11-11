package Util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

/**
 * Created by victor on 11/6/2015.
 *
 */
public class FileHandler {

    public static String readFile(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded);
    }

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
