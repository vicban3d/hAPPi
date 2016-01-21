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
 * This class implements the Database interface and allows communication with MongoDB.
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
    public void addData(Document document) {
        String id = document.getString("id");
        Document doc =  new Document();
        doc.append(id, document);
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        db.getCollection(id).insertOne(doc);
    }

    @Override
    public void removeData(String documentID) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        db.getCollection(documentID).drop();
    }

    @Override
    public void updateData(Document document) {
        removeData(document.getString("id"));
        addData(document);
    }

    @Override
    public Document getData(String documentId) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        Document doc = db.getCollection(documentId).find().first();
        doc = (Document)doc.get(documentId);
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