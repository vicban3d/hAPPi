package database;

import logic.User;
import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by Gila on 25/04/2016.
 */
public class UserCodec implements Codec<User> {
    private CodecRegistry codecRegistry;

    public UserCodec(CodecRegistry codecRegistry) {
        this.codecRegistry = codecRegistry;
    }

    @Override
    public User decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        reader.readObjectId();
        String username = reader.readString("username");
        String password = reader.readString("password");
        String email = reader.readString("email");


//        Codec<Document> historyCodec = codecRegistry.get(Document.class);
//        List<Document> history = new ArrayList<>();
//        reader.readStartArray();
//        while (reader.readBsonType() != BsonType.END_OF_DOCUMENT) {
//            history.add(historyCodec.decode(reader, decoderContext));
//        }
//        reader.readEndArray();
        reader.readEndDocument();

        return new User(username,password,email);
    }

    @Override
    public void encode(BsonWriter writer, User user, EncoderContext encoderContext) {
        writer.writeStartDocument();

        writer.writeName("username");
        writer.writeString(user.getUsername());

        writer.writeName("password");
        writer.writeString(user.getPassword());

        writer.writeName("email");
        writer.writeString(user.getEmail());

//        writer.writeStartArray("history");
//        for (Document document : user.getHistory()) {
//            Codec<Document> documentCodec = codecRegistry.get(Document.class);
//            encoderContext.encodeWithChildContext(documentCodec, writer, document);
//        }
//        writer.writeEndArray();
        writer.writeEndDocument();
    }

    @Override
    public Class<User> getEncoderClass() {
        return User.class;
    }
}
