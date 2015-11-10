package Logic;

import Util.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by victor on 11/10/2015.
 *
 */
public class Entity {

    private String name;
    private String[] attributes;
    public Entity(String data){
        try {
            JSONObject json = new JSONObject(data);
            this.name = json.getString("name");
            this.attributes = json.getString("attributes").split(" ");
            System.out.println(attributes);
        } catch (JSONException e) {
            Logger.logERROR("Failed to initialize entity!", e.getMessage());
        }

    }

    public String getName(){
        return name;
    }

    public String getAttributes(){
        String res = "";
        for (int i=0; i<attributes.length; i++){
            res+=attributes[i] + " ";
        }
        return res.substring(0, res.length() - 1);
    }
}
