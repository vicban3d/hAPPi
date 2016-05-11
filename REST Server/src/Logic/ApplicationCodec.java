package Logic;

import org.bson.BsonReader;
import org.bson.BsonWriter;
import org.bson.codecs.Codec;
import org.bson.codecs.DecoderContext;
import org.bson.codecs.EncoderContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Gila on 18/04/2016.
 */
class ApplicationCodec implements Codec<Application> {
    @Override
    public Application decode(BsonReader reader, DecoderContext decoderContext) {
        reader.readStartDocument();
        String id = reader.readString("id");
        String name = reader.readString("name");
        String username = reader.readString("username");
//        ArrayList<String> platforms = reader.readStartArray();
        ArrayList<ApplicationObject> objects;
        ArrayList<ApplicationBehavior> behaviors;


        return null;
    }

    @Override
    public void encode(BsonWriter bsonWriter, Application application, EncoderContext encoderContext) {

    }

    @Override
    public Class<Application> getEncoderClass() {
        return null;
    }
}
