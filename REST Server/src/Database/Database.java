package Database;

import Logic.ApplicationObject;
import Logic.User;
import org.bson.Document;

import java.io.IOException;

/**
 * Created by victor on 11/12/2015.
 *
 */
public interface Database {

    /**
     * Connects to Mongo database.
     */
    void connect() throws IOException;

    /**
     * Clears the whole database.
     * WARNING! DO NOT USE LIGHTLY!
     */
    void clearAll();

    /**
     * Removes the document with the given ID from the database.
     * @param documentID the id of the collection to remove.
     */
    void removeData(String documentID, String username);

    /**
     * Updates the document with the given ID in the database.
     * @param document the new document.
     */
    void updateData(Document document);

    /**
     * Retrieves a document with the given ID from the database.
     * @param documentId the ID of the document to retrieve.
     * @return the requested document.
     */
    Document getData(String documentId, String username);

    /**
     * Adds a new document to the database.
     * @param document the document to add.
     */
    void addData(Document document);

    void addUser(Document document);

    User getUser(String username);
}
