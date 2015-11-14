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
     * Creates a new project with the given parameters.
     * @param project - a JSON compatible string describing the project.
     */
    void createProject(String project) throws CordovaRuntimeException;

    /**
     * Adds an android component to the given project.
     * @param project - the name of the project.
     */
    void addAndroidToProject(String project) throws CordovaRuntimeException;

    /**
     * Adds an ios component to the given project.
     * @param project - the name of the project.
     */
    void addIOSToProject(String project) throws CordovaRuntimeException;

    /**
     * Adds a Windows Phone component to the given project.
     * @param project - the name of the project.
     */
    void addWindowsPhoneToProject(String project) throws CordovaRuntimeException;

    /**
     * Compiles and builds the project.
     * @param projectName - the name of the project to build.
     */
    void buildProject(String projectName) throws CordovaRuntimeException;

    /**
     * Creates a new entity in the given project.
     * @param project - the name of the project.
     * @param entity - the JSON compatible parameters of the entity.
     */
    void createEntity(String project, String entity);

    /**
     * Initiates a connection to the database.
     */
    void connectToDatabase() throws DatabaseConnectionErrorException;
}
