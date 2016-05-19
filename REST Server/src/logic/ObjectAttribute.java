package logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by victor on 1/14/2016.
 *
 */

@SuppressWarnings("unused")
@XmlRootElement
public class ObjectAttribute extends Document{

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private  String type;

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

    public static ObjectAttribute fromDocument(Document data) {
        //noinspection unchecked
        return new ObjectAttribute(data.getString("name"),data.getString("type"));
    }
}
