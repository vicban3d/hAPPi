package Database;

import Logic.AppInstance;
import Logic.ApplicationBehavior;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by Gila on 25/04/2016.
 */
public class ApplicationBehaviorCodecProvider implements CodecProvider{
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == ApplicationBehavior.class){
            return (Codec<T>) new ApplicationBehaviorCodec(codecRegistry);
        }
        return null;
    }
}
