package Database;

import com.mongodb.DBCollection;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.LinkedList;

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
     * Adds given data to a given project in the database.
     * @param id
     * @param data - the content to be added.
     */
    void addData(String id, String data);

    void removeData(String currentAppName, String categoryName, String data);

    DBCollection getData(String projectId);

    /**
     * Clear the whole DB
     * WARNING! DO NOT USE LIGHTLY!
     */
    void cleaAll();
    String getApplicationNameById(String collectionId) throws JSONException;
    void updateApplication(String appId, LinkedList<JSONObject> elementsToUpdate, LinkedList<String> elements) throws JSONException;

    void addApplication(JSONObject json) throws JSONException;
}
