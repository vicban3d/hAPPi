package Logic;

import Database.Database;
import Database.MongoDB;
import Utility.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

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
    public void createProject(String project){
        JSONObject json;
        try {
            json = new JSONObject(project);
            compiler.createProject(json);
        } catch (JSONException e) {
            Logger.logERROR("Error creating JSON object!", e.getMessage());
        }
    }

    @Override
    public void addAndroidToProject(String project) {
        JSONObject json;
        try {
            json = new JSONObject(project);
            compiler.addPlatform(json, "android");
        } catch (JSONException e) {
            Logger.logERROR("Error creating JSON object!", e.getMessage());
        }
    }

    @Override
    public void addIOSToProject(String project) {

    }

    @Override
    public void addWindowsPhoneToProject(String project) {

    }

    @Override
    public void buildProject(String project){
        JSONObject json;
        try {
            json = new JSONObject(project);
            compiler.buildProject(json);
        } catch (JSONException e) {
            Logger.logERROR("Error creating JSON object!", e.getMessage());
        }
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
    public void connectToDatabase() {
        database.connect();
    }
}
