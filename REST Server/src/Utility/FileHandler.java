package Utility;

import com.sun.grizzly.util.FileUtil;

import java.io.IOException;
import java.nio.file.*;
import java.io.File;
/**
 * Created by victor on 11/6/2015.
 *
 */

/**
 * A class which handles reading and writing of application files.
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
            Logger.ERROR("Failed to read from file " + path + "!", e.getMessage());
            return null;
        }
        return new String(encoded);
    }

    /**
     * Write the given content to a file at the given path.
     * @param path - the path of the relevant file.
     * @param content - the content which should be written to the file.
     */
    public static void writeFile(String path, String content) throws IOException {
        Path filePath = Paths.get(path);
        if (!Files.exists(filePath)){
            try {
                Files.createFile(filePath);
            } catch (IOException e) {
                Logger.ERROR("Failed to write to file " + path + "!", e.getMessage());
                throw e;
            }
        }
        try {
            Files.write(filePath, content.getBytes(), StandardOpenOption.APPEND);
        } catch (IOException e) {
            Logger.ERROR("Failed to write to file " + path + "!", e.getMessage());
            throw e;
        }
    }

    public static void clearFile(String path) throws IOException {
        Files.delete(Paths.get(path));
        Files.createFile(Paths.get(path));
    }

    public static void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if(files!=null) {
            //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();

    }

    public static void createFolder(File file) {
        file.mkdir();
    }

    public static void renameFolder(String oldName, String newName) throws IOException{
        File oldFile = new File(Strings.PATH_APPS + "\\" + oldName);
        File newFile = new File(Strings.PATH_APPS + "\\" + newName);
        if (newFile.exists() && !oldFile.equals(newFile))
            throw new java.io.IOException("file exists");
        oldFile.renameTo(newFile);
    }

    public static void copyFile(String source, String destination) throws IOException {
        Files.copy(Paths.get(source), Paths.get(destination), StandardCopyOption.REPLACE_EXISTING);
    }
}