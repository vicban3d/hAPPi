package Logic;

/**
 * Created by victor on 11/10/2015.
 *
 */

import Exceptions.CordovaRuntimeException;
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
    void createApplication(String application) throws CordovaRuntimeException, JSONException, IOException;

    /**
     * Adds an android component to the given application.
     * @param application - the name of the application.
     */
    void addAndroidToApplication(String application) throws CordovaRuntimeException;

    /**
     * Adds an ios component to the given application.
     * @param application - the name of the application.
     */
    void addIOSToApplication(String application) throws CordovaRuntimeException;

    /**
     * Adds a Windows Phone component to the given application.
     * @param application - the name of the application.
     */
    void addWindowsPhoneToApplication(String application) throws CordovaRuntimeException;

    /**
     * Compiles and builds the application.
     * @param appId - the name of the application to build.
     */
    void buildApplication(String appId) throws CordovaRuntimeException, JSONException, IOException;

    /**
     * Creates a new object in the given application.
     * @param appId - the id of the application.
     * @param object - the JSON compatible parameters of the object.
     */
    void createObject(String appId, String object) throws JSONException;

    void removeObject(String appId, String data) throws JSONException;

    /**
     * Initiates a connection to the database.
     */
    void connectToDatabase() throws IOException;

    void clearDatabase();

    void removeApplication(String currentAppName);

    void updateApplication(String application) throws CordovaRuntimeException, IOException;

    void removePlatforms(String application);

    void createBehavior(String appId, String appName, String behavior) throws JSONException;

    void removeBehavior(String appId, String data);
}
