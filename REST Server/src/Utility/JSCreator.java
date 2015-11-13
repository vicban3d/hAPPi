package Utility;

import Logic.Entity;

/**
 * Created by victor on 11/9/2015.
 *
 */

/**
 * A class which creates JavaScript commands from given objects.
 */
public class JSCreator {

    public static final String JSFUNCTION_MAKE_STRUCT = "function makeStruct(attributes) { // a general factory for structs\n" +
            "    var fields = attributes.split(' ');\n" +
            "    var count = fields.length;\n" +
            "    function constructor() {\n" +
            "        for (var i = 0; i < count; i++) {\n" +
            "            this[fields[i]] = arguments[i];\n" +
            "        }\n" +
            "    }\n" +
            "    return constructor;\n" +
            "}";

    /**
     * Initializes a JavaScript file in the given project with all relevant helper functions.
     * @param projectName - the name of the project.
     * @param name - the name of the file.
     */
    public void initializeFile(String projectName, String name){
        String text = "";
        text+= FileHandler.readFile(Strings.PATH_WEB_CONTENT + "\\helper_functions.js");
        // Add more initial file content here.
        FileHandler.writeFile(Strings.PATH_PROJECTS + "\\" + projectName + "\\www\\js\\" + name + ".js", text);
    }

    /**
     * Creates an entity deceleration in JavaScript.
     * @param newEntity - the entity to create.
     * @return a string containing the relevant JavaScript command.
     */
    public String create(Entity newEntity) {
        String result = "";
        String name = newEntity.getName();
        String attributes = newEntity.getAttributes();
        result += "var " + name + " = makeStruct(" + name + " " + attributes + ");";
        return result;
    }
}
