package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by victor on 1/13/2016.
 *
 */

@SuppressWarnings("unused")
@XmlRootElement
public class ApplicationBehavior extends Document{

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private ArrayList<BehaviorAction> actions;


    @JsonCreator
    public ApplicationBehavior(@JsonProperty("name") String name,
                               @JsonProperty("action") ArrayList<BehaviorAction> actions) {

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

    public ArrayList<BehaviorAction> getActions() {
        return actions;
    }

    public void setActions(ArrayList<BehaviorAction> actions) {
        this.actions = actions;
    }

    @Override
    public String toString() {
        String result = "Behavior:\n" +
                "\t* Name: " + name + "\n" +
                "\t* Actions:\n";
        for (BehaviorAction act : this.actions){
            result  = result + "\t* " + act.toString();
        }
        return result;


    }
}