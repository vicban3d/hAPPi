package Logic;

import Database.MongoDB;
import Util.FileHandler;
import Util.JSCreator;
import Util.Logger;
import Util.Strings;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.nio.file.StandardOpenOption;

/**
 * Created by victor on 11/10/2015.
 */
public class hAPPiFacade implements Facade {

    private MongoDB db;
    private JSCreator jsCreator;

    public  hAPPiFacade(){
        db = new MongoDB();
        jsCreator = new JSCreator();
    }

    @Override
    public void createProject(String project){
        JSONObject json = null;
        try {
            json = new JSONObject(project);
            String name = json.getString("name");
            Logger.logINFO("Creating Project " + name + "...");
            try {
                Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " create " + Strings.PATH_PROJECTS + "/" + name + " com.example.hello " + name);
                p.waitFor();
                Logger.logINFO("Created new project " + json.getString("name") + ".");
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            Logger.logERROR("Failed to handle JSON object!", e.getMessage());
        }

    }

    @Override
    public void createEntity(String project, String entity){
        db.addData(project, "Entities", entity);
        Entity newEntity = new Entity(entity);
        String jsValue = jsCreator.create(newEntity);
        String path = Strings.PATH_PROJECTS + "/" + project + "/www/js/entities.js";
        FileHandler.writeFile(path, jsValue, StandardOpenOption.APPEND);
    }

    @Override
    public void connectToDatabase() {
        db.connect();
    }
}
