package database;

import logic.*;
import org.bson.BsonReader;
import org.bson.BsonType;
import org.bson.BsonWriter;
import org.bson.Document;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gila on 25/04/2016.
 */
public class ApplicationCodec implements Codec<Application> {
    private CodecRegistry codecRegistry;

    public ApplicationCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public Application decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readObjectId();
        String id = reader.readString("id");
        String name = reader.readString("name");
        String username = reader.readString("username");

        Codec<Document> documentCodec = codecRegistry.get(Document.class);
        List<String> platforms = new ArrayList<>();

        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            platforms.add(reader.readString());
        }
        reader.readEndArray();

        ApplicationObjectCodec objectCodec = new ApplicationObjectCodec(codecRegistry);
        List<ApplicationObject> objects = new ArrayList<>();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            objects.add(objectCodec.decode(reader,decoderContext));
        }
        reader.readEndArray();

        List<ApplicationBehavior> behaviors = new ArrayList<>();
        ApplicationBehaviorCodec behaviorCodec = new ApplicationBehaviorCodec(codecRegistry);
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            behaviors.add(behaviorCodec.decode(reader,decoderContext));
        }
        reader.readEndArray();


        reader.readEndDocument();
        return new Application(id,name,username,platforms,objects,behaviors,new ArrayList<ApplicationEvent>());
    }

    @Override
    public void encode(BsonWriter writer, Application application, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("id");
        writer.writeString(application.getId());
        writer.writeName("name");
        writer.writeString(application.getName());
        writer.writeName("username");
        writer.writeString(application.getUsername());

        writer.writeStartArray("platforms");
        ArrayList<String> platforms = application.getPlatforms();
        if(platforms != null) {
            for (String platform : platforms) {
                Codec<String> documentCodec = codecRegistry.get(String.class);
                encoderContext.encodeWithChildContext(documentCodec, writer, platform);
            }
        }
        writer.writeEndArray();

        writer.writeStartArray("objects");

        ArrayList<ApplicationObject> objects = application.getObjects();
        if(objects != null) {
            for (ApplicationObject document : objects) {
                Codec<ApplicationObject> objCodec = codecRegistry.get(ApplicationObject.class);
                encoderContext.encodeWithChildContext(objCodec, writer, document);
            }
        }
        writer.writeEndArray();

        writer.writeStartArray("behaviors");
        ArrayList<ApplicationBehavior> behaviors = application.getBehaviors();
        if(behaviors != null) {
            for (ApplicationBehavior document : behaviors) {
                Codec<ApplicationBehavior> documentCodec = codecRegistry.get(ApplicationBehavior.class);
                encoderContext.encodeWithChildContext(documentCodec, writer, document);
            }
        }

        writer.writeEndArray();

        writer.writeEndDocument();
    }

    @Override
    public Class<Application> getEncoderClass() {
        return Application.class;
    }
}
