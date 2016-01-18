package Logic;

import Database.Database;
import Database.MongoDB;
import Exceptions.CordovaRuntimeException;
import Utility.*;
import org.bson.Document;
import org.codehaus.jettison.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by victor on 11/10/2015.
 *
 */
public class hAPPiFacade implements Facade {

    private final Database database;
    private final AppCompiler compiler;

    /**
     * Constructor and initiator for the Facade.
     */
    public  hAPPiFacade(){
        database = new MongoDB();
        compiler = new CordovaAppCompiler();
    }

    @Override
    public void createApplication(Application application) throws CordovaRuntimeException, JSONException {
            database.addData(application);
            compiler.createApplication(application.toJSON());
            createPlatforms(application.getPlatforms(), application.getName());
    }

    @Override
    public void updateApplication(Application application) throws IOException, CordovaRuntimeException {
        Application oldApp = database.getData(application.getId());
        String oldApplicationName = oldApp.getName();
        removePlatforms(oldApplicationName);
        FileHandler.renameFolder(oldApplicationName, application.getName());
        createPlatforms(application.getPlatforms(), application.getName());
        database.updateData(application);
    }

    private void createPlatforms(ArrayList<String> platforms, String applicationName) throws CordovaRuntimeException {
        if(platforms == null || platforms.isEmpty()) {
            return;
        }
        for (String platform : platforms){
            compiler.addPlatform(applicationName, platform);
        }
    }

    @Override
    public void buildApplication(String appId) throws CordovaRuntimeException, IOException {
        Application application = database.getData(appId);
        prepareApplicationForCompilation(application);
        compiler.buildApplication(application.getName());
        FileHandler.copyFile(Strings.PATH_APPS + "/" + application.getName() + "/platforms/android/build/outputs/apk/android-debug.apk", Strings.PATH_WEB_CONTENT + "/");
    }

    private void prepareApplicationForCompilation(Application application) throws IOException {
        String name = application.getName();
        ArrayList<ApplicationObject> objects = application.getObjects();
        ArrayList<ApplicationBehavior> behaviors = application.getBehaviors();
        ArrayList<String> allActions = new ArrayList<>();
        for (Document obj : objects){
            allActions.add(obj.toJson());
        }
        ArrayList<String> allBehaviors = new ArrayList<>();
        for (Document beh : behaviors){
            allBehaviors.add(beh.toJson());
        }

        FileHandler.clearFile(Strings.PATH_APPS + "\\" + name + "\\www\\index.html");
        FileHandler.clearFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\index.js");
        FileHandler.clearFile(Strings.PATH_APPS + "\\" + name + "\\www\\css\\index.css");
        String HTMLContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\index.html");
        String MAINContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\main.html");
        String JSContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\index.js");
        String CSSContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\model\\index.css");
        String ANGULARContent = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\lib\\angular.min.js");
        if (HTMLContent != null && JSContent != null && CSSContent != null) {
            JSContent = JSContent.replace("<[OBJECTS]>", allActions.toString().replace("\"", "\\\""));
            JSContent = JSContent.replace("<[BEHAVIORS]>", allBehaviors.toString().replace("\"", "\\\""));
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\index.html", HTMLContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\main.html", MAINContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\index.js", JSContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\js\\angular.min.js", ANGULARContent);
            FileHandler.writeFile(Strings.PATH_APPS + "\\" + name + "\\www\\css\\index.css", CSSContent);
        }
    }

    @Override
    public void createObject(String appId, ApplicationObject data) {
        Application application = database.getData(appId);
        application.addObject(data);
        database.updateData(application);
    }

    @Override
    public void removeObject(String appId, ApplicationObject object) {
        Application application = database.getData(appId);
        application.removeObject(object);
        database.updateData(application);
    }

    @Override
    public void connectToDatabase() throws IOException {
        database.connect();
    }

    @Override
    public void clearDatabase() {
        database.clearAll();
    }

    @Override
    public void removeApplication(String appId) {
        String appName = database.getData(appId).getName();
        database.removeData(appId);
        File file = new File(Strings.PATH_APPS + "\\" + appName);
        FileHandler.deleteFolder(file);
        Logger.INFO("The application " + appName + " was deleted.");
    }

    @Override
    public void removePlatforms(String fileName){
        File file = new File(Strings.PATH_APPS + "\\" + fileName+ "\\platforms");
        FileHandler.deleteFolder(file);
        FileHandler.createFolder(file);
    }

    @Override
    public void createBehavior(String appId, ApplicationBehavior behavior) {
        Application application = database.getData(appId);
        application.addBehavior(behavior);
        database.updateData(application);
    }

    @Override
    public void removeBehavior(String appId, ApplicationBehavior behavior) {
        Application application = database.getData(appId);
        application.removeBehavior(behavior);
        database.updateData(application);
    }

    @Override
    public Database getDataBase(){
        return database;
    }
}
