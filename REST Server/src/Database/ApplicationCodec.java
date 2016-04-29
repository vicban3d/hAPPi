package Database;

import Logic.Application;
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

        String id = reader.readString("id");
        String name = reader.readString("name");
        String username = reader.readString("username");

        Codec<Document> historyCodec = codecRegistry.get(Document.class);
        List<Document> history = new ArrayList<>();
        reader.readStartArray();
        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
            history.add(historyCodec.decode(reader, decoderContext));
        }
        reader.readEndArray();
        reader.readEndDocument();

//        User user = new User(username,password,email);

        return null;
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

        writer.writeStartArray("objects");
        for (Document document : application.getObjects()) {
            Codec<Document> documentCodec = codecRegistry.get(Document.class);
            encoderContext.encodeWithChildContext(documentCodec, writer, document);
        }
        writer.writeEndArray();

        writer.writeStartArray("behaviors");
        for (Document document : application.getBehaviors()) {
            Codec<Document> documentCodec = codecRegistry.get(Document.class);
            encoderContext.encodeWithChildContext(documentCodec, writer, document);
        }
        writer.writeEndArray();

        writer.writeStartArray("platforms");
        for (String platform : application.getPlatforms()) {
            Codec<String> documentCodec = codecRegistry.get(String.class);
            encoderContext.encodeWithChildContext(documentCodec, writer, platform);
        }
        writer.writeEndArray();


        writer.writeEndDocument();
    }

    @Override
    public Class<Application> getEncoderClass() {
        return Application.class;
    }
}
