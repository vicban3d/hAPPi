package database;

import logic.AppInstance;
import org.bson.codecs.Codec;
import org.bson.codecs.configuration.CodecProvider;
import org.bson.codecs.configuration.CodecRegistry;

/**
 * Created by Gila on 25/04/2016.
 */
public class AppInstanceCodecProvider implements CodecProvider{
    @Override
    @SuppressWarnings("unchecked")
    public <T> Codec<T> get(Class<T> aClass, CodecRegistry codecRegistry) {
        if (aClass == AppInstance.class){
            return (Codec<T>) new AppInstanceCodec(codecRegistry);
        }
        return null;
    }
}
