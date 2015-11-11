package Util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
    public void addAndroidModule() {

    }

    @Override
    public void addIOSModule() {

    }

    @Override
    public void addWindowsPhoneModule() {

    }

    private void initializeFiles(String projectPath){
        Logger.logINFO("Initializing project files...");
        FileHandler.writeFile(projectPath + "/www/js/helper.js", JSCreator.JSFUNCTION_MAKE_STRUCT);

        Logger.logINFO("Initialized project files.");
    }
}
