package Database;

import Logic.Application;

import java.io.IOException;

/**
 * Created by victor on 11/12/2015.
 *
 */
public interface Database {

    /**
     * Connects to Mongo database.
     */
    void connect() throws IOException;

    /**
     * Clear the whole DB
     * WARNING! DO NOT USE LIGHTLY!
     */
    void clearAll();

    void removeData(String appId);

//    String getApplicationNameById(String collectionId) throws JSONException;

    void updateData(Application application);

    Application getData(String appId);

    void addData(Application application);
}
