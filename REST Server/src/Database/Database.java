package Database;

import Logic.AppInstance;
import Logic.Application;
import Logic.User;
import com.mongodb.client.MongoDatabase;

import java.io.IOException;
import java.util.List;

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

    ////////////////// Applications /////////////////


    /**
     * Removes the document with the given ID from the database.
     * @param appID the id of the collection to remove.
     */
    void removeApplication(String appID);

    /**
     * Updates the document with the given ID in the database.
     * @param document the new document.
     */
    void updateApplication(Application document);


    /**
     * Adds a new document to the database.
     * @param document the document to add.
     */
    void addApplication(Application document);


    Application getApplication(String id);


    /////////////////////// Users //////////////////

    void addUser(User document);

    User getUser(String username);

    boolean isUserExist(String username);

    boolean isPasswordRight(String username, String pass);

    public List<Application> getApplicationOfUser(String username);

    //////////////////////// app instances /////////////////

    void addApplicationInstance(AppInstance appInstance);

    AppInstance getAppInstance(String id);

    boolean isInstanceExist(String instanceId);

    void updateAppInstance(AppInstance instance);


    ///////////////////   get database //////////////////////

    MongoDatabase getDB();
}
