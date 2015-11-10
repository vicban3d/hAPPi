package Util;

import Logic.Entity;

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
        FileHandler.writeFile(Strings.PATH_PROJECTS + "/" + projectName + "/www/js/" + name + ".js", text, StandardOpenOption.CREATE);
    }




    public String create(Entity newEntity) {
        String result = "";
        String name = newEntity.getName();
        String attributes = newEntity.getAttributes();
        result += "var " + name + " = createStruct(" + name + " " + attributes + ");";
        return result;
    }
}
