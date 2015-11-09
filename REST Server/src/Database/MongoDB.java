package Database;

import Util.FileHandler;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;
import org.bson.Document;
import org.codehaus.jettison.json.JSONObject;

import java.io.IOException;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class MongoDB {

    private final String PATH_PROJECTS = "C:\\Users\\Gila-Ber\\HAPPI\\Projects";
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

    public void add(String collection, String data, String projectName){
        DB db = mongoClient.getDB("hAPPiDB");
        //db.createCollection(collection);
        DBObject json = (DBObject)JSON.parse(data);
        DBCollection coll = db.getCollection(collection);
        coll.insert(json);
        System.out.println(db.getCollection(collection).getName());
        DBCursor cur = coll.find();
        String content = "";
        while(cur.hasNext())
            content += cur.next();

        try {
            FileHandler.writeFile(PATH_PROJECTS + "/BABOON/" + coll.getName() + ".js",content);//TODO : change to projectName
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}