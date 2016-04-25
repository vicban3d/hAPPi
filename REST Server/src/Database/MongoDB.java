package Database;

import Logic.*;
import Utility.Logger;
import Utility.Strings;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
//import org.codinjutsu.tools.mongo.model.MongoCollection;
import com.mongodb.client.MongoCollection;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import javax.print.Doc;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
    private String username;

    public MongoDB() {
    }

    @Override
    public void connect() throws IOException {
        Runtime.getRuntime().exec("mongod");
        mongoClient = new MongoClient("localhost" + ":" + Strings.DB_PORT);
        Logger.SEVERE("Database started.");
    }


    @Override
    public void addData(Document document, String username) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("username", username);
        FindIterable<Document> docs = db.getCollection("UserApplications").find(whereQuery);
        Document first = docs.first();
        if(first == null){
            first = new Document();
            first.append("username", username).append(document.getString("id"), document);
            db.getCollection("UserApplications").insertOne(first);
        }
        else {
            first.append(document.getString("id"), document);
            db.getCollection("UserApplications").findOneAndReplace(whereQuery, first);
        }
    }


    public void addUser(Document document){
        String id = document.getString("username");
        Document doc =  new Document();
        doc.append(id, document);
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        db.getCollection("Users").insertOne(doc);
    }

    @Override
    public void removeData(String documentID, String username) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        BasicDBObject usernameQuery = new BasicDBObject();
        usernameQuery.put("username", username);

        Document userApplications = db.getCollection("UserApplications").find(usernameQuery).first();
        userApplications.remove(documentID);

        db.getCollection("UserApplications").findOneAndReplace(usernameQuery, userApplications);
    }

    @Override
    public void updateData(Document document, String username) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        BasicDBObject usernameQuery = new BasicDBObject();
        usernameQuery.put("username", username);

        Document userApplications = db.getCollection("UserApplications").find(usernameQuery).first();
        userApplications.remove(document.getString("id"));
        userApplications.put(document.getString("id"), document);

        db.getCollection("UserApplications").findOneAndReplace(usernameQuery, userApplications);
    }

    @Override
    public Document getData(String documentId, String username) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        BasicDBObject usernameQuery = new BasicDBObject();
        usernameQuery.put("username", username);
        Document userApp = db.getCollection("UserApplications").find(usernameQuery).first();
        Document doc = (Document) userApp.get(documentId);
        String id = doc.getString("id");
        String name = doc.getString("name");
        @SuppressWarnings("unchecked") ArrayList<String> platforms = (ArrayList<String>)doc.get("platforms");
        @SuppressWarnings("unchecked") ArrayList<ApplicationObject> objects = (ArrayList<ApplicationObject>) doc.get("objects");
        @SuppressWarnings("unchecked") ArrayList<ApplicationBehavior> behaviors = (ArrayList<ApplicationBehavior>) doc.get("behaviors");
        @SuppressWarnings("unchecked") ArrayList<ApplicationEvent> events = (ArrayList<ApplicationEvent>) doc.get("events");
        return new Application(id, name, platforms, objects, behaviors, events);
    }

    public User getUser(String username){
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put("username", username);
        FindIterable<Document> docs = db.getCollection("Users").find(whereQuery);

        return (User)docs.first();
    }

    @Override
    public void clearAll() {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        db.drop();
    }
}