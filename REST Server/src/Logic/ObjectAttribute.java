package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by victor on 1/14/2016.
 *
 */

@XmlRootElement
public class ObjectAttribute extends Document{

    @XmlElement(required = true)
    String name;
    @XmlElement(required = true)
    String type;

    @JsonCreator
    public ObjectAttribute(@JsonProperty("name") String name,@JsonProperty("type") String type) {
        super();
        this.append("name", name);
        this.append("type", type);
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Attribute:\n" +
                "\t* Name: " + this.name +
                "\t* Type: " + this.type;
    }
}
