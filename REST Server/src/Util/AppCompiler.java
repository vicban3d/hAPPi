package Util;

import org.codehaus.jettison.json.JSONObject;

/**
 * Created by victor on 11/11/2015.
 */

/**
 * Interface describing an application compiler such as Cordova which may be used to compile user applications.
 */
public interface AppCompiler {

    /**
     * Creates a new project and initializes relevant files.
     * @param name - the name of the created project.
     */
    void createProject(JSONObject name);

    /**
     * Add an android module to the given project.
     * @param project - the name of the project.
     */
    void addAndroidModule(JSONObject project);

    /**
     * Add an ios module to the given project.
     * @param project - the name of the project.
     */
    void addIOSModule(JSONObject project);

    /**
     * Add a windows phone module to the given project.
     * @param project - the name of the project.
     */
    void addWindowsPhoneModule(JSONObject project);

    /**
     * Compiles the given project and creates a package file on all added modules.
     * @param project - the project to compile.
     */
    void buildProject(JSONObject project);
}
