package Util;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.nio.file.StandardOpenOption;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class JSCreator {

    public JSCreator() {
    }

    public void initializeFile(String projectName, String name){
        String text = "";
        try {
            text+= FileHandler.readFile(Strings.PATH_WEB_CONTENT + "/helper_functions.js");
        } catch (IOException e) {
           Logger.logSEVERE("Could not find Helper Functions JS file!");
        }
        // Add more initial file content here.
        try {
            FileHandler.writeFile(Strings.PATH_PROJECTS + "/" + projectName + "/www/js/" + name + ".js", text, StandardOpenOption.CREATE);
        } catch (IOException e) {
            Logger.logSEVERE("Failed to write to JS file" + name + ".js!");
        }
    }

    public void appendFile(String projectName, String fileName, String data){
        try {
            FileHandler.writeFile(Strings.PATH_PROJECTS + "/" + projectName + "/www/js/" + fileName, data, StandardOpenOption.APPEND);
        } catch (IOException e) {
            Logger.logSEVERE("Failed to write to JS file" + fileName + ".js!");
        }
    }

    public String createEntity(JSONObject entity){
        String result = "";
        try {
            String name = entity.getString("name");
            String attributes = entity.getString("attributes");
            result += "var " + name + " = createStruct(" + name + " " + attributes + ");";

        } catch (JSONException e) {
            Logger.logSEVERE("Failed to create entity!");
        }
        return result;
    }
}
