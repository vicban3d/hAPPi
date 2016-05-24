package database;

import logic.ApplicationObject;
import logic.ObjectAction;
import logic.ObjectAttribute;
import logic.ObjectActionChain;
import logic.ActionChain;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.*;

/**
 * Created by Gila on 25/04/2016.
 */
public class ApplicationObjectCodec implements Codec<ApplicationObject> {
    private CodecRegistry codecRegistry;

    public ApplicationObjectCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public ApplicationObject decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String id = reader.readString("id");
        String name = reader.readString("name");

        List<ObjectAttribute> attributes = new ArrayList<>();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            reader.readStartDocument();
            String attrName = reader.readString("name");
            String attrType = reader.readString("type");
            attributes.add(new ObjectAttribute(attrName,attrType));
            reader.readEndDocument();
        }
        reader.readEndArray();

        List<ObjectAction> actions = new ArrayList<>();
        reader.readStartArray();
            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                reader.readStartDocument();
                    String actionName = reader.readString("name");
                    reader.readStartDocument();
                        String attrName = reader.readString("name");
                        String attrType = reader.readString("type");
                        ObjectAttribute operand1 = new ObjectAttribute(attrName,attrType);
                    reader.readEndDocument();
                    String operator = reader.readString("operator");
                    String operandType = reader.readString("operandType");
                    String operand2 = reader.readString("operand2");
                    actions.add(new ObjectAction(actionName,operand1,operator,operandType,operand2));
                reader.readEndDocument();
            }
        reader.readEndArray();

        List<ObjectActionChain> listOfActionChain = new ArrayList<>();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            reader.readStartDocument();
            String actionChainName = reader.readString("name");
            ArrayList<ActionChain> listOfActions = new ArrayList<>();
            reader.readStartArray();
            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                reader.readStartDocument();
                reader.readStartDocument();
                ObjectAttribute operandAttribute = null;
                if (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    String attrName = reader.readString("name");
                    String attrType = reader.readString("type");
                    operandAttribute = new ObjectAttribute(attrName, attrType);
                }
                reader.readEndDocument();

                reader.readStartDocument();
                ObjectAction operandAction = null;
                if (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    String actionName = reader.readString("name");
                    String attrName = reader.readString("name");
                    String attrType = reader.readString("type");
                    ObjectAttribute actionAttr = new ObjectAttribute(attrName, attrType);

                    String operator = reader.readString("operator");
                    String operandType = reader.readString("operandType");
                    String operand2 = reader.readString("operand2");

                    operandAction = new ObjectAction(actionName,actionAttr,operator,operandType,operand2);
                }
                String operator = reader.readString("operator");
                listOfActions.add(new ActionChain(operandAttribute,operandAction, operator));
                reader.readEndDocument();
            }
            reader.readEndArray();
            listOfActionChain.add(new ObjectActionChain(actionChainName, listOfActions));
            reader.readEndDocument();
        }
        reader.readEndArray();

        reader.readEndDocument();
        return new ApplicationObject(id, name, attributes, actions, listOfActionChain);
    }

    @Override
    public void encode(BsonWriter writer, ApplicationObject applicationObject, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("id");
        writer.writeString(applicationObject.getId());

        writer.writeName("name");
        writer.writeString(applicationObject.getName());

        writer.writeStartArray("attributes");
            for (ObjectAttribute attribute : applicationObject.getAttributes()){
                writer.writeStartDocument();
                    writer.writeName("name");
                    writer.writeString(attribute.getName());
                    writer.writeName("type");
                    writer.writeString(attribute.getType());
                writer.writeEndDocument();
            }
        writer.writeEndArray();

        writer.writeStartArray("actions");
            for (ObjectAction action : applicationObject.getActions()){
                writer.writeStartDocument();
                writer.writeName("name");
                writer.writeString(action.getName());
                ObjectAttribute attribute = action.getOperand1();
                writer.writeStartDocument("operand1");
                    writer.writeName("name");
                    writer.writeString(attribute.getName());
                    writer.writeName("type");
                    writer.writeString(attribute.getType());
                writer.writeEndDocument();
                writer.writeName("operator");
                writer.writeString(action.getOperator());
                writer.writeName("operandType");
                writer.writeString(action.getOperandType());
                writer.writeName("operand2");
                writer.writeString(action.getOperand2());
                writer.writeEndDocument();
            }
        writer.writeEndArray();

        writer.writeStartArray("actionChains");
        for (ObjectActionChain objectActionChain : applicationObject.getActionsChain()) {
            writer.writeStartDocument();
            writer.writeName("name");
            writer.writeString(objectActionChain.getName());
            writer.writeStartArray("actions");
            for (ActionChain actionChain : objectActionChain.getActionsChain()) {
                writer.writeStartDocument();
                ObjectAttribute attribute = actionChain.getOperandAttribute();
                if (attribute!=null) {
                    writer.writeStartDocument("operandAttribute");
                    writer.writeName("name");
                    writer.writeString(attribute.getName());
                    writer.writeName("type");
                    writer.writeString(attribute.getType());
                    writer.writeEndDocument();
                }


                ObjectAction action = actionChain.getOperandAction();

                if (action!=null){
                    writer.writeStartDocument("operandAction");
                    writer.writeName("name");
                    writer.writeString(action.getName());

                    writer.writeStartDocument("operand1");
                        writer.writeName("name");
                        writer.writeString(action.getOperand1().getName());
                        writer.writeName("type");
                        writer.writeString(action.getOperand1().getType());
                    writer.writeEndDocument();

                    writer.writeName("operator");
                    writer.writeString(action.getOperator());

                    writer.writeName("operandType");
                    writer.writeString(action.getOperandType());

                    writer.writeName("operand2");
                    writer.writeString(action.getOperand2());
                    writer.writeEndDocument();
                }

                writer.writeName("operator");
                writer.writeString(actionChain.getOperator());
                writer.writeEndDocument();
            }
            writer.writeEndArray();
            writer.writeEndDocument();
        }
        writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<ApplicationObject> getEncoderClass() {
        return ApplicationObject.class;
    }
}
