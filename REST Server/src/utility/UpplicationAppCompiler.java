/**
 * Created by victor on 6/17/2016.
 */

package utility;

import com.upplication.cordova.Cordova;
import com.upplication.cordova.CordovaCLI;
import com.upplication.cordova.CordovaProject;
import com.upplication.cordova.Platform;
import exceptions.CordovaRuntimeException;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class UpplicationAppCompiler implements AppCompiler {

    private Cordova cordova;

    public UpplicationAppCompiler() {
        this.cordova  = new Cordova(Strings.PATH_CORDOVA, "");
    }

    @Override
    public void createApplication(JSONObject application) throws CordovaRuntimeException, JSONException, IOException {
        CordovaCLI cli = cordova.getCLI();
        String name = application.getString("name");
        String username = application.getString("username");
        if (!Files.exists(Paths.get(Strings.PATH_APPS + "\\" + username))){
            Files.createDirectory(Paths.get(Strings.PATH_APPS + "\\" + username));
        }
        cli.create(new File(Strings.PATH_APPS + "\\" + username + "\\" + name), "com." + name.replace(" ", "") + ".happi", name);
    }

    @Override
    public void addPlatform(String applicationName, String appUsername, String platform) throws CordovaRuntimeException {
        CordovaProject cordovaProject = cordova.getProject(new File(Strings.PATH_APPS + "/" + appUsername + "/" + applicationName));
        if (platform.equals("ios")) {
            cordovaProject.platform().add(Platform.IOs);
        } else {
            if (platform.equals("android")) {
                cordovaProject.platform().add(Platform.Android);
            } else {
                if (platform.equals("wp8")) {
                    CordovaAppCompiler comp = new CordovaAppCompiler();
                    comp.addPlatform(applicationName, appUsername, platform);
                } else {
                    throw new CordovaRuntimeException(new RuntimeException("Unsupported platform!"));
                }
            }
        }
    }

    @Override
    public void buildApplication(String applicationName, String appUsername) throws CordovaRuntimeException {
        CordovaProject cordovaProject = cordova.getProject(new File(Strings.PATH_APPS + "/" + appUsername + "/" + applicationName));
        cordovaProject.build();
    }
}
