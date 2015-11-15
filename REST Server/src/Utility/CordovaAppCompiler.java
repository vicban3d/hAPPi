package Utility;

import Exceptions.CordovaRuntimeException;
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

    @Override
    public void createProject(JSONObject project) throws CordovaRuntimeException {
        try {
            String name = project.getString("name");
            Logger.INFO("Creating new project: " + name + "...");
            executeCommand("","create \"" + Strings.PATH_PROJECTS + "\\" + name + "\" com.example.hello \"" + name + "\"");
            Logger.INFO("Created new project: " + name + ".");
            initializeFiles(Strings.PATH_PROJECTS + "\\" + name);
        } catch (JSONException e) {
            Logger.ERROR("Error while handling JSON object!", e.getMessage());
        }
    }

    @Override
    public void addPlatform(String projectName, String platform) throws CordovaRuntimeException {
        Logger.INFO("Adding " + platform + " platform to project " + projectName + "...");
        executeCommand(projectName, "platform add " + platform);
        Logger.INFO("Added " + platform + " platform to project " + projectName + ".");
    }

    @Override
    public void buildProject(String projectName) throws CordovaRuntimeException {
        Logger.INFO("Building project " + projectName + "...");
        executeCommand(projectName, "build");
        Logger.INFO("Project " + projectName + " built.");
    }


    private void initializeFiles(String projectPath){
        Logger.INFO("Initializing files in " + projectPath + "...");
        FileHandler.writeFile(projectPath + "\\www\\js\\helper.js", JSCreator.JSFUNCTION_MAKE_STRUCT);
        Logger.INFO("Initialized project files " + projectPath + ".");
    }


    private void executeCommand(String project, String command) throws CordovaRuntimeException {
        try {
            File dir = new File(Strings.PATH_PROJECTS + "\\" + project);
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " " + command, null, dir);
            p.waitFor();
            if (p.exitValue() != 0){
                byte[] error = new byte[1024];
                //noinspection ResultOfMethodCallIgnored
                p.getErrorStream().read(error);
                throw new CordovaRuntimeException(new String(error));
            }
        } catch (InterruptedException | IOException e) {
            Logger.ERROR("Error adding android to project!", e.getMessage());
        }
    }
}
