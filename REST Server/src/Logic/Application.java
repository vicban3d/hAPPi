package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by victor on 1/13/2016.
 *
 */

@XmlRootElement
public class Application extends Document{

    @XmlElement(required=true)
    private String id;
    @XmlElement(required=true)
    private String name;
    @XmlElement(required=true)
    private ArrayList<String> platforms;
    @XmlElement(required=true)
    private ArrayList<ApplicationObject> objects;
    @XmlElement(required=true)
    private ArrayList<ApplicationBehavior> behaviors;

    @JsonCreator
    public Application(@JsonProperty("id") String id,
                       @JsonProperty("name") String name,
                       @JsonProperty("platforms") ArrayList<String> platforms,
                       @JsonProperty("objects") ArrayList<ApplicationObject> objects,
                       @JsonProperty("behaviors") ArrayList<ApplicationBehavior> behaviors){
        super();
        this.append("id", id);
        this.append("name", name);
        this.append("platforms", platforms);
        this.append("objects", objects);
        this.append("behaviors", behaviors);
        this.id = id;
        this.name = name;
        this.platforms = platforms;
        this.objects = objects;
        this.behaviors = behaviors;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getPlatforms() {
        return platforms;
    }

    public void setPlatforms(ArrayList<String> platforms) {
        this.platforms = platforms;
    }

    public ArrayList<ApplicationObject> getObjects() {
        return objects;
    }

    public void setObjects(ArrayList<ApplicationObject> objects) {
        this.objects = objects;
    }

    public ArrayList<ApplicationBehavior> getBehaviors() {
        return behaviors;
    }

    public void setBehaviors(ArrayList<ApplicationBehavior> behaviors) {
        this.behaviors = behaviors;
    }

    private void parsePlatforms(String platforms) {
        Collections.addAll(this.platforms, platforms.split(","));
    }

    private void parseObjects(String objects) {
        System.out.println(objects);
    }

    private void parseBehaviors(String behaviors) {
        System.out.println(behaviors);
    }

    public void addObject(ApplicationObject newObject) {
        this.objects.add(newObject);
        this.put("objects", this.objects);
    }

    public void addBehavior(ApplicationBehavior newBehavior) {
        this.behaviors.add(newBehavior);
        this.put("behaviors", this.behaviors);
    }

    @Override
    public String toString(){
        String result = "Application:\n";
        result += "\tId: " + this.id + "\n";
        result += "\tName: " + this.name + "\n";
        result += "\tPlatforms:\n";
        if (platforms != null){
            for (String platform : this.platforms){
                result += "\t* " + platform + "\n";
            }
        }
        if (objects != null) {
            for (ApplicationObject object : this.objects) {
                result += "\t* " + object + "\n";
            }
        }
        if (behaviors != null) {
            for (ApplicationBehavior behavior : this.behaviors) {
                result += "\t* " + behavior + "\n";
            }
        }

        return result;

    }

    public JSONObject toJSON() throws IOException, JSONException {
        JSONObject json = new JSONObject();
        json.put("id", this.id);
        json.put("name", this.name);
        json.put("platforms", this.platforms);
        json.put("objects", this.objects);
        json.put("behaviors", this.behaviors);
        return json;
    }

    public void removeObject(ApplicationObject object) {
        objects.remove(object);
    }

    public void removeBehavior(ApplicationBehavior object) {
        behaviors.remove(object);
    }
}
