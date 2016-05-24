package logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Gila-Ber on 10/03/2016.
 */
@SuppressWarnings("unused")
@XmlRootElement
public class Condition extends Document {

    @XmlElement(required = true)
    private ObjectAttribute attribute;
    @XmlElement(required = true)
    private String logicOperation;
    @XmlElement(required = true)
    private String value;

    @JsonCreator
    public Condition(
            @JsonProperty("attribute") ObjectAttribute attribute,
            @JsonProperty("logicOperation") String logicOperation,
            @JsonProperty("value") String value){

        super();
        this.append("attribute", attribute);
        this.append("logicOperation", logicOperation);
        this.append("value", value);
        this.attribute = attribute;
        this.logicOperation = logicOperation;
        this.value = value;
    }

    @Override
    public String toString() {
        return "Condition:\n" +
                "\t* Attribute: " + this.attribute +
                "\t* Logic Operation: " + this.logicOperation +
                "\t* Value: " + this.value;
    }

    public ObjectAttribute getAttribute() {
        return attribute;
    }

    public void setAttribute(ObjectAttribute attribute) {
        this.attribute = attribute;
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
}
