package Logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by victor on 1/16/2016.
 *
 */

@SuppressWarnings("unused")
@XmlRootElement
public class BehaviorAction extends Document {

    @XmlAttribute(required = true)
    private ApplicationObject operandObject;
    @XmlAttribute(required = true)
    private ObjectAttribute operandAttribute;
    @XmlAttribute(required = true)
    private String operator;
    @XmlAttribute(required = true)
    private List<Condition> conditions;

    @JsonCreator
    public BehaviorAction(@JsonProperty("operandObject") ApplicationObject operandObject,
                          @JsonProperty("operandAttribute") ObjectAttribute operandAttribute,
                          @JsonProperty("Conditions") List<Condition> conditions,
                          @JsonProperty("operator") String operator) {

        super();
        this.append("operandObject", operandObject);
        this.append("operandAttribute", operandAttribute);
        this.append("Conditions", conditions);
        this.append("operator", operator);
        this.operandObject = operandObject;
        this.operandAttribute = operandAttribute;
        this.operator = operator;
        this.conditions = conditions;
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

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public String toString() {
        return "Behavior Action:\n" +
                "\t* Object: " + operandObject +
                "\t* Attribute: " + operandAttribute +
                "\t* Conditions: " + conditions +
                "\t* Operator: " + operator;
    }

}
