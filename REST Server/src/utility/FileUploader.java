package utility;

import com.dropbox.core.DbxException;

import java.io.IOException;

/**
 * Created by victor on 1/21/2016.
 */
public interface FileUploader {
    /**
     * Upload the file at the given path to the storage account.
     * @param username the username who owns the file
     * @param targetName the name of the file as should appear inside the storage.
     * @param path the path to the uploaded file.
     * @return a web link to the file in the storage.
     * @throws DbxException
     * @throws IOException
     */
    String uploadFile(String username, String targetName, String path) throws DbxException, IOException;


    /**
     * Removes the given file from the application storage
     * @param username the username who owns the file
     * @param fileName the name of the file
     * @throws DbxException
     */
    void deleteFile(String username, String fileName) throws DbxException;
}
