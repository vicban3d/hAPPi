package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by victor on 1/16/2016.
 *
 */

@SuppressWarnings("unused")
@XmlRootElement
class BehaviorAction extends Document {
    @XmlAttribute(required = true)
    private ApplicationObject operandObject;
    @XmlAttribute(required = true)
    private ObjectAttribute operandAttribute;
    @XmlAttribute(required = true)
    private String operator;

    @JsonCreator
    public BehaviorAction(@JsonProperty("operandObject") ApplicationObject operandObject,
                          @JsonProperty("operandAttribute") ObjectAttribute operandAttribute,
                          @JsonProperty("operator") String operator) {

        super();
        this.append("operandObject", operandObject);
        this.append("operandAttribute", operandAttribute);
        this.append("operator", operator);
        this.operandObject = operandObject;
        this.operandAttribute = operandAttribute;
        this.operator = operator;
    }

    public ApplicationObject getOperandObject() {
        return operandObject;
    }

    public void setOperandObject(ApplicationObject operandObject) {
        this.operandObject = operandObject;
    }

    public ObjectAttribute getOperandAttribute() {
        return operandAttribute;
    }

    public void setOperandAttribute(ObjectAttribute operandAttribute) {
        this.operandAttribute = operandAttribute;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "Behavior Action:\n" +
                "\t* Object: " + operandObject +
                "\t* Attribute: " + operandAttribute +
                "\t* Operator: " + operator;
    }
}
