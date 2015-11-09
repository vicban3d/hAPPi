package Database;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class MongoDB {

    public MongoDB() {
    }

    public void connect() {

        try{
            // To connect to mongodb server
            MongoClient mongoClient = new MongoClient("localhost:27017");

            // Now connect to your databases
            MongoDatabase db = mongoClient.getDatabase("hAPPiDB");

            System.out.println("Connect to database successfully");

        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }
}