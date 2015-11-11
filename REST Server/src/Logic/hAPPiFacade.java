package Logic;

import Database.MongoDB;
import Util.*;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by victor on 11/10/2015.
 */
public class hAPPiFacade implements Facade {

    private MongoDB db;
    private JSCreator jsCreator;
    private AppCompiler compiler;
    public  hAPPiFacade(){
        db = new MongoDB();
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
    public void createEntity(String project, String entity){
        db.addData(project, "Entities", entity);
        Entity newEntity = new Entity(entity);
        String jsValue = jsCreator.create(newEntity);
        String path = Strings.PATH_PROJECTS + "/" + project + "/www/js/entities.js";
        FileHandler.writeFile(path, jsValue);
    }

    @Override
    public void connectToDatabase() {
        db.connect();
    }
}
