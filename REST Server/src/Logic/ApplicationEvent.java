package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created by Gila-Ber on 18/04/2016.
 */
@SuppressWarnings("unused")
@XmlRootElement
public class ApplicationEvent extends Document {

    @XmlElement(required=true)
    private String id;
    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private ApplicationObject applicationObject;
    @XmlElement(required = true)
    private ObjectAttribute objectAttribute;
    @XmlElement(required = true)
    private String logicOperation;
    @XmlElement(required = true)
    private String value;

    @JsonCreator
    public ApplicationEvent(@JsonProperty("id") String id,
                            @JsonProperty("name") String name,
                            @JsonProperty("object") ApplicationObject applicationObject,
                            @JsonProperty("attribute") ObjectAttribute objectAttribute,
                            @JsonProperty("operator") String logicOperation,
                            @JsonProperty("value") String value) {
        super();
        this.append("id", id);
        this.append("name", name);
        this.append("object", applicationObject);
        this.append("attribute", objectAttribute);
        this.append("operator", logicOperation);
        this.append("value", value);
        this.id = id;
        this.name = name;
        this.applicationObject = applicationObject;
        this.objectAttribute = objectAttribute;
        this.logicOperation = logicOperation;
        this.value = value;
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

    public ApplicationObject getApplicationObject() {
        return applicationObject;
    }

    public void setApplicationObject(ApplicationObject applicationObject) {
        this.applicationObject = applicationObject;
    }

    public ObjectAttribute getObjectAttribute() {
        return objectAttribute;
    }

    public void setObjectAttribute(ObjectAttribute objectAttribute) {
        this.objectAttribute = objectAttribute;
    }

    public String getLogicOperation() {
        return logicOperation;
    }

    public void setLogicOperation(String logicOperation) {
        this.logicOperation = logicOperation;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "ApplicationEvent{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", applicationObject=" + applicationObject +
                ", objectAttribute=" + objectAttribute +
                ", logicOperation='" + logicOperation + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
