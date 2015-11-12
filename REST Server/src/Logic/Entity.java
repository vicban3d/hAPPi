package Logic;

import Utility.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by victor on 11/10/2015.
 *
 */


/**
 * A class describing an entity in the application which has a name and several attributes.
 */
public class Entity {

    private String name; // The name of the entity.
    private String[] attributes; // The attributes of the entity.

    /**
     * Initiates the entity from a JSON compatible string.
     * @param data - a JSON compatible string containing all entity parameters.
     */
    public Entity(String data){
        try {
            JSONObject json = new JSONObject(data);
            this.name = json.getString("name");
            this.attributes = json.getString("attributes").split(" ");
        } catch (JSONException e) {
            Logger.logERROR("Failed to initialize entity!", e.getMessage());
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
        for (String attribute : attributes) {
            res += attribute + " ";
        }
        return res.substring(0, res.length() - 1);
    }
}
