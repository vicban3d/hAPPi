package Utility;

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
            System.out.println("victorrrrrrrrr "+ name);
            Logger.logINFO("Creating Project " + name + "...");
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " create " + Strings.PATH_PROJECTS + "/" + name + " com.example.hello " + name);
            p.waitFor();
            Logger.logINFO("Created new project " + name + ".");
            initializeFiles(Strings.PATH_PROJECTS + "\\" + name);
        } catch (InterruptedException | IOException e) {
            Logger.logERROR("Error creating project!", e.getMessage());
        } catch (JSONException e) {
            Logger.logERROR("Error while handling JSON object!", e.getMessage());
        }
    }

    @Override
    public void addPlatform(JSONObject project, String platform) {
        String name;
        try {
            name = project.getString("name");
        } catch (JSONException e) {
            Logger.logERROR("Error while handling JSON object!", e.getMessage());
            return;
        }
        Logger.logINFO("Adding " + platform + " to project " + name + "...");
        executeCommand(name, "platform add " + platform);
        Logger.logINFO("Added " + platform + " to project " + name + ".");
    }

    @Override
    public void buildProject(JSONObject project) {
        String name;
        try {
            name = project.getString("name");
        } catch (JSONException e) {
            Logger.logERROR("Error while handling JSON object!", e.getMessage());
            return;
        }
        Logger.logINFO("Building project " + name + "...");
        executeCommand(name, "build");
        Logger.logINFO("Project " + name + " built.");
    }


    private void initializeFiles(String projectPath){
        Logger.logINFO("Initializing project files...");
        FileHandler.writeFile(projectPath + "/www/js/helper.js", JSCreator.JSFUNCTION_MAKE_STRUCT);

        Logger.logINFO("Initialized project files.");
    }


    private void executeCommand(String project, String command){
        try {
            File dir = new File(Strings.PATH_PROJECTS + "/" + project);
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " " + command, null, dir);
            p.waitFor();

        } catch (InterruptedException | IOException e) {
            Logger.logERROR("Error adding android to project!", e.getMessage());
        }
    }
}
