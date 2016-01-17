package Utility;

import Logic.ApplicationObject;

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
//     * Creates an entity deceleration in JavaScript.
//     * @param newApplicationObject - the entity to create.
//     * @return a string containing the relevant JavaScript command.
//     */
//    public String create(ApplicationObject newApplicationObject) {
//        String result = "";
//        String name = newApplicationObject.getName();
//        String attributes = newApplicationObject.getAttributes();
//        String actions = newApplicationObject.getActions();
//        result += "var " + name + " = makeStruct(" + name + " " + attributes + " " + actions + ");";
//        return result;
//    }
}
