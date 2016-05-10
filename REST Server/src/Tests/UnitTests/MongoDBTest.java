package Tests.UnitTests;

import Database.MongoDB;
import Logic.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

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

        ArrayList<String> platforms = createPlatforms();
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
        ArrayList<ApplicationBehavior> behaviors = createApplicationBehaviors(attr1, object);
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
        AppInstance appInstance = createAppInstance();
        db.addApplicationInstance(appInstance);

        AppInstance appInstanceResult = db.getAppInstance("appInstanceId", "appId");

        validateAppInstanceResults(appInstance, appInstanceResult, "Attr1");
    }

    @Test
    public void testGetAppInstance() throws Exception {
        AppInstance appInstance = createAppInstance();
        db.addApplicationInstance(appInstance);
        AppInstance appInstanceResult = db.getAppInstance("appInstanceId", "appId");
        validateAppInstanceResults(appInstance, appInstanceResult, "Attr1");
    }

    @Test
    public void testGetNotExistApplication() {
        boolean notExist = false;
        try {
            db.getApplication("testAppId");
        }
        catch(Exception e) {
            notExist = true; // failed to get app because app not exist
        }
        assertTrue(notExist);
    }

    @Test
    public void testGetNotExistAppInstance() {
        boolean notExist = false;
        try {
            db.getAppInstance("testAppInstanceId", "appId");
        }
        catch(Exception e) {
            notExist = true; // failed to get appInstance because appInstance not exist
        }
        assertTrue(notExist);
    }

    @Test
    public void testIsInstanceExist() throws Exception {
        AppInstance appInstance = createAppInstance();
        db.addApplicationInstance(appInstance);

        assertTrue(db.isInstanceExist(appInstance.getId(), appInstance.getApp_id()));
        assertFalse(db.isInstanceExist("notExistInstanceId", appInstance.getApp_id()));
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
        AppInstance appInstance = createAppInstance();
        db.addApplicationInstance(appInstance);
        appInstance = db.getAppInstance("appInstanceId", "appId");
        assertTrue(appInstance.getObjectInstances().keySet().size() == 1);

        appInstance.getObjectInstances().remove("Attr1");
        db.updateAppInstance(appInstance);

        AppInstance updatedAppInstance = db.getAppInstance("appInstanceId", "appId");
        assertTrue(updatedAppInstance.getObjectInstances().keySet().size() == 0);
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

    private AppInstance createAppInstance() {
        Map<String, List<List<String>>> instances = new HashMap<String, List<List<String>>>();
        List<List<String>> instanceValues = new ArrayList<List<String>>();
        instanceValues.add(Arrays.asList("1","2"));
        instanceValues.add(Arrays.asList("3","4"));
        instances.put("Attr1", instanceValues);
        return new AppInstance("appInstanceId","appId", instances);
    }

    private void validateAppInstanceResults(AppInstance expectedAppInstance, AppInstance actualAppInstance, String attr) {
        assertEquals(expectedAppInstance.keySet().size(), actualAppInstance.keySet().size());
        assertEquals(expectedAppInstance.get(attr), actualAppInstance.get(attr));
    }

    private ArrayList<ApplicationBehavior> createApplicationBehaviors(ObjectAttribute attr1, ApplicationObject object) {
        ArrayList<BehaviorAction> behaviorActions = new ArrayList<BehaviorAction>();
        BehaviorAction bAction = new BehaviorAction(object, attr1, new ArrayList<Condition>(), "SumOfAll");
        behaviorActions.add(bAction);
        ApplicationBehavior behavior = new ApplicationBehavior("behaviorId", "behavior1", behaviorActions);

        ArrayList<ApplicationBehavior> behaviors = new ArrayList<ApplicationBehavior>();
        behaviors.add(behavior);
        return behaviors;
    }

    private ArrayList<String> createPlatforms() {
        ArrayList<String> platforms = new ArrayList<String>();
        platforms.add("android");
        platforms.add("ios");
        return platforms;
    }

}