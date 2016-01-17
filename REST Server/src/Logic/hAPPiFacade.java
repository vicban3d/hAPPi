package Logic;

import Database.Database;
import Database.MongoDB;
import Exceptions.CordovaRuntimeException;
import Utility.*;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by victor on 11/10/2015.
 *
 */
public class hAPPiFacade implements Facade {

    private final Database database;
    private final JSCreator jsCreator;
    private final AppCompiler compiler;

    /**
     * Constructor and initiator for the Facade.
     */
    public  hAPPiFacade(){
        database = new MongoDB();
        jsCreator = new JSCreator();
        compiler = new CordovaAppCompiler();
    }

    @Override
    public void createApplication(Application application) throws CordovaRuntimeException, JSONException, IOException {
            database.addData(application);
            compiler.createApplication(application.toJSON());
            createPlatforms(application.getPlatforms(), application.getName());
    }

    @Override
    public void updateApplication(Application application) throws CordovaRuntimeException, IOException {
        JSONObject json;
        try {
            json = application.toJSON();
            Application oldApp = database.getData(application.getId());
            String oldApplicationName = oldApp.getName();
            removePlatforms(oldApplicationName);
            FileHandler.renameFolder(oldApplicationName, json.getString("name")); //rename the app folder name
            //createPlatforms(json.getString("platforms"), json.getString("name"));
            updateApplicationInDB(json);
        } catch (JSONException e) {
            Logger.ERROR("Error creating JSON object!", e.getMessage());
        }

    }

    private void updateApplicationInDB(JSONObject json) throws JSONException {
        LinkedList<JSONObject> elementsToUpdate = new LinkedList<>();
        LinkedList<String> elements = new LinkedList<>();
        elements.add("name");
        elements.add("platforms");
        JSONObject j1 = new JSONObject();
        j1.put("name",json.getString("name"));
        elementsToUpdate.add(j1);
        JSONObject j2 = new JSONObject();
        j2.put("platforms",json.getString("platforms"));
        elementsToUpdate.add(j2);
//        database.updateApplication(json.getString("id"),elementsToUpdate, elements);//update app name in database
    }


    private void createPlatforms(ArrayList<String> platforms, String applicationName) throws CordovaRuntimeException {
        if(platforms == null || platforms.isEmpty()) {
            return;
        }
        for (String platform : platforms) {
            if(platform.equals("android"))
                addAndroidToApplication(applicationName);
            if(platform.equals("ios"))
                addIOSToApplication(applicationName);
            if(platform.equals("windowsPhone"))
                addWindowsPhoneToApplication(applicationName);
        }
    }

    @Override
    public void addAndroidToApplication(String application) throws CordovaRuntimeException {
        compiler.addPlatform(application, "android");
    }

    @Override
    public void addIOSToApplication(String application) throws CordovaRuntimeException {
        compiler.addPlatform(application, "ios");
    }

    @Override
    public void addWindowsPhoneToApplication(String application) throws CordovaRuntimeException {
        compiler.addPlatform(application, "wp8");
    }

    @Override
    public void buildApplication(String appId) throws CordovaRuntimeException, JSONException, IOException {
        Application application = database.getData(appId);
        prepareApplicationForCompilation(application);
        compiler.buildApplication(application.getName());
    }

    private void prepareApplicationForCompilation(Application application) throws JSONException, IOException {
        String name = application.getName();
        ArrayList<ApplicationObject> objects = application.getObjects();
        ArrayList<ApplicationBehavior> behaviors = application.getBehaviors();
        ArrayList<String> allActions = new ArrayList<>();
        for (Document obj : objects){
            allActions.add(obj.toJson());
        }
        ArrayList<String> allBehaviors = new ArrayList<>();
        JSONArray behaviorsJArr = new JSONArray();
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
    public void createObject(String appId, ApplicationObject data) throws JSONException, IOException {
        Application application = database.getData(appId);
        application.addObject(data);
        database.updateData(application);
    }

    @Override
    public void removeObject(String appId, ApplicationObject object) throws IOException, JSONException {
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
    public void removeApplication(String appId) throws JSONException, IOException {
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
    public void createBehavior(String appId, ApplicationBehavior behavior) throws JSONException, IOException {
        Application application = database.getData(appId);
        application.addBehavior(behavior);
        database.updateData(application);
    }

    @Override
    public void removeBehavior(String appId, ApplicationBehavior behavior) throws IOException, JSONException {
        Application application = database.getData(appId);
        application.removeBehavior(behavior);
        database.updateData(application);
    }

    @Override
    public Database getDataBase(){
        return database;
    }
}
