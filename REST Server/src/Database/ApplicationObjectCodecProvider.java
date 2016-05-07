package Database;

import Logic.AppInstance;
import Logic.ApplicationObject;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by Gila on 25/04/2016.
 */
public class ApplicationObjectCodecProvider implements CodecProvider{
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == ApplicationObject.class){
            return (Codec<T>) new ApplicationObjectCodec(codecRegistry);
        }
        return null;
    }
}
