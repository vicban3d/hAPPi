package database;

import logic.AppInstance;
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
public class AppInstanceCodec implements Codec<AppInstance> {
    private CodecRegistry codecRegistry;

    public AppInstanceCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public AppInstance decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readObjectId();
        String id = reader.readString("id");
        String app_id = reader.readString("app_id");
        Map<String, List<List<String>>> objectInstances = new HashMap<>();
        reader.readStartDocument();
            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                String key = reader.readName();   // read the name "c"

                List<List<String>> objArr = new ArrayList<>();
                Codec<String> docCodec = codecRegistry.get(String.class);
                reader.readStartArray();
                while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                    List<String> objAttr = new ArrayList<>();
                    reader.readStartArray();
                    while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                        objAttr.add(docCodec.decode(reader, decoderContext));
                    }
                    objArr.add(objAttr);
                    reader.readEndArray();
                }
                reader.readEndArray();

                objectInstances.put(key, objArr);
            }
        reader.readEndDocument();

        reader.readEndDocument();

        return new AppInstance(id,app_id,objectInstances);
    }

    @Override
    public void encode(BsonWriter writer, AppInstance appInstance, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("id");
        writer.writeString(appInstance.getId());

        writer.writeName("app_id");
        writer.writeString(appInstance.getApp_id());

        writer.writeStartDocument("object_map");
            for (Map.Entry<String,List<List<String>>> entry : appInstance.getObjectInstances().entrySet()) {
                writer.writeName(entry.getKey());
                writer.writeStartArray();
                for (List<String> list : entry.getValue()) {
                    writer.writeStartArray();
                    for (String str : list){
                        writer.writeString(str);
                    }
                    writer.writeEndArray();
                }
                writer.writeEndArray();
            }
        writer.writeEndDocument();
        writer.writeEndDocument();
    }

    @Override
    public Class<AppInstance> getEncoderClass() {
        return AppInstance.class;
    }
}
