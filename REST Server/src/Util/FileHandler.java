package Util;

import java.io.IOException;
import java.nio.file.Files;
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

    public static void writeFile(String path, String content, StandardOpenOption option) throws IOException {
        Files.write(Paths.get(path), content.getBytes(), option);
    }
}
