package logic;

import org.bson.Document;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by Almog on 25/04/2016.
 */

@SuppressWarnings("unused")
@XmlRootElement
public class ActionChain extends Document{

    @XmlElement(required = true)
    private ObjectAttribute operandAttribute;
    @XmlElement(required = true)
    private ObjectAction operandAction;
    @XmlElement(required = true)
    private String operator;

    @JsonCreator
    public ActionChain(@JsonProperty("operandAttribute") ObjectAttribute operandAttribute, @JsonProperty("operandAction") ObjectAction operandAction, @JsonProperty("operator") String operator) {

        super();
        this.append("operandAttribute", operandAttribute);
        this.append("operandAction", operandAction);
        this.append("operator", operator);
        this.operandAttribute = operandAttribute;
        this.operandAction = operandAction;
        this.operator = operator;
    }

    public ObjectAttribute getOperandAttribute() {
        return operandAttribute;
    }

    public void setOperandAttribute (ObjectAttribute operandAttribute) {
        this.operandAttribute = operandAttribute;
    }

    public ObjectAction getOperandAction() {
        return operandAction;
    }

    public void setOperandAction (ObjectAction operandAction) {
        this.operandAction = operandAction;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    @Override
    public String toString() {
        return "ActionsChain:\n" +
                "\t* OperandAttribute: " + this.operandAttribute +
                "\t* OperandAction: " + this.operandAction +
                "\t* Operator: " + this.operator;
    }

    public static ActionChain fromDocument(Document data) {
        return new ActionChain((ObjectAttribute)data.get("operandAttribute"),(ObjectAction)data.get("operandAction"),data.getString("operator"));
    }
}