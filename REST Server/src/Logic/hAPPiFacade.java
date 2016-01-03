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

import java.io.File;
import java.io.IOException;
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
        json = new JSONObject(application);
        compiler.createApplication(json);
        String indexPath = Strings.PATH_APPS + "\\" + json.getString("name") + "\\www\\index.html";
        String content =  createHtmlContent("<h1>Hello World!</h1>");
        FileHandler.clearFile(indexPath);
        FileHandler.writeFile(indexPath, content);
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
    public void createEntity(String application, String entity) throws IOException, JSONException {
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
            JSONObject json = new JSONObject(String.format("%s",en));
            String name  = json.getString("name");
            String attributes = json.getString("attributes");
            String actions = json.getString("actions");
            result += i++ + ") Name: " + name + "\n   Attributes: " + attributes + "\n" + "Actions: " + actions+ "<hr>";
        }
        //Edit index.html file with the entities
        String indexPath = Strings.PATH_APPS + "\\" + application + "\\www\\index.html";
        String content =  createHtmlContent(result);
        FileHandler.clearFile(indexPath);
        FileHandler.writeFile(indexPath, content);
    }

    @Override
    public void removeEntity(String application, String entity) throws IOException {
        database.removeData(application, "Entities", entity);
        Entity newEntity = new Entity(entity);
        String jsValue = jsCreator.create(newEntity);
        String path = Strings.PATH_APPS + "\\" + application + "\\www\\js\\entities.js";
        FileHandler.removeFromFile(path, jsValue);
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
    public void removeApplication(String application) throws JSONException {
        JSONObject json = new JSONObject(application);
        String appName = (String) json.get("name");
        database.removeData(appName, null, null);
        File file = new File(Strings.PATH_APPS + "\\" + appName);
        FileHandler.deleteFolder(file);
        Logger.INFO("The application " + appName + " was deleted.");
    }
}
