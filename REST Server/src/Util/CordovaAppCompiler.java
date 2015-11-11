package Util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.IOException;

/**
 * Created by victor on 11/11/2015.
 *
 *
 */
public class CordovaAppCompiler implements AppCompiler {


    public CordovaAppCompiler() {
    }

    @Override
    public void createProject(JSONObject project) {
        try {
            String name = project.getString("name");
            Logger.logINFO("Creating Project " + name + "...");
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " create " + Strings.PATH_PROJECTS + "/" + name + " com.example.hello " + name);
            p.waitFor();
            Logger.logINFO("Created new project " + name + ".");
            initializeFiles(Strings.PATH_PROJECTS + "/" + name);
        } catch (InterruptedException | IOException e) {
            Logger.logERROR("Error creating project!", e.getMessage());
        } catch (JSONException e) {
            Logger.logERROR("Error while handling JSON object!", e.getMessage());
        }
    }

    @Override
    public void addAndroidModule(JSONObject project) {
        try {
            String name = project.getString("name");
            Logger.logINFO("Adding android to Project " + name + "...");
            File dir = new File(Strings.PATH_PROJECTS + "/" + name);
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " platform add android", null, dir);
            p.waitFor();
            Logger.logINFO("Added android to project " + name + ".");
        } catch (InterruptedException | IOException e) {
            Logger.logERROR("Error adding android to project!", e.getMessage());
        } catch (JSONException e) {
            Logger.logERROR("Error while handling JSON object!", e.getMessage());
        }
    }

    @Override
    public void addIOSModule(JSONObject project) {

    }

    @Override
    public void addWindowsPhoneModule(JSONObject project) {

    }

    @Override
    public void buildProject(JSONObject project) {
        try {
            String name = project.getString("name");
            Logger.logINFO("Building project " + name + "...");
            File dir = new File(Strings.PATH_PROJECTS + "/" + name);
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " build", null, dir);
            p.waitFor();
            Logger.logINFO("Project " + name + " built.");
        } catch (InterruptedException | IOException e) {
            Logger.logERROR("Error adding android to project!", e.getMessage());
        } catch (JSONException e) {
            Logger.logERROR("Error while handling JSON object!", e.getMessage());
        }
    }


    private void initializeFiles(String projectPath){
        Logger.logINFO("Initializing project files...");
        FileHandler.writeFile(projectPath + "/www/js/helper.js", JSCreator.JSFUNCTION_MAKE_STRUCT);

        Logger.logINFO("Initialized project files.");
    }
}
