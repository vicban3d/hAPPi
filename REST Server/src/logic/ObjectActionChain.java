package logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Almog on 25/04/2016.
 */

@SuppressWarnings("unused")
@XmlRootElement
public class ObjectActionChain extends Document{

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private List<ActionChain> actions;

    @JsonCreator
    public ObjectActionChain(@JsonProperty("name") String name, @JsonProperty("actions") List<ActionChain> actions) {
        super();
        this.append("name", name);
        this.append("actions", actions);
        this.name = name;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ActionChain> getActionsChain() {
        return actions;
    }

    public void setActionsChain(List<ActionChain> actions) { this.actions = actions; }

    @Override
    public String toString() {
        String result = "Action:\n" +
                "\t* name: " + this.name +
                "\t* actionsChain:\n";
        for (ActionChain act : this.actions){
            result  = result + "\t* " + act.toString();
        }
        return result;
    }

    public static ObjectActionChain fromDocument(Document data){
        return new ObjectActionChain(data.getString("name"), (ArrayList<ActionChain>) data.get("actions"));
    }
}