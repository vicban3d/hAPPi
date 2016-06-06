package utility;

import exceptions.CordovaRuntimeException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by victor on 11/11/2015.
 *
 *
 */
public class CordovaAppCompiler implements AppCompiler {

    @Override
    public void createApplication(JSONObject application) throws CordovaRuntimeException, JSONException {
        String name = application.getString("name");
        String username = application.getString("username");
        executeCommand("","create \"" + Strings.PATH_APPS + "\\" + username + "\\" + name + "\" com.happi.app \"" + name + "\"");
        //initializeFiles(Strings.PATH_APPS + "\\" + name);
    }

    @Override
    public void addPlatform(String applicationName, String appUsername, String platform) throws CordovaRuntimeException {
        Logger.DEBUG("Adding " + platform + " platform to application " + applicationName + "...");
        executeCommand(appUsername + "\\" + applicationName, "platform add " + platform);
        Logger.DEBUG("Added " + platform + " platform to application " + applicationName + ".");
    }

    @Override
    public void buildApplication(String applicationName, String appUsername) throws CordovaRuntimeException {
        Logger.DEBUG("Building application " + applicationName + "...");
        executeCommand(appUsername + "\\" + applicationName, "build");
        Logger.DEBUG("application " + applicationName + " built.");
    }

//    private void initializeFiles(String application) throws IOException {
//        Logger.DEBUG("Initializing application files in " + application + "...");
//        FileHandler.writeFile(application + "\\www\\js\\helper.js", JSCreator.JSFUNCTION_MAKE_STRUCT);
//        Logger.DEBUG("Initialized application files " + application + ".");
//    }


    private void executeCommand(String application, String command) throws CordovaRuntimeException {
        try {
            File dir = new File(Strings.PATH_APPS + "\\" + application);
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " " + command, null, dir);
            p.waitFor();
            if (p.exitValue() != 0){
                byte[] error = new byte[1024];
                //noinspection ResultOfMethodCallIgnored
                p.getErrorStream().read(error);
                throw new CordovaRuntimeException(new Exception(new String(error)));
            }
        } catch (InterruptedException | IOException e) {
            Logger.ERROR("Error adding android to application!", e);
        }
    }
}
