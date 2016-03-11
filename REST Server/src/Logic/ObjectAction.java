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

@SuppressWarnings("unused")
@XmlRootElement
public class ObjectAction extends Document{

    @XmlElement(required = true)
    private String name;
    @XmlElement(required = true)
    private ObjectAttribute operand1;
    @XmlElement(required = true)
    private String operator;
    @XmlElement(required = true)
    private String operand2;

    @JsonCreator
    public ObjectAction(@JsonProperty("name") String name,
                        @JsonProperty("operand1") ObjectAttribute operand1,
                        @JsonProperty("operator") String operator,
                        @JsonProperty("operand2") String operand2) {

        super();
        this.append("name", name);
        this.append("operand1", operand1);
        this.append("operator", operator);
        this.append("operand2", operand2);
        this.name = name;
        this.operand1 = operand1;
        this.operator = operator;
        this.operand2 = operand2;
    }

    public ObjectAttribute getOperand1() {
        return operand1;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOperand1(ObjectAttribute operand1) {
        this.operand1 = operand1;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public String getOperand2() {
        return operand2;
    }

    public void setOperand2(String operand2) {
        this.operand2 = operand2;
    }

    @Override
    public String toString() {
        return "Action:\n" +
                "\t* Name: " + this.name +
                "\t* Operand1: " + this.operand1 +
                "\t* Operator: " + this.operator +
                "\t* Operand2: " + this.operand2;
    }
}
