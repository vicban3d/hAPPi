package Database;

import Utility.Logger;
import Utility.Strings;
import com.mongodb.*;
import com.mongodb.util.JSON;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class MongoDB implements Database {

    /**
     * DB Structure:
     *                                                     ------- Data 1
     *                                                   /          .
     *                                ------- Category 1            .
     *                              /            .       \          .
     *            ------- Project 1              .         ------- Data k...
     *          /            .      \            .
     * Database              .        ------- Category m ...
     *          \            .
     *            ------- Project n ...
     *
     *
     * DB RECOVERY AFTER SHUTDOWN:
     * mongod --dbpath /data/db --repair --repairpath /data/db0
     */

    private MongoClient mongoClient;

    public MongoDB() {
    }

    @Override
    public void connect() throws IOException {
            Runtime.getRuntime().exec("mongod");
            mongoClient = new MongoClient("localhost" + ":" + Strings.DB_PORT);
            Logger.SEVERE("Database started.");
    }

    @Override
    public void addData(String projectId, String projectName, String categoryName, String data){
        //TODO - save the platforms
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
            DBCollection project = db.getCollection(projectId);
            DBCollection category = project.getCollection(categoryName);
            DBObject jsonData = (DBObject)JSON.parse(data);
            category.save(jsonData);
            Logger.INFO("Added data to Database: " + projectId + " -> " + categoryName + " -> " + data);
    }

    @Override
    public void removeData(String appID, String categoryName, String data) {
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        if(categoryName == null)
            db.getCollection(appID).drop();
        else{
            DBCollection project = db.getCollection(appID);
            DBCollection category = project.getCollection(categoryName);
            DBObject jsonData = (DBObject)JSON.parse(data);
            category.remove(jsonData);
        }
        Logger.INFO("Removed data from Database: " + appID + " -> " + categoryName + " -> " + data);
    }

    @Override
    public DBCollection getData(String projectId, String categoryName){
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        DBCollection project = db.getCollection(projectId);
        return project.getCollection(categoryName);
    }

    @Override
    public String getApplicationNameById(String appId) throws JSONException{
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        DBCollection collection = db.getCollection(appId).getCollection("Name");
        DBObject cursor = collection.findOne();
        JSON json = new JSON();
        String result = json.serialize(cursor);
        JSONObject newJson = new JSONObject(result);
        return newJson.getString("name");
    }

    @Override
    public void updateApplication(String appId, LinkedList<JSONObject> elementsToUpdate, LinkedList<String> elements) throws JSONException {
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        DBCollection collection = db.getCollection(appId);
        for (int i=0 ; i< elementsToUpdate.size(); i++){
            BasicDBObject newDocument = new BasicDBObject();
            String key = elements.get(i);
            newDocument.put(key, elementsToUpdate.get(i).getString(key.toLowerCase()));//.append("$set", new BasicDBObject().append(elementsToUpdate.get(i), elementsToUpdate.get(i+1)));
            BasicDBObject searchQuery = new BasicDBObject();
            collection.getCollection(key).update(searchQuery, newDocument);
        }
        printApplicationDataToLog(appId, "Updated");
    }

    @Override
    public void addApplication(JSONObject json) throws JSONException{
        String id = json.getString("id");
        String name = json.getString("name");
        String platforms = json.getString("platforms");
        addData(id, name, "name", new JSONObject("{name:" + name +"}").toString());
        addData(id, name, "platforms", new JSONObject("{platforms:" + platforms +"}").toString());
        printApplicationDataToLog(id, "Added");
    }

    private void printApplicationDataToLog(String appId, String action) throws JSONException {
        DBObject name = getData(appId, "name").find().one();
        JSONObject jsonName = new JSONObject(String.format("%s",name));
        DBObject platforms = getData(appId, "platforms").find().one();
        JSONObject jsonPlatforms = new JSONObject(String.format("%s",platforms));
        Logger.INFO(action + " application in Database: " + appId + " -> " + jsonName.getString("name") + " -> " + jsonPlatforms.getString("platforms"));
    }

    @Override
    public void cleaAll() {
        @SuppressWarnings("deprecation" ) DB db = mongoClient.getDB(Strings.DB_NAME);
        db.dropDatabase();
    }
}