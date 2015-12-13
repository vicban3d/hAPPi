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
    public void createApplication(JSONObject application) throws CordovaRuntimeException {
        try {
            String name = application.getString("name");
            Logger.INFO("Creating new application: " + name + "...");
            executeCommand("","create \"" + Strings.PATH_APPS + "\\" + name + "\" com.example.hello \"" + name + "\"");
            Logger.INFO("Created new application: " + name + ".");
            initializeFiles(Strings.PATH_APPS + "\\" + name);
        } catch (JSONException e) {
            Logger.ERROR("Error while handling JSON object!", e.getMessage());
        }
    }

    @Override
    public void addPlatform(String applicationName, String platform) throws CordovaRuntimeException {
        Logger.INFO("Adding " + platform + " platform to application " + applicationName + "...");
        executeCommand(applicationName, "platform add " + platform);
        Logger.INFO("Added " + platform + " platform to application " + applicationName + ".");
    }

    @Override
    public void buildApplication(String applicationName) throws CordovaRuntimeException {
        Logger.INFO("Building application " + applicationName + "...");
        executeCommand(applicationName, "build");
        Logger.INFO("application " + applicationName + " built.");
    }

    private void initializeFiles(String application){
        Logger.INFO("Initializing application files in " + application + "...");
        FileHandler.writeFile(application + "\\www\\js\\helper.js", JSCreator.JSFUNCTION_MAKE_STRUCT);
        Logger.INFO("Initialized application files " + application + ".");
    }


    private void executeCommand(String application, String command) throws CordovaRuntimeException {
        try {
            File dir = new File(Strings.PATH_APPS + "\\" + application);
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " " + command, null, dir);
            p.waitFor();
            if (p.exitValue() != 0){
                byte[] error = new byte[1024];
                //noinspection ResultOfMethodCallIgnored
                p.getErrorStream().read(error);
                throw new CordovaRuntimeException(new String(error));
            }
        } catch (InterruptedException | IOException e) {
            Logger.ERROR("Error adding android to application!", e.getMessage());
        }
    }
}
