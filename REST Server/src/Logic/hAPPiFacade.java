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
import com.mongodb.util.JSON;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

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
    public void createProject(String project) throws CordovaRuntimeException {
        database.cleaAll();
        JSONObject json;
        try {
            json = new JSONObject(project);
            compiler.createProject(json);
        } catch (JSONException e) {
            Logger.ERROR("Error creating JSON object!", e.getMessage());
        }
    }

    @Override
    public void addAndroidToProject(String projectName) throws CordovaRuntimeException {
        compiler.addPlatform(projectName, "android");
    }

    @Override
    public void addIOSToProject(String projectName) throws CordovaRuntimeException {
        compiler.addPlatform(projectName, "ios");
    }

    @Override
    public void addWindowsPhoneToProject(String projectName) throws CordovaRuntimeException {
        compiler.addPlatform(projectName, "wp8");
    }

    @Override
    public void buildProject(String projectName) throws CordovaRuntimeException {
        compiler.buildProject(projectName);

        DBCollection entities = database.getData(projectName, "Entities");
        DBCursor c =  entities.find();
        String result = "";
        int i=0;
        for (DBObject entity : c) {
            try {
                JSONObject json = new JSONObject(String.format("%s",entity));
                String name  = json.getString("name");
                String attributes = json.getString("attributes");
                result += i++ + ") Name: " + name + "\n   Attributes: " + attributes + "\n" + "<hr>";
            } catch (JSONException e) {
                Logger.ERROR("Failed while handling JSON object.", e.getMessage());
            }
        }
        //Edit index.html file with the entities
        String indexPath = Strings.PATH_PROJECTS + "\\" + projectName + "\\www\\index.html";
        String content =  createHtmlContent(result);
        try {
            FileHandler.clearFile(indexPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileHandler.writeFile(indexPath, content);
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
    public void createEntity(String project, String entity){
        database.addData(project, "Entities", entity);
        Entity newEntity = new Entity(entity);
        String jsValue = jsCreator.create(newEntity);
        String path = Strings.PATH_PROJECTS + "\\" + project + "\\www\\js\\entities.js";
        FileHandler.writeFile(path, jsValue);
    }

    @Override
    public void connectToDatabase() throws DatabaseConnectionErrorException {
        database.connect();
    }
}
