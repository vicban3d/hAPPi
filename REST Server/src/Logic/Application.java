package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by victor on 1/13/2016.
 *
 */

@SuppressWarnings("unused")
@XmlRootElement
public class Application extends Document{

    @XmlElement(required=true)
    private String id;
    @XmlElement(required=true)
    private String name;
    @XmlElement(required=true)
    private User user;
    @XmlElement(required=true)
    private ArrayList<String> platforms;
    @XmlElement(required=true)
    private ArrayList<ApplicationObject> objects;
    @XmlElement(required=true)
    private ArrayList<ApplicationBehavior> behaviors;

    @JsonCreator
    public Application(@JsonProperty("id") String id,
                       @JsonProperty("name") String name,
                       @JsonProperty("user") User user,
                       @JsonProperty("platforms") ArrayList<String> platforms,
                       @JsonProperty("objects") ArrayList<ApplicationObject> objects,
                       @JsonProperty("behaviors") ArrayList<ApplicationBehavior> behaviors){
        super();
        this.append("id", id);
        this.append("name", name);
        this.append("user",user);
        this.append("platforms", platforms);
        this.append("objects", objects);
        this.append("behaviors", behaviors);
        this.id = id;
        this.name = name;
        this.user = user;
        this.platforms = platforms;
        this.objects = objects;
        this.behaviors = behaviors;
    }

    public String getId() {
        return this.getString("id");
    }

    public void setId(String id) {
        this.put("id", id);
    }

    public String getName() {
        return this.getString("name");
    }

    public void setName(String name) {
        this.put("name", name);
    }

    public String getUser() {
        return this.getString("user");
    }

    public void setUser(User user) {
        this.put("user", user);
    }

    public ArrayList<String> getPlatforms() {
        //noinspection unchecked
        return (ArrayList<String>) this.get("platforms");
    }

    public void setPlatforms(ArrayList<String> platforms) {
        this.put("platforms", platforms);
    }

    public ArrayList<ApplicationObject> getObjects() {
        //noinspection unchecked
        return (ArrayList<ApplicationObject>) this.get("objects");
    }

    public void setObjects(ArrayList<ApplicationObject> objects) {
        this.put("objects",objects);
    }

    public ArrayList<ApplicationBehavior> getBehaviors() {
        //noinspection unchecked
        return (ArrayList<ApplicationBehavior>) this.get("behaviors");
    }

    public void setBehaviors(ArrayList<ApplicationBehavior> behaviors) {
        this.put("behaviors",behaviors);
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
        result += "\tName: " + this.name + "\n";
        result += "\tUsername: " + this.user.getUsername() + "\n";
        result += "\tPlatforms:\n";
        if (platforms != null){
            for (String platform : this.platforms){
                result += "\t* " + platform + "\n";
            }
        }
        if (objects != null) {
            for (Document object : this.objects) {
                result += "\t* " + object + "\n";
            }
        }
        if (behaviors != null) {
            for (Document behavior : this.behaviors) {
                result += "\t* " + behavior + "\n";
            }
        }
        return result;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("id", this.getId());
        json.put("name", this.getName());
        json.put("user", this.getUser());
        json.put("platforms", this.getPlatforms());
        json.put("objects", this.getObjects());
        json.put("behaviors", this.getBehaviors());
        return json;
    }

    public void removeObject(ApplicationObject object) {
        for(int i = 0; i < objects.size(); i++){
            Document doc = objects.get(i);
            if(doc.getString("id").equals(object.getId()))
                objects.remove(i);
        }
        this.put("objects", this.objects);
    }

    public void updateObject(ApplicationObject object) {
        for(int i = 0; i < objects.size(); i++){
            Document doc = objects.get(i);
            if(doc.getString("id").equals(object.getId())){
                objects.remove(i);
                objects.add(object);
            }
        }
        this.put("objects", this.objects);
    }

    public void removeBehavior(ApplicationBehavior behavior) {
        for(int i = 0; i < behaviors.size(); i++){
            Document doc = behaviors.get(i);
            if(doc.getString("id").equals(behavior.getId()))
                behaviors.remove(i);
        }
        this.put("behaviors", this.behaviors);
    }

    public void updateBehavior (ApplicationBehavior behavior){
        for(int i = 0; i < behaviors.size(); i++){
            Document doc = behaviors.get(i);
            if(doc.getString("id").equals(behavior.getId())) {
                behaviors.remove(i);
                behaviors.add(behavior);
            }
        }
        this.put("behaviors", this.behaviors);
    }

    public static Application fromDocument(Document data) {
        //noinspection unchecked
        return new Application(data.getString("id"), data.getString("name"), (User)data.get("user"), (ArrayList<String>)data.get("platforms"),(ArrayList<ApplicationObject>) data.get("objects"), (ArrayList<ApplicationBehavior>) data.get("behaviors"));
    }
}
