package Logic;

import Database.Database;
import Database.MongoDB;
import Exceptions.CordovaRuntimeException;
import Utility.*;
import com.dropbox.core.DbxException;
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
    private final FileUploader fileUploader;

    /**
     * Constructor and initiator for the Facade.
     */
    public  hAPPiFacade(){
        database = new MongoDB();
        compiler = new CordovaAppCompiler();
        fileUploader = new DropboxAPI();
    }

    @Override
    public void createApplication(Application application) throws CordovaRuntimeException, JSONException {
            database.addData(application);
            compiler.createApplication(application.toJSON());
            createPlatforms(application.getPlatforms(), application.getName());
    }

    @Override
    public void updateApplication(Application application) throws IOException, CordovaRuntimeException {
        Application oldApp = Application.fromDocument(database.getData(application.getId()));
        String oldApplicationName = oldApp.getName();
        removePlatforms(oldApplicationName);
        FileHandler.renameFolder(Strings.PATH_APPS + "\\" + oldApplicationName, Strings.PATH_APPS + "\\" + application.getName());
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
    public String buildApplication(String appId) throws CordovaRuntimeException, IOException, DbxException {
        Application application = Application.fromDocument(database.getData(appId));
        prepareApplicationForCompilation(application);
        compiler.buildApplication(application.getName());
        return fileUploader.uploadFile(application.getName(), Strings.PATH_APPS + "/" + application.getName() + "/platforms/android/build/outputs/apk/android-debug.apk");

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
        Application application = Application.fromDocument(database.getData(appId));
        application.addObject(data);
        database.updateData(application);
    }

    @Override
    public void removeObject(String appId, ApplicationObject object) {
        Application application = Application.fromDocument(database.getData(appId));
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
        String appName = Application.fromDocument(database.getData(appId)).getName();
        database.removeData(appId);
        FileHandler.deleteFolder(Strings.PATH_APPS + "\\" + appName);
        Logger.INFO("The application " + appName + " was deleted.");
    }

    @Override
    public void removePlatforms(String fileName){
        FileHandler.deleteFolder(Strings.PATH_APPS + "\\" + fileName+ "\\platforms");
        FileHandler.createFolder(Strings.PATH_APPS + "\\" + fileName+ "\\platforms");
    }

    @Override
    public void createBehavior(String appId, ApplicationBehavior behavior) {
        Application application = Application.fromDocument(database.getData(appId));
        application.addBehavior(behavior);
        database.updateData(application);
    }

    @Override
    public void removeBehavior(String appId, ApplicationBehavior behavior) {
        Application application = Application.fromDocument(database.getData(appId));
        application.removeBehavior(behavior);
        database.updateData(application);
    }

    @Override
    public Database getDataBase(){
        return database;
    }

    @Override
    public String getPage(String page) {
        return FileHandler.readFile(Strings.PATH_WEB_CONTENT + page);
    }

    @Override
    public void updateApplicationObject(String appId, ApplicationObject object) {
        Application application = Application.fromDocument(database.getData(appId));
        application.updateObject(object);
        database.updateData(application);
    }
}
