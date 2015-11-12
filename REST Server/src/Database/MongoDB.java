package Database;

import Utility.Logger;
import Utility.Strings;
import com.mongodb.*;
import com.mongodb.util.JSON;
import java.io.IOException;

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
     */

    private MongoClient mongoClient;

    public MongoDB() {
    }

    @Override
    public void connect() {
        try {
            Runtime.getRuntime().exec("mongod");
        } catch (IOException e) {
            Logger.logERROR("Failed to start database!", e.getMessage());
            return;
        }
        try{
            mongoClient = new MongoClient("localhost" + ":" + Strings.DB_PORT);
        }catch(Exception e){
            Logger.logERROR("Failed to connect to database!", e.getMessage());
        }
        Logger.logSEVERE("Database started.");
    }

    @Override
    public void addData(String projectName, String categoryName, String data){
        //TODO - should use project ID instead of name.
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        DBCollection project = db.getCollection(projectName);
        DBCollection category = project.getCollection(categoryName);
        DBObject jsonData = (DBObject)JSON.parse(data);
        category.save(jsonData);
        Logger.logINFO("Added data to Database: " + projectName + " -> " + categoryName + " -> " + data);
    }

    @Override
    public String getData(String projectName, String categoryName, String data){
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        DBCollection project = db.getCollection(projectName);
        DBCollection category = project.getCollection(categoryName);
        for (DBObject c : category.find()) {
            if (c.get("name").equals(data)){
                return c.toString();
            }
        }
        return null;
    }
}