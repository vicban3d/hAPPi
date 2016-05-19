package logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by victor on 11/10/2015.
 *
 */


/**
 * A class describing an entity in the application which has a name and several attributes.
 */

@SuppressWarnings("unused")
@XmlRootElement
public class ApplicationObject extends Document{

    @XmlElement(required=true)
    private String id;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private List<ObjectAttribute> attributes;
    @XmlElement(required = true)
    private List<ObjectAction> actions;

    @JsonCreator
    public ApplicationObject(@JsonProperty("id") String id,
                             @JsonProperty("name") String name,
                             @JsonProperty("attributes") List<ObjectAttribute> attributes,
                             @JsonProperty("actions") List<ObjectAction> actions) {

        super();
        this.append("id",id);
        this.append("name", name);
        this.append("attributes", attributes);
        this.append("actions", actions);
        this.id = id;
        this.name = name;
        this.attributes = attributes;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ObjectAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(ArrayList<ObjectAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<ObjectAction> getActions() {
        return actions;
    }

    public void setActions(ArrayList<ObjectAction> actions) {
        this.actions = actions;
    }

    public String toString(){
        String result = "Object:\n";
        result += "\tId: " + this.id + "\n";
        result += "\tName: " + this.name + "\n";
        result += "\tAttributes:\n";
        for (ObjectAttribute key : this.attributes){
            result += "\t* " + key.getType() + " - " + key.getName() + "\n";
        }
        result += "\tActions:\n";
        for (ObjectAction key : this.actions){
            result += "\t* " + key.getName() + ": " + key.getOperand1().getName() + " " + key.getOperator() + " " + key.getOperandType() + " " +key.getOperand2() + "\n";
        }
        return result;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static ApplicationObject fromDocument(Document data){

        return new ApplicationObject(data.getString("id"), data.getString("name"), (ArrayList<ObjectAttribute>) data.get("attributes"), (ArrayList<ObjectAction>)data.get("actions"));
    }
}
