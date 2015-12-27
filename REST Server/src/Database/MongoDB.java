package Database;

import Exceptions.DatabaseConnectionErrorException;
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
     *
     *
     * DB RECOVERY AFTER SHUTDOWN:
     * mongod --dbpath /data/db --repair --repairpath /data/db0
     */

    private MongoClient mongoClient;

    public MongoDB() {
    }

    @Override
    public void connect() throws DatabaseConnectionErrorException {
        try {
            Runtime.getRuntime().exec("mongod");
        } catch (IOException e) {
            Logger.ERROR("Failed to start database!", e.getMessage());
            throw new DatabaseConnectionErrorException("Failed to start database");
        }
        try{
            mongoClient = new MongoClient("localhost" + ":" + Strings.DB_PORT);
        }catch(Exception e){
            Logger.ERROR("Failed to connect to database!", e.getMessage());
        }
        Logger.SEVERE("Database started.");
    }

    @Override
    public void addData(String projectName, String categoryName, String data){
        //TODO - should use project ID instead of name.
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        DBCollection project = db.getCollection(projectName);
        DBCollection category = project.getCollection(categoryName);
        DBObject jsonData = (DBObject)JSON.parse(data);
//        category.save(jsonData);
        Logger.INFO("Added data to Database: " + projectName + " -> " + categoryName + " -> " + data);
    }

    @Override
    public void removeData(String projectName, String categoryName, String data) {
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        if(categoryName == null)
            db.getCollection(projectName).drop();
        else{
            DBCollection project = db.getCollection(projectName);
            DBCollection category = project.getCollection(categoryName);
            DBObject jsonData = (DBObject)JSON.parse(data);
            category.remove(jsonData);
        }
        Logger.INFO("Removed data from Database: " + projectName + " -> " + categoryName + " -> " + data);
    }

    @Override
    public DBCollection getData(String projectName, String categoryName){
        @SuppressWarnings("deprecation") DB db = mongoClient.getDB(Strings.DB_NAME);
        DBCollection project = db.getCollection(projectName);
        return project.getCollection(categoryName);
    }

    @Override
    public void cleaAll() {
        @SuppressWarnings("deprecation" ) DB db = mongoClient.getDB(Strings.DB_NAME);
        db.dropDatabase();
    }
}