package Logic;

import Database.Database;
import Database.MongoDB;
import Exceptions.CordovaRuntimeException;
import Exceptions.DatabaseConnectionErrorException;
import Utility.*;
import Utility.FileHandler;
import Utility.Logger;
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
        //Edit index.html file with the entities
        String indexPath = Strings.PATH_PROJECTS + "\\" + projectName + "\\www\\index.html";
        String entitiesPath = Strings.PATH_PROJECTS + "\\" + projectName + "\\www\\js\\entities.js";
        String content = FileHandler.readFile(entitiesPath);
        HashMap<String, String[]> elements = makeMapOfElements(content);
        content =  createHtmlContent(elements);
        try {
            FileHandler.clearFile(indexPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        FileHandler.writeFile(indexPath, content);
    }

    private HashMap<String, String[]> makeMapOfElements(String content) {
        HashMap<String,String[]> result = new HashMap<>();
        if (content == null) {
            return result;
        }
        String[] split = content.split("\\)");
        for (int j=0; j < split.length - 1 ; j++){
            String[] elements = split[j].split("\\(");
            elements = elements[1].split(" ");
            String[] attributes = new String[elements.length -1];
            System.arraycopy(elements, 1, attributes, 0, elements.length - 1);
            result.put(elements[0], attributes);
        }
        return result;
    }

    private String createListEntities(HashMap<String,String[]> elements){
        String result = "";
        Object[] keys = elements.keySet().toArray();
        for(int i=0; i < keys.length ; i++){
            String element = (String)keys[i];
            String[] attributes = elements.get(keys[i]);
            result += i+1 + ") Name: " + element + "\n   Attributes: " + Arrays.toString(attributes) + "\n" + "<hr>";
        }
        return result;
    }

    private String createHtmlContent(HashMap<String,String[]> elements){
        return  "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "<pre>\n" +
                createListEntities(elements) +
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
