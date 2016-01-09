package Logic;

import Database.Database;
import Database.MongoDB;
import Exceptions.CordovaRuntimeException;
import Utility.*;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
    public void createApplication(String application) throws CordovaRuntimeException, JSONException, IOException {
        JSONObject json;
        try {
            json = new JSONObject(application);
            compiler.createApplication(json);
            createPlatforms(json.getString("platforms"), json.getString("name"));
            database.addApplication(json);
        } catch (JSONException e) {
            Logger.ERROR("Error creating JSON object!", e.getMessage());
        }
    }

    @Override
    public void updateApplication(String application) throws CordovaRuntimeException, IOException {
        JSONObject json;
        try {
            json = new JSONObject(application);
            String oldApplicationName = database.getApplicationNameById(json.getString("id")); //get app name from db
            removePlatforms(oldApplicationName);
            FileHandler.renameFolder(oldApplicationName, json.getString("name")); //rename the app folder name
            createPlatforms(json.getString("platforms"), json.getString("name"));
            updateApplicationInDB(json);
        } catch (JSONException e) {
            Logger.ERROR("Error creating JSON object!", e.getMessage());
        }

    }

    private void updateApplicationInDB(JSONObject json) throws JSONException {
        LinkedList<JSONObject> elementsToUpdate = new LinkedList<JSONObject>();
        LinkedList<String> elements = new LinkedList<>();
        elements.add("name");
        elements.add("platforms");
        JSONObject j1 = new JSONObject();
        j1.put("name",json.getString("name"));
        elementsToUpdate.add(j1);
        JSONObject j2 = new JSONObject();
        j2.put("platforms",json.getString("platforms"));
        elementsToUpdate.add(j2);
        database.updateApplication(json.getString("id"),elementsToUpdate, elements);//update app name in database
    }


    private void createPlatforms(String platforms, String applicationName) throws CordovaRuntimeException {
        String[] split = platforms.replace("[", "").replace("]", "").split(",");
        for (String s: split) {
            s = s.substring(1,s.length() -1);
            if(s.equals("android"))
                addAndroidToApplication(applicationName);
            if(s.equals("ios"))
                addIOSToApplication(applicationName);
            if(s.equals("windowsPhone"))
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
    public void buildApplication(String applicationName) throws CordovaRuntimeException {
        compiler.buildApplication(applicationName);
    }

    private String createHtmlContent(String content){
        return  "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<pre>\n" +
                content +
                "</pre>\n" +
                "</body>\n" +
                "</html>";
    }

    @Override
    public void createEntity(String appId, String appName, String entity) throws JSONException{

        database.addData(appId, appName, "entities", entity);
        Entity newEntity = new Entity(entity);
        String jsValue = jsCreator.create(newEntity);
        String path = Strings.PATH_APPS + "\\" + appName + "\\www\\js\\entities.js";
            try {
                FileHandler.writeFile(path, jsValue);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DBCollection entities = database.getData(appId, "entities");
        DBCursor c =  entities.find();

        String result = "";
        int i=0;
        for (DBObject en : c) {
            JSONObject json = new JSONObject(String.format("%s",en));
            String name  = json.getString("name");
            String attributes = json.getString("attributes");
            String actions = json.getString("actions");
            result += i++ + ") Name: " + name + "\n   Attributes: " + attributes + "\n" + "Actions: " + actions+ "<hr>";
        }
        //Edit index.html file with the entities
        String indexPath = Strings.PATH_APPS + "\\" + appName + "\\www\\index.html";
        String content =  createHtmlContent(result);
            try {
                FileHandler.clearFile(indexPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                FileHandler.writeFile(indexPath, content);
            } catch (IOException e) {
                e.printStackTrace();
            }

    }

    @Override
    public void removeEntity(String appId, String appName, String entity) {
        database.removeData(appId, "entities", entity);
        Entity newEntity = new Entity(entity);
        String jsValue = jsCreator.create(newEntity);
        String path = Strings.PATH_APPS + "\\" + appName + "\\www\\js\\entities.js";
        try {
            FileHandler.removeFromFile(path, jsValue);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connectToDatabase() throws IOException {
        database.connect();
    }

    @Override
    public void clearDatabase() {
        database.cleaAll();
    }

    @Override
    public void removeApplication(String application){
        File file = null;
        String appId = "";
        String appName = "";
        try {
            JSONObject json = new JSONObject(application);
            appId = (String) json.get("id");
            appName = (String)json.get("name");
            database.removeData(appId, null, null);
            file = new File(Strings.PATH_APPS + "\\" + appName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FileHandler.deleteFolder(file);
        Logger.INFO("The application " + appName + " was deleted.");
    }

    @Override
    public void removePlatforms(String fileName){
        File file = new File(Strings.PATH_APPS + "\\" + fileName+ "\\platforms");
        FileHandler.deleteFolder(file);
        FileHandler.createFolder(file);
    }
}
