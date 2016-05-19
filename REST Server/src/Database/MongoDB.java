package database;

import exceptions.InvalidUserCredentialsException;
import logic.*;
import utility.Logger;
import utility.Strings;
import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
//import org.codinjutsu.tools.mongo.model.MongoCollection;
import com.mongodb.client.MongoCollection;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by victor on 11/9/2015.
 * This class implements the Database interface and allows communication with MongoDB.
 */
public class MongoDB implements Database {

    public static final String APPLICATION_INSTANCES_TABLE = "ApplicationInstances";
    public static final String ID_KEY = "id";
    /**
     * DB RECOVERY AFTER SHUTDOWN:
     * mongod --dbpath /data/db --repair --repairpath /data/db0
     */

    private MongoClient mongoClient;
    private String username;

    public MongoDB() {
        CodecRegistry codecRegistry = CodecRegistries.fromRegistries(
                CodecRegistries.fromProviders(new UserCodecProvider()),
                CodecRegistries.fromProviders(new ApplicationCodecProvider()),
                CodecRegistries.fromProviders(new AppInstanceCodecProvider()),
                CodecRegistries.fromProviders(new ApplicationObjectCodecProvider()),
                CodecRegistries.fromProviders(new ApplicationBehaviorCodecProvider()),
                MongoClient.getDefaultCodecRegistry());
        MongoClientOptions options = MongoClientOptions.builder()
                .codecRegistry(codecRegistry).build();
        mongoClient = new MongoClient("localhost" + ":" + Strings.DB_PORT, options);
    }

    @Override
    public void connect() throws IOException {
        Runtime.getRuntime().exec("mongod");
        Logger.SEVERE("Database started.");
    }

    @Override
    public void updateApplication(Application document) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        BasicDBObject whereQ = getWhereQuery("id", document.getString("id"));
        getApplicationsTable().findOneAndReplace(whereQ, document);
    }

    @Override
    public void addApplication(Application document) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        getApplicationsTable().insertOne(document);
    }

    @Override
    public void removeApplication(String appID) {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        BasicDBObject whereQ = getWhereQuery("id",appID);
        getApplicationsTable().findOneAndDelete(whereQ);
    }


    public void addUser(User document) throws InvalidUserCredentialsException {
        MongoDatabase db = getMongoDatabase();
        if (isUserExist(document.getUsername())){
            throw new InvalidUserCredentialsException(new Exception("Failed to create user!"));
        }
        getUsersTable().insertOne(document);
    }

    public User getUser(String username){
        MongoDatabase db = getMongoDatabase();
        BasicDBObject whereQuery = getWhereQuery("username",username);
        FindIterable<User> docs = getUsersTable().find(whereQuery);
        return docs.first();
    }

    @Override
    public boolean isUserExist(String username) {
        MongoDatabase db = getMongoDatabase();
        BasicDBObject whereQuery = getWhereQuery("username",username);
        return getUsersTable().count(whereQuery)>0;
    }

    @Override
    public boolean isPasswordRight(String username, String pass) {
        MongoDatabase db = getMongoDatabase();
        BasicDBObject whereQuery = getWhereQuery("username", username);
        whereQuery.put("password", pass);
        return getUsersTable().count(whereQuery)>0;
    }

    private MongoCollection<User> getUsersTable() {
        return getMongoDatabase().getCollection("Users", User.class);
    }

    @Override
    public List<Application> getApplicationOfUser(String username){
        BasicDBObject whereQuery = getWhereQuery("username",username);
        FindIterable<Application> docs = getApplicationsTable().find(whereQuery);
        List<Application> ret = new LinkedList<>();
        for (Application doc : docs){
            ret.add(doc);
        }

        return ret;
    }

    @Override
    public void addApplicationInstance(AppInstance appInstance) {
        MongoDatabase db = getMongoDatabase();
        getAppInstanceTable().insertOne(appInstance);
    }

    @Override
    public AppInstance getAppInstance(String id, String app_id) {
        BasicDBObject whereQuery = getWhereQuery(ID_KEY, id);
        whereQuery.put("app_id",app_id);
        Document appDoc = getAppInstanceTable().find(whereQuery).first();
        return AppInstance.fromDocument(appDoc);
    }

    @Override
    public boolean isInstanceExist(String instanceId, String app_id) {
        BasicDBObject whereQuery = getWhereQuery(ID_KEY,instanceId);
        whereQuery.put("app_id",app_id);
        MongoCollection<AppInstance> appInstanceTable = getAppInstanceTable();
        return appInstanceTable.count(whereQuery)>0;
    }

    @Override
    public MongoDatabase getDB() {
        return getMongoDatabase();
    }

    @Override
    public void clearAll() {
        MongoDatabase db = getMongoDatabase();
        db.drop();
    }

    @Override
    public void updateAppInstance(AppInstance instance) {
        String id = instance.getString(ID_KEY);
        BasicDBObject whereQuery = getWhereQuery(ID_KEY,id);
        Document doc = getAppInstance(id, instance.getApp_id());
        getAppInstanceTable().findOneAndReplace(whereQuery,instance);
    }

    @Override
    public Application getApplication(String id) {
        BasicDBObject whereQuery = getWhereQuery("id", id);
        Document doc = getApplicationsTable().find(whereQuery).first();
        return (Application.fromDocument(doc));
    }

    //////////////// privates


    private MongoCollection<Application> getApplicationsTable() {
        MongoDatabase db = mongoClient.getDatabase(Strings.DB_NAME);
        return db.getCollection("Applications", Application.class);
    }

    private BasicDBObject getWhereQuery(String key, Object value) {
        BasicDBObject whereQuery = new BasicDBObject();
        whereQuery.put(key, value);
        return whereQuery;
    }

    private MongoCollection<AppInstance> getAppInstanceTable(){
        MongoDatabase db = getMongoDatabase();
        return db.getCollection(APPLICATION_INSTANCES_TABLE, AppInstance.class);
    }

    private MongoDatabase getMongoDatabase() {
        return mongoClient.getDatabase(Strings.DB_NAME);
    }
}