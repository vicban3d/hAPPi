package Logic;

/**
 * Created by victor on 11/10/2015.
 *
 */

import Database.Database;
import Exceptions.CordovaRuntimeException;
import com.dropbox.core.DbxException;
import org.codehaus.jettison.json.JSONException;

import java.io.IOException;

/**
 * A facade interface which allows the connection layer to communicate with the business layer.
 */
public interface Facade {

    /**
     * Creates a new application with the given parameters.
     * @param application - a JSON compatible string describing the application.
     */
    void createApplication(Application application) throws CordovaRuntimeException, JSONException;

     /**
     * Compiles and builds the application.
      * @param appId - the name of the application to build.
      */
    String buildApplication(String appId, String user) throws CordovaRuntimeException, IOException, DbxException;

    /**
     * Creates a new object in the given application.
     * @param appId - the id of the application.
     * @param object - the JSON compatible parameters of the object.
     */
    void createObject(String appId, String user, ApplicationObject object);

    /**
     * Removes an object from the given application.
     * @param appId - the id of the application.
     * @param object - the JSON compatible parameters of the object.
     */
    void removeObject(String appId, String user, ApplicationObject object);

    /**
     * Initiates a connection to the database.
     */
    void connectToDatabase() throws IOException;

    void clearDatabase();

    void removeApplication(String appId, String user);

    void updateApplication(Application application) throws IOException, CordovaRuntimeException;

    void removePlatforms(String application);

    void createBehavior(String appId, String user, ApplicationBehavior behavior);

    void removeBehavior(String appId, String user, ApplicationBehavior behavior);

    Database getDataBase();

    String getPage(String page);

    void updateApplicationObject(String appId, String user, ApplicationObject object);

    void updateApplicationBehavior(String appId, String user, ApplicationBehavior behavior);

    void addUser(User user);
}
