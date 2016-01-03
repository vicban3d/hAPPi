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
     * Creates an entity deceleration in JavaScript.
     * @param newEntity - the entity to create.
     * @return a string containing the relevant JavaScript command.
     */
    public String create(Entity newEntity) {
        String result = "";
        String name = newEntity.getName();
        String attributes = newEntity.getAttributes();
        String actions = newEntity.getActions();
        result += "var " + name + " = makeStruct(" + name + " " + attributes + " " + actions + ");";
        return result;
    }
}
