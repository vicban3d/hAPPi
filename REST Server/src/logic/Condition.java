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
    private ObjectActionChain actionChain;
    @XmlElement(required = true)
    private String logicOperation;
    @XmlElement(required = true)
    private String value;

    @JsonCreator
    public Condition(
            @JsonProperty("operandAttribute") ObjectAttribute attribute,
            @JsonProperty("operandActionChain") ObjectActionChain actionChain,
            @JsonProperty("logicOperation") String logicOperation,
            @JsonProperty("value") String value){

        super();
        this.append("operandAttribute", attribute);
        this.append("operandActionChain", actionChain);
        this.append("logicOperation", logicOperation);
        this.append("value", value);
        this.attribute = attribute;
        this.actionChain = actionChain;
        this.logicOperation = logicOperation;
        this.value = value;
    }

    public ObjectAttribute getAttribute() {
        return attribute;
    }

    public void setActionChain(ObjectActionChain actionChain) {
        this.actionChain = actionChain;
        this.put("actionChain", actionChain);
    }

    public ObjectActionChain getActionChain() {
        return actionChain;
    }

    public void setAttribute(ObjectAttribute attribute) {
        this.attribute = attribute;
        this.put("attribute", attribute);
    }

    public String getLogicOperation() {
        return logicOperation;
    }

    public void setLogicOperation(String logicOperation) {
        this.logicOperation = logicOperation;
        this.put("logicOperation", logicOperation);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
        this.put("value", value);
    }

    @Override
    public String toString() {
        return "Condition:\n" +
                "\t* Attribute: " + this.attribute +
                "\t* ActionChain: " + this.actionChain +
                "\t* Logic Operation: " + this.logicOperation +
                "\t* Value: " + this.value;
    }

}
