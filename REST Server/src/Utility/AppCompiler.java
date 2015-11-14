package Utility;

import Exceptions.CordovaRuntimeException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by victor on 11/11/2015.
 *
 */

/**
 * Interface describing an application compiler such as Cordova which may be used to compile user applications.
 */
public interface AppCompiler {

    /**
     * Creates a new project and initializes relevant files.
     * @param name - the name of the created project.
     */
    void createProject(JSONObject name) throws CordovaRuntimeException;

    /**
     * Add an android module to the given project.
     * @param projectName - the name of the project.
     * @param platform - the platform that should be added.
     */
    void addPlatform(String projectName, String platform) throws CordovaRuntimeException;

    /**
     * Compiles the given project and creates a package file on all added modules.
     * @param projectName - the name of the project to build.
     */
    void buildProject(String projectName) throws CordovaRuntimeException;
}
