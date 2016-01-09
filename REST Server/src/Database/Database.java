package Database;

import com.mongodb.DBCollection;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.LinkedList;
import java.util.List;

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
     * @param projectName - name of the project to add data to.
     * @param categoryName - name of the category to add data to.
     * @param data - the content to be added.
     */
    void addData(String id, String projectName, String categoryName, String data);

    void removeData(String currentAppName, String categoryName, String data);

    /**
     * gets the data from a given project.
     * @param projectName - the project to get from.
     * @return a string representation of the needed data.
     */
    DBCollection getData(String projectName, String categoryName);

    /**
     * Clear the whole DB
     * WARNING! DO NOT USE LIGHTLY!
     */
    void cleaAll();
    String getApplicationNameById(String collectionId) throws JSONException;
    void updateApplication(String appId, LinkedList<JSONObject> elementsToUpdate, LinkedList<String> elements) throws JSONException;

    void addApplication(JSONObject json) throws JSONException;
}
