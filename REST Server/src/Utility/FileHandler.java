package Utility;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
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
            Logger.ERROR("Failed to read from file " + path + "!", e);
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
                Logger.ERROR("Failed to write to file " + path + "!", e);
                throw e;
            }
        }
        try {
            Files.write(filePath, content.getBytes());
        } catch (IOException e) {
            Logger.ERROR("Failed to write to file " + path + "!", e);
            throw e;
        }
    }

    /**
     * Clears the target file of all content.
     * @param path the path to the file that should be cleared.
     * @throws IOException
     */
    public static void clearFile(String path) throws IOException {
        Files.delete(Paths.get(path));
        Files.createFile(Paths.get(path));
    }

    /**
     * Deletes the target folder.
     * @param folderPath the path to the folder that should be deleted.
     */
    public static void deleteFolder(String folderPath) {
        File folder = new File(folderPath);
        File[] files = folder.listFiles();
        if(files!=null) {
            //some JVMs return null for empty dirs
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f.getAbsolutePath());
                } else {
                    f.delete();
                }
            }
        }
        folder.delete();
    }

    /**
     * Creates an empty folder from the given file.
     * @param folderPath the file handler to create a folder from.
     */
    public static void createFolder(String folderPath) {
        File folder = new File(folderPath);
        folder.mkdir();
    }

    /**
     * Renames the folder with the given name to a given new name.
     * @param oldName the name of the original folder.
     * @param newName the new name of the folder.
     * @throws IOException
     */
    public static void renameFolder(String oldName, String newName) throws IOException{
        File oldFile = new File(oldName);
        File newFile = new File(newName);
        if (newFile.exists() && !oldFile.equals(newFile))
            throw new java.io.IOException("file exists");
        oldFile.renameTo(newFile);
    }
}