package Database;

import Logic.AppInstance;
import Logic.ApplicationObject;
import Logic.ObjectAction;
import Logic.ObjectAttribute;
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
                    String operand2 = reader.readString("operand2");
                    actions.add(new ObjectAction(actionName,operand1,operator,operand2));
                reader.readEndDocument();
            }
        reader.readEndArray();

        reader.readEndDocument();

        return new ApplicationObject(id,name,attributes,actions);
    }

    @Override
    public void encode(BsonWriter writer, ApplicationObject applicationObject, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("id");
        writer.writeString(applicationObject.getId());

        writer.writeName("name");
        writer.writeString(applicationObject.getName());

        writer.writeStartArray();
            for (ObjectAttribute attribute : applicationObject.getAttributes()){
                writer.writeName("name");
                writer.writeString(attribute.getName());
                writer.writeName("type");
                writer.writeString(attribute.getType());
            }
        writer.writeEndArray();

        writer.writeStartArray();
            for (ObjectAction action : applicationObject.getActions()){
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
                writer.writeName("operand2");
                writer.writeString(action.getOperand2());
            }
        writer.writeEndArray();

        writer.writeEndDocument();
    }

    @Override
    public Class<ApplicationObject> getEncoderClass() {
        return ApplicationObject.class;
    }
}