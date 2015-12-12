package Logic;

/**
 * Created by victor on 11/10/2015.
 *
 */

import Exceptions.CordovaRuntimeException;
import Exceptions.DatabaseConnectionErrorException;

/**
 * A facade interface which allows the connection layer to communicate with the business layer.
 */
public interface Facade {

    /**
     * Creates a new application with the given parameters.
     * @param application - a JSON compatible string describing the application.
     */
    void createApplication(String application) throws CordovaRuntimeException;

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
     * @param applicationName - the name of the application to build.
     */
    void buildApplication(String applicationName) throws CordovaRuntimeException;

    /**
     * Creates a new entity in the given application.
     * @param application - the name of the application.
     * @param entity - the JSON compatible parameters of the entity.
     */
    void createEntity(String application, String entity);

    void removeEntity(String application, String entity);

    /**
     * Initiates a connection to the database.
     */
    void connectToDatabase() throws DatabaseConnectionErrorException;

    void clearDatabase();
}
