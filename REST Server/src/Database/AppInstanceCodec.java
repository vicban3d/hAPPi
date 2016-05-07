package Database;

import Logic.AppInstance;
import Logic.User;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Document;
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
        Map<String, List<String>> objectInstances = new HashMap<>();
        reader.readStartDocument();
            String key = reader.readName();   // read the name "c"

            Codec<String> docCodec = codecRegistry.get(String.class);
            List<String> objArr = new ArrayList<>();
            reader.readStartArray();
            while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
                objArr.add(docCodec.decode(reader, decoderContext));
            }
            reader.readEndArray();

            objectInstances.put(key,objArr);

        reader.readEndDocument();

        reader.readEndDocument();

        AppInstance appInstance= new AppInstance(id,app_id,objectInstances);

        return appInstance;
    }

    @Override
    public void encode(BsonWriter writer, AppInstance appInstance, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("id");
        writer.writeString(appInstance.getId());

        writer.writeName("app_id");
        writer.writeString(appInstance.getApp_id());

        writer.writeStartDocument("object_map");
            for (Map.Entry<String,List<String>> entry : appInstance.getObjectInstances().entrySet()){
                writer.writeName(entry.getKey());
                writer.writeStartArray();
                    for (String str : entry.getValue()){
                        writer.writeString(str);
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
