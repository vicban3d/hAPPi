package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

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
public class ApplicationBehavior extends Document{

    @XmlElement(required=true)
    private String id;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private BehaviorAction action;


    @JsonCreator
    public ApplicationBehavior (@JsonProperty("id") String id,
                               @JsonProperty("name") String name,
                               @JsonProperty("action") BehaviorAction action) {

        super();
        this.append("id", id);
        this.append("name", name);
        this.append("action", action);
        this.id = id;
        this.name = name;
        this.action = action;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BehaviorAction getAction() {
        return action;
    }

    public void setAction(BehaviorAction action) {
        this.action = action;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        String result = "Behavior:\n" +
                "\t* Name: " + name + "\n" +
                "\t* Actions:\n";
            result  = result + "\t* " + action.toString();

        return result;
    }
}
