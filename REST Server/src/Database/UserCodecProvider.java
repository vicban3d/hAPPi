package Database;

import Database.UserCodec;
import Logic.User;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by Gila on 25/04/2016.
 */
public class UserCodecProvider implements CodecProvider{
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == User.class){
            return (Codec<T>) new UserCodec(codecRegistry);
        }
        return null;
    }
}
