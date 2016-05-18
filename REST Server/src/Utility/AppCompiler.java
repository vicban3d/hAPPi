package Utility;

import Exceptions.CordovaRuntimeException;
import org.codehaus.jettison.json.JSONException;
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
     * Creates a new application and initializes relevant files.
     * @param name - the name of the created application.
     */
    void createApplication(JSONObject name) throws CordovaRuntimeException, JSONException;

    /**
     * Add an android module to the given application.
     * @param applicationName - the name of the application.
     * @param platform - the platform that should be added.
     */
    void addPlatform(String applicationName, String appUsername, String platform) throws CordovaRuntimeException;

    /**
     * Compiles the given application and creates a package file on all added modules.
     * @param applicationName - the name of the application to build.
     */
    void buildApplication(String applicationName, String appUsername) throws CordovaRuntimeException;

}
