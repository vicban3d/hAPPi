package Database;

import Logic.*;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        List<BehaviorAction> actions = new ArrayList<>();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            reader.readStartDocument();
            ApplicationObjectCodec objectCodec = new ApplicationObjectCodec(codecRegistry);
            ApplicationObject applicationObject = objectCodec.decode(reader,decoderContext);

            reader.readStartDocument();
            String attrName = reader.readString("name");
            String attrType = reader.readString("type");
            ObjectAttribute attribute = new ObjectAttribute(attrName,attrType);
            reader.readEndDocument();

            List<Condition> conds = null;
            if (reader.readBsonType() == BsonType.ARRAY) {
                conds = new ArrayList<>();
                reader.readStartArray();
                while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    reader.readStartDocument();
                    String condAttrName = reader.readString("name");
                    String condAttrType = reader.readString("type");
                    ObjectAttribute condAttribute = new ObjectAttribute(condAttrName, condAttrType);
                    reader.readEndDocument();

                    String logicOperand = reader.readString("logicOperation");
                    String value = reader.readString("value");
                    String andOrOperator = reader.readString("andOrOperator");
                    conds.add(new Condition(condAttribute, logicOperand, value, andOrOperator));
                }
                reader.readEndArray();
            }else{
                reader.readNull();
            }
            String operator = reader.readString("operator");
            reader.readEndDocument();
            actions.add(new BehaviorAction(applicationObject, attribute, conds, operator));
        }
        reader.readEndArray();

        reader.readEndDocument();
        return new ApplicationBehavior(id,name,actions);
    }

    @Override
    public void encode(BsonWriter writer, ApplicationBehavior applicationBehavior, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("id");
        writer.writeString(applicationBehavior.getId());

        writer.writeName("name");
        writer.writeString(applicationBehavior.getName());

        writer.writeStartArray("actions");
        if (applicationBehavior.getActions()!=null){// && applicationBehavior.getActions().size()>0) {
            for (BehaviorAction action : applicationBehavior.getActions()) {
                ApplicationObjectCodec applicationObjectCodec = new ApplicationObjectCodec(codecRegistry);
                encoderContext.encodeWithChildContext(applicationObjectCodec, writer, action.getOperandObject());

                writer.writeStartDocument("operandAttribute");
                writer.writeName("name");
                writer.writeString(action.getOperandAttribute().getName());
                writer.writeName("type");
                writer.writeString(action.getOperandAttribute().getType());
                writer.writeEndDocument();

                writer.writeStartArray("Conditions");
                for (Condition cond : action.getConditions()) {
                    writer.writeStartDocument("attribute");
                    writer.writeName("name");
                    writer.writeString(cond.getAttribute().getName());
                    writer.writeName("type");
                    writer.writeString(cond.getAttribute().getType());
                    writer.writeEndDocument();

                    writer.writeName("logicOperation");
                    writer.writeString(cond.getLogicOperation());

                    writer.writeName("value");
                    writer.writeString(cond.getValue());

                    writer.writeName("andOrOperator");
                    writer.writeString(cond.getandOrOperator());
                }
                writer.writeEndArray();

                writer.writeName("operator");
                writer.writeString(action.getOperator());
            }
        }
//
//        }
        writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<ApplicationBehavior> getEncoderClass() {
        return ApplicationBehavior.class;
    }
}
