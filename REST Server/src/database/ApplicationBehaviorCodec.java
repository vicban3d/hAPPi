package database;

import logic.*;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gila on 25/04/2016.
 */
public class ApplicationBehaviorCodec implements Codec<ApplicationBehavior> {
    private CodecRegistry codecRegistry;

    public ApplicationBehaviorCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public ApplicationBehavior decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();

        String id = reader.readString("id");
        String name = reader.readString("name");

        reader.readStartDocument();
        ApplicationObjectCodec objectCodec = new ApplicationObjectCodec(codecRegistry);
        ApplicationObject applicationObject = objectCodec.decode(reader,decoderContext);

        ObjectAttribute operandAttribute = null;
        ObjectActionChain operandAction = null;
        if (reader.readName().equals("operandAttribute")){
            reader.readStartDocument();
            String attrName = reader.readString("name");
            String attrType = reader.readString("type");
            operandAttribute = new ObjectAttribute(attrName, attrType);
            reader.readEndDocument();
        }else{
            reader.readStartDocument();
            String actionName = reader.readString("name");

            List<ActionChain> actionChainList = new ArrayList<>();
            reader.readStartArray();
            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                reader.readStartDocument();
                ObjectAttribute chainOperandAttribute = null;
                ObjectAction chainOperandAction = null;
                if (reader.readName().equals("operandAttribute")) {
                    reader.readStartDocument();
                    String attrName = reader.readString("name");
                    String attrType = reader.readString("type");
                    reader.readEndDocument();
                    chainOperandAttribute = new ObjectAttribute(attrName, attrType);
                } else {
                    reader.readStartDocument();
                    String chainOperandActionName = reader.readString("name");
                    reader.readStartDocument();
                    String attrName = reader.readString("name");
                    String attrType = reader.readString("type");
                    reader.readEndDocument();
                    ObjectAttribute chainOperandActionAttr = new ObjectAttribute(attrName, attrType);

                    String operator = reader.readString("operator");
                    String operandType = reader.readString("operandType");
                    String operand2 = reader.readString("operand2");
                    reader.readEndDocument();
                    chainOperandAction = new ObjectAction(chainOperandActionName, chainOperandActionAttr, operator, operandType, operand2);
                }

                String operator = reader.readString("operator");
                reader.readEndDocument();
                actionChainList.add(new ActionChain(chainOperandAttribute,chainOperandAction,operator));
            }
            reader.readEndArray();
            operandAction = new ObjectActionChain(actionName,actionChainList);
            reader.readEndDocument();
        }
        List<Condition> conds = new ArrayList<>();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            reader.readStartDocument();
            ObjectAttribute condOperandAttribute=null;
            ObjectActionChain condOperandAction = null;
            if (reader.readName().equals("attribute")) {
                reader.readStartDocument();
                String attrName = reader.readString("name");
                String attrType = reader.readString("type");
                reader.readEndDocument();
                condOperandAttribute = new ObjectAttribute(attrName, attrType);
            } else {
                reader.readStartDocument();
                String actionChainName = reader.readString("name");
                ArrayList<ActionChain> listOfActions = new ArrayList<>();
                reader.readStartArray();
                while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    reader.readStartDocument();
                    ObjectAttribute chainOperandAttribute = null;
                    ObjectAction chainOperandAction = null;
                    if (reader.readName().equals("operandAttribute")){
                        reader.readStartDocument();
                        String attrName = reader.readString("name");
                        String attrType = reader.readString("type");
                        chainOperandAttribute = new ObjectAttribute(attrName, attrType);
                        reader.readEndDocument();
                    }else{
                        reader.readStartDocument();
                        String actionName = reader.readString("name");
                        reader.readStartDocument();
                        String attrName = reader.readString("name");
                        String attrType = reader.readString("type");
                        reader.readEndDocument();
                        ObjectAttribute actionAttr = new ObjectAttribute(attrName, attrType);

                        String operator = reader.readString("operator");
                        String operandType = reader.readString("operandType");
                        String operand2 = reader.readString("operand2");

                        chainOperandAction = new ObjectAction(actionName,actionAttr,operator,operandType,operand2);
                        reader.readEndDocument();
                    }

                    String operator = reader.readString("operator");
                    reader.readEndDocument();

                    listOfActions.add(new ActionChain(chainOperandAttribute,chainOperandAction, operator));
                }
                reader.readEndArray();
                reader.readEndDocument();
                condOperandAction = new ObjectActionChain(actionChainName,listOfActions);
            }
            String logicOperand = reader.readString("logicOperation");
            String value = reader.readString("value");

            conds.add(new Condition(condOperandAttribute, condOperandAction, logicOperand, value));
            reader.readEndDocument();
        }
        reader.readEndArray();


        String operator = reader.readString("operator");
        reader.readEndDocument();
        BehaviorAction action = new BehaviorAction(applicationObject, operandAttribute,operandAction, conds, operator);

        reader.readEndDocument();
        return new ApplicationBehavior(id,name,action);
    }

    @Override
    public void encode(BsonWriter writer, ApplicationBehavior applicationBehavior, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("id");
        writer.writeString(applicationBehavior.getId());

        writer.writeName("name");
        writer.writeString(applicationBehavior.getName());

        writer.writeStartDocument("action");
        if (applicationBehavior.getAction()!=null){
            BehaviorAction action = applicationBehavior.getAction();
                writer.writeName("operandObject");
                ApplicationObjectCodec applicationObjectCodec = new ApplicationObjectCodec(codecRegistry);
                encoderContext.encodeWithChildContext(applicationObjectCodec, writer, action.getOperandObject());

                if (action.getOperandAttribute()!=null)
                {
                    writer.writeStartDocument("operandAttribute");
                    writer.writeName("name");
                    writer.writeString(action.getOperandAttribute().getName());
                    writer.writeName("type");
                    writer.writeString(action.getOperandAttribute().getType());
                    writer.writeEndDocument();
                }
                else
                {
                    ObjectActionChain actionChainObj = action.getActionChain();
                    writer.writeStartDocument("actionChain");
                    writer.writeName("name");
                    writer.writeString(actionChainObj.getName());
                    writer.writeStartArray("actions");
                    for (ActionChain actionChain : actionChainObj.getActionsChain()) {
                        writer.writeStartDocument();
                        ObjectAttribute attribute = actionChain.getOperandAttribute();
                        ObjectAction opAction = actionChain.getOperandAction();
                        if (attribute!=null) {
                            writer.writeStartDocument("operandAttribute");
                            writer.writeName("name");
                            writer.writeString(attribute.getName());
                            writer.writeName("type");
                            writer.writeString(attribute.getType());
                            writer.writeEndDocument();
                        }
                        else{
                            writer.writeStartDocument("operandAction");
                            writer.writeName("name");
                            writer.writeString(opAction.getName());

                            writer.writeStartDocument("operand1");
                            writer.writeName("name");
                            writer.writeString(opAction.getOperand1().getName());
                            writer.writeName("type");
                            writer.writeString(opAction.getOperand1().getType());
                            writer.writeEndDocument();

                            writer.writeName("operator");
                            writer.writeString(opAction.getOperator());

                            writer.writeName("operandType");
                            writer.writeString(opAction.getOperandType());

                            writer.writeName("operand2");
                            writer.writeString(opAction.getOperand2());
                            writer.writeEndDocument();
                        }

                        writer.writeName("operator");
                        writer.writeString(actionChain.getOperator());
                        writer.writeEndDocument();
                    }
                    writer.writeEndArray();
                    writer.writeEndDocument();
                }

                writer.writeStartArray("conditions");
                for (Condition cond : action.getConditions()) {
                    writer.writeStartDocument();
                        if (cond.getAttribute()!=null) {
                            writer.writeStartDocument("attribute");
                            writer.writeName("name");
                            writer.writeString(cond.getAttribute().getName());
                            writer.writeName("type");
                            writer.writeString(cond.getAttribute().getType());
                            writer.writeEndDocument();
                        }else{
                            ObjectActionChain objActionChain = cond.getActionChain();
                            writer.writeStartDocument("actionChain");

                                writer.writeName("name");
                                writer.writeString(objActionChain.getName());

                                writer.writeStartArray("actions");
                                for (ActionChain actionChain : objActionChain.getActionsChain()){
                                    writer.writeStartDocument();
                                    if (actionChain.getOperandAttribute()!=null) {
                                        ObjectAttribute operandAttribute = actionChain.getOperandAttribute();
                                        writer.writeStartDocument("operandAttribute");
                                        writer.writeName("name");
                                        writer.writeString(operandAttribute.getName());
                                        writer.writeName("type");
                                        writer.writeString(operandAttribute.getType());
                                        writer.writeEndDocument();
                                    }else{
                                        ObjectAction operandAction = actionChain.getOperandAction();
                                        writer.writeStartDocument("operandAction");
                                        writer.writeName("name");
                                        writer.writeString(operandAction.getName());
                                        ObjectAttribute attribute = operandAction.getOperand1();
                                        writer.writeStartDocument("operand1");
                                        writer.writeName("name");
                                        writer.writeString(operandAction.getOperand1().getName());
                                        writer.writeName("type");
                                        writer.writeString(operandAction.getOperand1().getType());
                                        writer.writeEndDocument();
                                        writer.writeName("operator");
                                        writer.writeString(operandAction.getOperator());
                                        writer.writeName("operandType");
                                        writer.writeString(operandAction.getOperandType());
                                        writer.writeName("operand2");
                                        writer.writeString(operandAction.getOperand2());
                                        writer.writeEndDocument();
                                    }

                                    writer.writeName("operator");
                                    writer.writeString(actionChain.getOperator());
                                    writer.writeEndDocument();
                                }
                                writer.writeEndArray();
                            writer.writeEndDocument();
                        }

                        writer.writeName("logicOperation");
                        writer.writeString(cond.getLogicOperation());

                        writer.writeName("value");
                        writer.writeString(cond.getValue());
                    writer.writeEndDocument();
                }
                writer.writeEndArray();

                writer.writeName("operator");
                writer.writeString(action.getOperator());
            }
        writer.writeEndDocument();
        writer.writeEndDocument();
//        writer.writeEndDocument();
    }

    @Override
    public Class<ApplicationBehavior> getEncoderClass() {
        return ApplicationBehavior.class;
    }
}
