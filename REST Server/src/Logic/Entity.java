package Logic;

import Utility.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import java.util.HashMap;
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
    private final Map<String, String> attributes; // The attributes of the entity.
    private  final Map<String, String[]> actions;

    /**
     * Initiates the entity from a JSON compatible string.
     * @param data - a JSON compatible string containing all entity parameters.
     */
    public Entity(String data){
        attributes = new HashMap<>();
        actions = new HashMap<>();
        try {
            JSONObject json = new JSONObject(data);
            this.name = json.getString("name");

            if (!json.getString("attributes").equals("[]")) {
                String[] attrs = json.getString("attributes").substring(1, json.getString("attributes").length() - 1).split("},\\{");

                for (String attr : attrs) {
                    if (attr.charAt(0) != '{') {
                        attr = "{" + attr;
                    }
                    if (attr.charAt(attr.length() - 1) != '}') {
                        attr = attr + "}";
                    }
                    JSONObject jattr = new JSONObject(attr);
                    String name = jattr.getString("name");
                    String type = jattr.getString("type");
                    attributes.put(name, type);
                }

            }
            if (!json.getString("actions").equals("[]")) {
                String[] acts = json.getString("actions").substring(1, json.getString("actions").length() - 1).split("},\\{");
                for (String act : acts) {
                    if (act.charAt(0) != '{') {
                        act = "{" + act;
                    }
                    if (act.charAt(act.length() - 1) != '}') {
                        act = act + "}";
                    }
                    System.out.println(act);
                    JSONObject jact = new JSONObject(act);
                    String name = jact.getString("name");
                    String o1 = new JSONObject(jact.getString("operand1")).getString("name");
                    String op = jact.getString("operator");
                    String o2 = jact.getString("operand2");
                    String[] arr = new String[3];
                    arr[0] = o1;
                    arr[1] = op;
                    arr[2] = o2;
                    actions.put(name, arr);
                }
            }

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
            res += attribute + " - " + attributes.get(attribute) + " ";
        }
        return res;
    }

    public String getActions() {
        String res = "";
        for (String action : actions.keySet()) {
            res += action + " - " + actions.get(action)[0] + " " + actions.get(action)[1]+ " " + actions.get(action)[2]  + "\n";
        }
        return res;
    }
}
