package Tests.unitTests;

import Database.MongoDB;
import Logic.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class MongoDBTest {

    MongoDB db = new MongoDB();

    @Before
    public void clearDB() throws IOException {
        db.connect();
        db.clearAll();
    }

    @Test
    public void testUpdateApplication() throws Exception {
        Application application = createApplication("testAppId", "testApp", "testUser", null, null, null, null);
        db.addApplication(application);

        ArrayList<String> platforms = new ArrayList<String>();
        platforms.add("android"); platforms.add("ios");
        application.setPlatforms(platforms);

        ArrayList<ApplicationObject> objects = new ArrayList<ApplicationObject>();
        // create attributes
        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ObjectAttribute attr1 = new ObjectAttribute("attr1", "Text");
        ObjectAttribute attr2 = new ObjectAttribute("attr2", "Number");
        attributes.add(attr1); attributes.add(attr2);
        // create actions
        ArrayList<ObjectAction> actions = new ArrayList<ObjectAction>();
        ObjectAction action = new ObjectAction("action1", attr2, "increase by", "1");
        actions.add(action);
        // create object
        ApplicationObject object = new ApplicationObject("objectId", "objectName", attributes, actions);
        objects.add(object);
        application.setObjects(objects);

        application.setName("newTestApp");

        db.updateApplication(application);
        Application updatedApp = db.getApplication("testAppId");
        assertEquals(application.getName(), updatedApp.getName());
        assertEquals(application.getPlatforms(), updatedApp.getPlatforms());
        assertEquals(application.getObjects(), updatedApp.getObjects());
    }

    @Test
    public void testAddApplication() throws Exception {
        Application application = createApplication("testAppId", "testApp", "testUser", new ArrayList<String>(), null, null, null);
        db.addApplication(application);
        Application appResult = db.getApplication("testAppId");
        assertEquals(application.getName(), appResult.getName());
        //TODO assert
    }

    @Test
    public void testRemoveApplication() throws Exception {
        Application application = createApplication("testAppId", "testApp", "testUser", null, null, null, null);
        db.addApplication(application);
        db.removeApplication("testAppId");
        boolean appRemoved = false;
        try {
            db.getApplication("testAppId");
        }
        catch(Exception e) {
            appRemoved = true; // failed to get app because app not exist
        }
        assertTrue(appRemoved);
    }

    @Test
    public void testAddUser() throws Exception {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        User userFromDB = db.getUser("user1");
        assertEquals(user, userFromDB);
    }

    @Test
    public void testGetUser() throws Exception {

    }

    @Test
    public void testIsUserExist() throws Exception {

    }

    @Test
    public void testIsPasswordRight() throws Exception {

    }

    @Test
    public void testGetApplicationOfUser() throws Exception {

    }

    @Test
    public void testAddApplicationInstance() throws Exception {

    }

    @Test
    public void testGetAppInstance() throws Exception {

    }

    @Test
    public void testIsInstanceExist() throws Exception {

    }

    @Test
    public void testGetDB() throws Exception {

    }

    @Test
    public void testClearAll() throws Exception {

    }

    @Test
    public void testUpdateAppInstance() throws Exception {

    }

    @Test
    public void testGetApplication() throws Exception {

    }
    
    private Application createApplication(String appId, String appName, String userName, ArrayList<String> platforms, ArrayList<ApplicationObject> objects, ArrayList<ApplicationBehavior> behaviors, ArrayList<ApplicationEvent> events) {
        Application app = new Application(appId, appName, userName, platforms, objects, behaviors, events);
        return app;
    }
}