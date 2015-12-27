package Logic;

import Utility.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

/**
 * Created by victor on 11/10/2015.
 *
 */


/**
 * A class describing an entity in the application which has a name and several attributes.
 */
public class Entity {

    private String name; // The name of the entity.
    private Map<String, String> attributes; // The attributes of the entity.

    /**
     * Initiates the entity from a JSON compatible string.
     * @param data - a JSON compatible string containing all entity parameters.
     */
    public Entity(String data){
        try {
            JSONObject json = new JSONObject(data);
            this.name = json.getString("name");
            String[] attrs = json.getString("attributes").replace("[","").replace("]","").replace("{","").replace(",","").replace("}",",").split(",");

            for (String attr: attrs) {
                String name = attr.split("name\":\"")[1].split("\"")[0];
                String type = attr.split("type\":\"")[1].split("\"")[0];
                attributes.put(name, type);
            }
            System.out.println(attributes.toString());

        } catch (JSONException e) {
            Logger.ERROR("Failed to initialize entity!", e.getMessage());
        }

    }

    /**
     * @return the name of the entity
     */
    public String getName(){
        return name;
    }

    /**
     * @return the attributes of the entity in "att_1 att_2 ... att_n" format.
     */
    public String getAttributes(){
        String res = "";
        for (String attribute : attributes.keySet()) {
            res += attribute + " - " + attributes.get(attribute);
        }
        return res.substring(0, res.length() - 1);
    }
}
