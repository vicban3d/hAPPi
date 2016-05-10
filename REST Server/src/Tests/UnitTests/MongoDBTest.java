package Tests.UnitTests;

import Database.MongoDB;
import Logic.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

        // create behavior
        ArrayList<BehaviorAction> behaviorActions = new ArrayList<BehaviorAction>();
        BehaviorAction bAction = new BehaviorAction(object, attr1, new ArrayList<Condition>(), "SumOfAll");
        behaviorActions.add(bAction);
        ApplicationBehavior behavior = new ApplicationBehavior("bId", "behavior1", behaviorActions);

        ArrayList<ApplicationBehavior> behaviors = new ArrayList<ApplicationBehavior>();
        behaviors.add(behavior);
        application.setBehaviors(behaviors);

        application.setEvents(new ArrayList<ApplicationEvent>());
        application.setName("newTestApp");

        db.updateApplication(application);
        Application updatedApp = db.getApplication("testAppId");

        validateApplicationResults(application, updatedApp);
    }

    @Test
    public void testAddApplication() throws Exception {
        Application application = createApplication("testAppId", "testApp", "testUser", new ArrayList<String>(), new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
        db.addApplication(application);
        Application appResult = db.getApplication("testAppId");
        validateApplicationResults(application, appResult);
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
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        User userFromDB = db.getUser("user1");
        assertEquals(user, userFromDB);
    }

    @Test
    public void testIsUserExist() throws Exception {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        assertTrue(db.isUserExist("user1"));
        assertFalse(db.isUserExist("user2"));
    }

    @Test
    public void testIsPasswordRight() throws Exception {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        assertTrue(db.isPasswordRight("user1", "pass"));
        assertFalse(db.isPasswordRight("user1", "wrongPass"));
    }

    @Test
    public void testGetApplicationOfUser() throws Exception {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);

        Application application1 = createApplication("appId", "appName", "user1", new ArrayList<String>(), new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
        db.addApplication(application1);

        Application application2 = createApplication("appId2", "appName2", "user1", new ArrayList<String>(), new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
        db.addApplication(application2);

        List<Application> userApplication = db.getApplicationOfUser("user1");
        assertTrue(userApplication.size() == 2);
        for (Application app : userApplication) {
            assertTrue(app.getName().equals("appName") || app.getName().equals("appName2"));
        }
    }

    @Test
    public void testAddApplicationInstance() throws Exception {
        //TODO
    }

    @Test
    public void testGetAppInstance() throws Exception {
        //TODO
    }

    @Test
    public void testIsInstanceExist() throws Exception {
        //TODO
    }

    @Test
    public void testGetDB() throws Exception {
        //TODO
    }

    @Test
    public void testClearAll() throws Exception {
        //TODO
    }

    @Test
    public void testUpdateAppInstance() throws Exception {
        //TODO
    }

    @Test
    public void testGetApplication() throws Exception {
        Application app = createApplication("appId", "appName", "user1", new ArrayList<String>(), new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
        db.addApplication(app);
        Application appFromDB = db.getApplication(app.getId());
        validateApplicationResults(app, appFromDB);
    }
    
    private Application createApplication(String appId, String appName, String userName, ArrayList<String> platforms, ArrayList<ApplicationObject> objects, ArrayList<ApplicationBehavior> behaviors, ArrayList<ApplicationEvent> events) {
        Application app = new Application(appId, appName, userName, platforms, objects, behaviors, events);
        return app;
    }

    private void validateApplicationResults(Application expectedApp, Application actualApp) {
        assertEquals(expectedApp.getName(), actualApp.getName());
        assertEquals(expectedApp.getPlatforms(), actualApp.getPlatforms());
        assertEquals(expectedApp.getObjects(), actualApp.getObjects());
        assertEquals(expectedApp.getBehaviors(), actualApp.getBehaviors());
        assertEquals(expectedApp.getEvents(), actualApp.getEvents());
    }

}