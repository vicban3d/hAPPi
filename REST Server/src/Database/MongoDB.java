package Database;

import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class MongoDB {

    MongoClient mongoClient;

    public MongoDB() {
    }

    public void connect() {

        try{
            // Connect to mongodb server
             mongoClient = new MongoClient("localhost:27017");
             System.out.println("Connect to database successfully");

        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public void add(String collection, String data){
        DB db = mongoClient.getDB("hAPPiDB");
        //db.createCollection(collection);
        DBObject json = (DBObject)JSON.parse(data);
        DBCollection coll = db.getCollection(collection);
        coll.insert(json);
        System.out.println(db.getCollection(collection).getName());
        System.out.println(db.getCollection(collection).find(json));
        DBCursor cur = coll.find();
        while(cur.hasNext()){
            System.out.println(cur.next());
        }
    }


}