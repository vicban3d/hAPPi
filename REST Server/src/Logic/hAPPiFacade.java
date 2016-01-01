package Logic;

import Database.Database;
import Database.MongoDB;
import Exceptions.CordovaRuntimeException;
import Exceptions.DatabaseConnectionErrorException;
import Utility.*;
import Utility.FileHandler;
import Utility.Logger;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.io.File;
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
    public void createApplication(String application) throws CordovaRuntimeException {
        JSONObject json;
        try {
            json = new JSONObject(application);
            compiler.createApplication(json);
        } catch (JSONException e) {
            Logger.ERROR("Error creating JSON object!", e.getMessage());
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
    public void createEntity(String application, String entity){
        database.addData(application, "Entities", entity);
        Entity newEntity = new Entity(entity);
        String jsValue = jsCreator.create(newEntity);
        String path = Strings.PATH_APPS + "\\" + application + "\\www\\js\\entities.js";
        FileHandler.writeFile(path, jsValue);

        DBCollection entities = database.getData(application, "Entities");
        DBCursor c =  entities.find();

        String result = "";
        int i=0;
        for (DBObject en : c) {
            try {
                JSONObject json = new JSONObject(String.format("%s",en));
                String name  = json.getString("name");
                String attributes = json.getString("attributes");
                String actions = json.getString("actions");
                result += i++ + ") Name: " + name + "\n   Attributes: " + attributes + "\n" + "Actions: " + actions+ "<hr>";
            } catch (JSONException e) {
                Logger.ERROR("Failed while handling JSON object.", e.getMessage());
            }
        }
        System.out.println(result);
        //Edit index.html file with the entities
        String indexPath = Strings.PATH_APPS + "\\" + application + "\\www\\index.html";
        String content =  createHtmlContent(result);
        try {
            FileHandler.clearFile(indexPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileHandler.writeFile(indexPath, content);
    }

    @Override
    public void removeEntity(String application, String entity) {
        database.removeData(application, "Entities", entity);
        Entity newEntity = new Entity(entity);
        String jsValue = jsCreator.create(newEntity);
        String path = Strings.PATH_APPS + "\\" + application + "\\www\\js\\entities.js";
        FileHandler.removeFromFile(path, jsValue);
    }

    @Override
    public void connectToDatabase() throws DatabaseConnectionErrorException {
        database.connect();
    }

    @Override
    public void clearDatabase() {
        database.cleaAll();
    }

    @Override
    public void removeApplication(String application){
        File file = null;
        String appName = "";
        try {
            JSONObject json = new JSONObject(application);
            appName = (String) json.get("name");
            database.removeData(appName, null, null);
            file = new File(Strings.PATH_APPS + "\\" + appName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        FileHandler.deleteFolder(file);
        Logger.INFO("The application " + appName + " was deleted.");
    }
}
