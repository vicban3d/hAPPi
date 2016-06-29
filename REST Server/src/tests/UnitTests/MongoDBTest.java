package tests.unitTests;

import database.MongoDB;
import exceptions.InvalidUserCredentialsException;
import logic.*;
import org.junit.Before;
import org.junit.Test;
import tests.TestUtils;

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
        ObjectAction action = new ObjectAction("action1", attr2, "increase by", "Constant Value", "1");
        actions.add(action);
        // create actionsChain
        ActionChain actionChain1 = new ActionChain(attr1,null,"+");
        ActionChain actionChain2 = new ActionChain(null,action,"DONE");
        ArrayList<ActionChain> actionsChain = new ArrayList<ActionChain>();
        actionsChain.add(actionChain1);
        actionsChain.add(actionChain2);
        ObjectActionChain objectActionChain = new ObjectActionChain("actionChain",actionsChain);
        ArrayList<ObjectActionChain> objectActionsChain = new ArrayList<ObjectActionChain>();
        objectActionsChain.add(objectActionChain );
        // create object
        ApplicationObject object = new ApplicationObject("objectId", "objectName", attributes, actions, objectActionsChain);
        objects.add(object);
        application.setObjects(objects);

        // create behavior
        ArrayList<ApplicationBehavior> behaviors = createApplicationBehaviors(attr1, object);
        application.setBehaviors(behaviors);

        application.setEvents(new ArrayList<ApplicationEvent>());
        application.setName("newTestApp");

        db.updateApplication(application);
        Application updatedApp = db.getApplication("testAppId");

        TestUtils.validateApplicationResults(application, updatedApp);
    }

    @Test
    public void testAddApplication() throws Exception {
        ArrayList<String> platforms = createPlatforms();
        Application application = createApplication("testAppId", "testApp", "testUser", platforms, new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
        db.addApplication(application);
        Application appResult = db.getApplication("testAppId");
        TestUtils.validateApplicationResults(application, appResult);
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
    public void testAddUser() throws Exception, InvalidUserCredentialsException {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        User userFromDB = db.getUser("user1");
        assertEquals(user, userFromDB);
    }

    @Test
    public void testGetUser() throws Exception, InvalidUserCredentialsException {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        User userFromDB = db.getUser("user1");
        assertEquals(user, userFromDB);
    }

    @Test
    public void testIsUserExistWhenUserReallyExist() throws Exception, InvalidUserCredentialsException {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        assertTrue(db.isUserExist("user1"));
    }

    @Test
    public void testIsUserExistWhenUserNotExist() throws Exception {
        assertFalse(db.isUserExist("user"));
    }

    @Test
    public void testIsPasswordRightWhenItsReallyRight() throws Exception, InvalidUserCredentialsException {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        assertTrue(db.isPasswordRight("user1", "pass"));
    }

    @Test
    public void testIsPasswordRightWhenItsWrong() throws Exception, InvalidUserCredentialsException {
        User user = new User("user1", "pass", "user1@gmail.com");
        db.addUser(user);
        assertFalse(db.isPasswordRight("user1", "wrongPass"));
    }

    @Test
    public void testGetAllApplicationsOfUser() throws Exception, InvalidUserCredentialsException {
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
        AppInstance appInstance = TestUtils.createAppInstance();
        db.addApplicationInstance(appInstance);

        AppInstance appInstanceResult = db.getAppInstance("appInstanceId", "appId");

        validateAppInstanceResults(appInstance, appInstanceResult, "Attr1");
    }

    @Test
    public void testGetAppInstance() throws Exception {
        AppInstance appInstance = TestUtils.createAppInstance();
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
            assertNull(db.getAppInstance("testAppInstanceId", "appId"));
    }

    @Test
    public void testIsInstanceExistWhenInstanceExist() throws Exception {
        AppInstance appInstance = TestUtils.createAppInstance();
        db.addApplicationInstance(appInstance);

        assertTrue(db.isInstanceExist(appInstance.getId(), appInstance.getApp_id()));
    }

    @Test
    public void testIsInstanceExistWhenInstanceNotExist() throws Exception {
        AppInstance appInstance = TestUtils.createAppInstance();
        assertFalse(db.isInstanceExist("notExistInstanceId", appInstance.getApp_id()));
    }

    @Test
    public void testUpdateAppInstance() throws Exception {
        AppInstance appInstance = TestUtils.createAppInstance();
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
        TestUtils.validateApplicationResults(app, appFromDB);
    }

    @Test
    public void testClearAll() throws Exception {
        Application app = createApplication("appId", "appName", "user1", new ArrayList<String>(), new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
        db.addApplication(app);

        db.clearAll();
        boolean emptyDB = false;
        try {
            db.getApplication(app.getId());
        } catch (Exception e) {
            emptyDB = true;
        }
        assertTrue(emptyDB);
    }

    private Application createApplication(String appId, String appName, String userName, ArrayList<String> platforms, ArrayList<ApplicationObject> objects, ArrayList<ApplicationBehavior> behaviors, ArrayList<ApplicationEvent> events) {
        Application app = new Application(appId, appName, userName, platforms, objects, behaviors, events);
        return app;
    }

    private void validateAppInstanceResults(AppInstance expectedAppInstance, AppInstance actualAppInstance, String attr) {
        assertEquals(expectedAppInstance.keySet().size(), actualAppInstance.keySet().size());
        assertEquals(expectedAppInstance.get(attr), actualAppInstance.get(attr));
    }

    private ArrayList<ApplicationBehavior> createApplicationBehaviors(ObjectAttribute attr1, ApplicationObject object) {
        ObjectActionChain actionChain = new ObjectActionChain("chain", new ArrayList<ActionChain>());
        BehaviorAction bAction = new BehaviorAction(object, attr1, null, new ArrayList<Condition>(), "SumOfAll");
        ApplicationBehavior behavior = new ApplicationBehavior("behaviorId", "behavior1", bAction);

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