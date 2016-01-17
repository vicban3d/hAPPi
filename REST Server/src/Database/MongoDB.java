package Database;

import Logic.Application;
import Logic.ApplicationBehavior;
import Logic.ApplicationObject;
import Utility.Logger;
import Utility.Strings;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class MongoDB implements Database {

    /**
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
    public void addData(Application application) {
        String id = application.getId();
        Document doc =  new Document();
//        doc.append("id", application.getId());
//        doc.append("name", application.getName());
//        doc.append("platforms", application.getPlatforms());
//        doc.append("objects", application.getObjects());
//        doc.append("behaviors", application.getBehaviors());
        doc.append(id, application);
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        db.getCollection(id).insertOne(doc);
    }

    @Override
    public void removeData(String appId) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        db.getCollection(appId).drop();
    }

    @Override
    public void updateData(Application application) {
        removeData(application.getId());
        addData(application);
    }

    @Override
    public Application getData(String appId) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        Document doc = db.getCollection(appId).find().first();
        doc = (Document)doc.get(appId);
        String id = doc.getString("id");
        String name = doc.getString("name");
        @SuppressWarnings("unchecked") ArrayList<String> platforms = (ArrayList<String>)doc.get("platforms");
        @SuppressWarnings("unchecked") ArrayList<ApplicationObject> objects = (ArrayList<ApplicationObject>) doc.get("objects");
        @SuppressWarnings("unchecked") ArrayList<ApplicationBehavior> behaviors = (ArrayList<ApplicationBehavior>) doc.get("behaviors");
        return new Application(id, name, platforms, objects, behaviors);
    }


    @Override
    public void clearAll() {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        db.drop();
    }
}