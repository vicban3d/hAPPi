package Database;

import Util.Logger;
import Util.Strings;
import com.mongodb.*;
import com.mongodb.util.JSON;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class MongoDB {

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

    MongoClient mongoClient;

    public MongoDB() {
    }

    /**
     * Connects to Mongo database.
     */
    public void connect() {
        try{
            mongoClient = new MongoClient("localhost" + ":" + Strings.DB_PORT);
            Logger.logSEVERE("Successfully connected to database!");
        }catch(Exception e){
            Logger.logSEVERE("Failed to connect to database!");
            Logger.logSEVERE(e.getMessage());
        }
    }

    /**
     * Adds given data to a given project in the database.
     * @param projectName - name of the project to add data to.
     * @param categoryName - name of the category to add data to.
     * @param data - the content to be added.
     */
    public void addData(String projectName, String categoryName, String data){
        //TODO - should use project ID instead of name.
        DB db = mongoClient.getDB(Strings.DB_NAME);
        DBCollection project = db.getCollection(projectName);
        DBCollection category = project.getCollection(categoryName);
        DBObject jsonData = (DBObject)JSON.parse(data);
        category.save(jsonData);
        Logger.logINFO("Added data to Database: " + projectName + " -> " + categoryName + " -> " + data);
    }

    /**
     * gets the data from a given project.
     * @param projectName - the project to get from.
     * @param data - the data to retrieve.
     * @return a string representation of the needed data.
     */
    public String getData(String projectName, String categoryName, String data){
        DB db = mongoClient.getDB(Strings.DB_NAME);
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