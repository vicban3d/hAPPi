package logic;

/**
 * Created by victor on 11/10/2015.
 *
 */

import database.Database;
import exceptions.CordovaRuntimeException;
import com.dropbox.core.DbxException;
import exceptions.InvalidUserCredentialsException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.List;

/**
 * A facade interface which allows the connection layer to communicate with the business layer.
 */
public interface Facade {

    /**
     * Creates a new application with the given parameters.
     * @param application - a JSON compatible string describing the application.
     */
    void createApplication(Application application) throws CordovaRuntimeException, JSONException, IOException;

     /**
     * Compiles and builds the application.
      * @param appId - the name of the application to build.
      */
    String buildApplication(String appId, String username) throws CordovaRuntimeException, IOException, DbxException;

    /**
     * Creates a new object in the given application.
     * @param appId - the id of the application.
     * @param object - the JSON compatible parameters of the object.
     */
    void createObject(String appId, String username, ApplicationObject object);

    /**
     * Removes an object from the given application.
     * @param appId - the id of the application.
     * @param object - the JSON compatible parameters of the object.
     */
    void removeObject(String appId, String username, ApplicationObject object);

    /**
     * Initiates a connection to the database.
     */
    void connectToDatabase() throws IOException;

    void clearDatabase();

    void removeApplication(String appId, String username) throws DbxException, IOException;

    void updateApplication(Application application, String username) throws IOException, CordovaRuntimeException;

    void removePlatforms(String application, String username) throws IOException;

    void createBehavior(String appId, String username, ApplicationBehavior behavior);

    void removeBehavior(String appId, String username, ApplicationBehavior behavior);

    Database getDataBase();

    String getPage(String page) throws IOException;

    void updateApplicationObject(String appId, String username, ApplicationObject object);

    void updateApplicationBehavior(String appId, String username, ApplicationBehavior behavior);

    void addUser(User user) throws InvalidUserCredentialsException, IOException;

    byte[] getImageAsBytes(String resource);

    void createEvent(String id, String username, ApplicationEvent data);

    void removeEvent(String id, String username, ApplicationEvent data);

    void updateApplicationEvent(String id, String username, ApplicationEvent event);

    void addObjectInstance(JSONObject jsonObj);

    void removeObjectInstance(JSONObject jsonObject);

    AppInstance getObjectInstance(JSONObject jsonObject);

    List<Application> login(User user) throws InvalidUserCredentialsException;
}
