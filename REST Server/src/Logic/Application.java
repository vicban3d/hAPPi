package Logic;

import com.sun.jersey.core.impl.provider.entity.XMLJAXBElementProvider;
import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

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
    private String username;
    @XmlElement(required=true)
    private List<String> platforms;
    @XmlElement(required=true)
    private List<ApplicationObject> objects;
    @XmlElement(required=true)
    private List<ApplicationBehavior> behaviors;
    @XmlElement(required=true)
    private List<ApplicationEvent> events;


    @JsonCreator
    public Application(@JsonProperty("id") String id,
                       @JsonProperty("name") String name,
                       @JsonProperty("username") String username,
                       @JsonProperty("platforms") List<String> platforms,
                       @JsonProperty("objects") List<ApplicationObject> objects,
                       @JsonProperty("behaviors") List<ApplicationBehavior> behaviors,
                       @JsonProperty("events") List<ApplicationEvent> events){
        super();
        this.append("id", id);
        this.append("name", name);
        this.append("username", username);
        this.append("platforms", platforms);
        this.append("objects", objects);
        this.append("behaviors", behaviors);
        this.append("events", events);
        this.id = id;
        this.name = name;
        this.username = username;
        this.platforms = platforms;
        this.objects = objects;
        this.behaviors = behaviors;
        this.events = events;
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

    public ArrayList<String> getPlatforms() {
        //noinspection unchecked
        return (ArrayList<String>) this.get("platforms");
    }

    public String getUsername() {
        return this.getString("username");
    }

    public void setUsername(String username) {
        this.put("username", username);
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

    public ArrayList<ApplicationEvent> getEvents() {
        //noinspection unchecked
        return (ArrayList<ApplicationEvent>) this.get("events");
    }

    public void setEvents(ArrayList<ApplicationEvent> events) {
        this.put("events",events);
    }

    public void addObject(ApplicationObject newObject) {
        this.objects.add(newObject);
        this.put("objects", this.objects);
    }

    public void addBehavior(ApplicationBehavior newBehavior) {
        this.behaviors.add(newBehavior);
        this.put("behaviors", this.behaviors);
    }

    public void addEvent(ApplicationEvent newEvent) {
        this.events.add(newEvent);
        this.put("events", this.events);
    }

    @Override
    public String toString(){
        String result = "Application:\n";
        result += "\tName: " + this.name + "\n";
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

    public JSONObject toJSON(){
        try {
            JSONObject json = new JSONObject();
            json.put("id", this.getId());
            json.put("name", this.getName());
            json.put("username",username);
            json.put("platforms", this.getPlatforms());
            json.put("objects", this.getObjects());
            json.put("behaviors", this.getBehaviors());
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
//        return new Application(data.getString("id"), data.getString("name"),  (ArrayList<String>)data.get("platforms"),(ArrayList<ApplicationObject>) data.get("objects"), (ArrayList<ApplicationBehavior>) data.get("behaviors"));
        return new Application(data.getString("id"), data.getString("name"), data.getString("username"),(ArrayList<String>)data.get("platforms"),(ArrayList<ApplicationObject>) data.get("objects"), (ArrayList<ApplicationBehavior>) data.get("behaviors"), (ArrayList<ApplicationEvent>) data.get("events"));
    }

    public void removeEvent(ApplicationEvent event) {
        for(int i = 0; i < events.size(); i++){
            Document doc = events.get(i);
            if(doc.getString("id").equals(event.getId()))
                events.remove(i);
        }
        this.put("events", this.events);
    }

    public void updateEvent(ApplicationEvent event) {
        for(int i = 0; i < events.size(); i++){
            Document doc = events.get(i);
            if(doc.getString("id").equals(event.getId())){
                events.remove(i);
                events.add(event);
            }
        }
        this.put("events", this.events);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Application)) return false;
        if (!super.equals(o)) return false;

        Application that = (Application) o;

        if (!id.equals(that.id)) return false;
        if (!name.equals(that.name)) return false;
        if (!username.equals(that.username)) return false;
        if (!platforms.equals(that.platforms)) return false;
        if (!objects.equals(that.objects)) return false;
        if (!behaviors.equals(that.behaviors)) return false;
        return events.equals(that.events);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + username.hashCode();
        result = 31 * result + platforms.hashCode();
        result = 31 * result + objects.hashCode();
        result = 31 * result + behaviors.hashCode();
        result = 31 * result + events.hashCode();
        return result;
    }
}
