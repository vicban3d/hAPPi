package tests.systemTests;


import database.Database;
import exceptions.CordovaRuntimeException;
import exceptions.InvalidUserCredentialsException;
import org.codehaus.jettison.json.JSONObject;
import tests.TestUtils;
import utility.FileHandler;
import utility.Strings;
import logic.*;
import org.codehaus.jettison.json.JSONException;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Gila-Ber on 04/05/2016.
 */
public class hAPPiFacadeTest {

    hAPPiFacade facade = new hAPPiFacade();
    Database dataBase = facade.getDataBase();
    Application app ;
    User user;

    @Before
    public void setup() throws IOException, InvalidUserCredentialsException {
        facade.connectToDatabase();
        // clear the database
        dataBase.clearAll();

        // clear the applications directory
        FileHandler.deleteFolder(Strings.PATH_APPS);
        FileHandler.createFolder(Strings.PATH_APPS);

        //create user
        user = new User("username", "username", "username@test");
        facade.addUser(user);

        //create app
        ArrayList<String> platforms = new ArrayList<String>();
        platforms.add("android");
        app = new Application("appId", "testApp", user.getUsername(), platforms, new ArrayList<ApplicationObject>(),
                new ArrayList<ApplicationBehavior>(), new ArrayList<ApplicationEvent>());
    }

    @Test
    public void testCreateApplication() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()));
        // validate platforms
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()
                + "\\platforms\\android"));

        // validate application in DB
        Application applicationFromDB = dataBase.getApplication(app.getId());
        TestUtils.validateApplicationResults(app, applicationFromDB);
    }

    @Test
    public void testCreateApplicationDuplicateName() throws Exception {
        facade.createApplication(app);
        boolean duplicateNameError = false;
        try{
            facade.createApplication(app);
        }
        catch(Exception e) {
            duplicateNameError = true;
        }
        assertTrue(duplicateNameError);
    }

    @Test
    public void testUpdateApplication() throws Exception {
        String appOldName = app.getName();
        facade.createApplication(app);
        app.setName("newAppName");
        ArrayList<String> platforms = new ArrayList<String>();
        app.setPlatforms(platforms);
        facade.updateApplication(app, app.getUsername());
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()));
        // validate platforms
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + appOldName));
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()
                + "\\platforms\\android"));
        // validate application in DB
        Application applicationFromDB = dataBase.getApplication(app.getId());
        TestUtils.validateApplicationResults(app, applicationFromDB);
    }

    @Test
    public void testRemoveApplication() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()));
        facade.removeApplication(app.getId(), app.getUsername());
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()));
        // validate app removed from DB
        boolean appExist = true;
        try{
            dataBase.getApplication(app.getId());
        } catch (Exception e){
            appExist = false;
        }
        assertFalse(appExist);
    }

    @Test
    public void testRemovePlatforms() throws Exception {
        facade.createApplication(app);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()
                + "\\platforms\\android"));
        facade.removePlatforms(app.getName(), app.getUsername());
        assertFalse(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + app.getUsername() + "\\" + app.getName()
                + "\\platforms\\android"));
    }

    @Test
    public void testUpdateApplicationWithNameAlreadyExist() throws CordovaRuntimeException, JSONException {
        String appOldName = app.getName();
        facade.createApplication(app);
        app.setName("newAppName");
        app.setId("newAppId");
        facade.createApplication(app);

        boolean updateFailed = false;
        app.setName(appOldName);
        ArrayList<String> platforms = new ArrayList<String>();
        app.setPlatforms(platforms);

        try{
            facade.updateApplication(app, app.getUsername());
        }catch(Exception e) {
            updateFailed = true;
        }
        assertTrue(updateFailed);
    }

    @Test
    public void testRemoveNotExistApplication () {
        boolean appNotExist = false;
        try {
            facade.removeApplication("notExistId", "username");
        } catch (Exception e){
            appNotExist = true;
        }
        assertTrue(appNotExist);
    }

    @Test
    public void testUpdateNotExistApplication () {
        boolean appNotExist = false;
        try {
            facade.updateApplication(app, app.getUsername());
        } catch (Exception e){
            appNotExist = true;
        }
        assertTrue(appNotExist);
    }

    @Test
    public void testCreateObject() throws Exception {
        facade.createApplication(app);

        ApplicationObject object = createObject();
        facade.createObject(app.getId(), app.getUsername(), object);

        Application appFromDB = dataBase.getApplication(app.getId());
        ArrayList<ApplicationObject> objectsFromDB = appFromDB.getObjects();
        assertTrue(objectsFromDB.size() == 1);

        ArrayList<ApplicationObject> objects = new ArrayList<ApplicationObject>();
        objects.add(object);
        app.setObjects(objects);
        TestUtils.validateApplicationResults(app, appFromDB);
    }

    @Test
    public void createObjectToNotExistApplication() {
        boolean appNotExist = false;
        try{
            ApplicationObject object = createObject();
            facade.createObject(app.getId(), app.getUsername(), object);
        } catch(Exception e){
            appNotExist = true;
        }
        assertTrue(appNotExist);
    }

    @Test
    public void testRemoveObject() throws Exception {
        facade.createApplication(app);
        ApplicationObject object = createObject();
        facade.createObject(app.getId(), app.getUsername(), object);

        facade.removeObject(app.getId(), app.getUsername(), object);
        // validate that object removed
        Application appFromDB = dataBase.getApplication(app.getId());
        assertTrue(appFromDB.getObjects().size() == 0);
    }

    @Test
    public void testClearDatabase() throws Exception {
        dataBase.addApplication(app);
        assertNotNull(dataBase.getApplication(app.getId()));
        boolean appExist = true;
        facade.clearDatabase();
        try{
            dataBase.getApplication(app.getId());
        } catch(Exception e){
            appExist = false;
        }
        assertFalse(appExist);
    }

    @Test
    public void testCreateBehavior() throws Exception {
        facade.createApplication(app);
        ApplicationBehavior behavior = createBehavior();
        ArrayList<ApplicationBehavior> behaviors = new ArrayList<ApplicationBehavior>();
        behaviors.add(behavior);
        app.setBehaviors(behaviors);

        facade.createBehavior(app.getId(), app.getUsername(), behavior);
        // validate results
        Application appFromDB = dataBase.getApplication(app.getId());
        assertTrue(appFromDB.getBehaviors().size() == 1);
        TestUtils.validateApplicationResults(app, appFromDB);
    }


    @Test
    public void createBehaviorToNotExistApplication() {
        boolean appNotExist = false;
        try{
            ApplicationBehavior behavior = createBehavior();
            facade.createBehavior(app.getId(), app.getUsername(), behavior);
        } catch(Exception e){
            appNotExist = true;
        }
        assertTrue(appNotExist);
    }

    @Test
    public void testRemoveBehavior() throws Exception {
        // create app with behavior
        ApplicationBehavior behavior = createBehavior();
        ArrayList<ApplicationBehavior> behaviors = new ArrayList<ApplicationBehavior>();
        behaviors.add(behavior);
        app.setBehaviors(behaviors);
        facade.createApplication(app);
        Application application = dataBase.getApplication(app.getId());
        assertTrue(application.getBehaviors().size() == 1);
        // remove behavior
        facade.removeBehavior(app.getId(), app.getUsername(), behavior);
        application = dataBase.getApplication(app.getId());
        assertTrue(application.getBehaviors().size() == 0);
    }

    @Test
    public void testUpdateApplicationObject() throws Exception {
        // create app with objects
        ApplicationObject object = createObject();
        ArrayList<ApplicationObject> objects = new ArrayList<ApplicationObject>();
        objects.add(object);
        app.setObjects(objects);
        facade.createApplication(app);

        // update object
        ArrayList<ObjectAttribute> newAttributes = new ArrayList<ObjectAttribute>();
        ObjectAttribute attr3 = new ObjectAttribute("attr", "Number");
        newAttributes.add(attr3);
        object.setAttributes(newAttributes);
        object.setActions(new ArrayList<ObjectAction>());
        facade.updateApplicationObject(app.getId(), app.getUsername(), object);

        //validation
        Application application = dataBase.getApplication(app.getId());
        ArrayList<ApplicationObject> objectsFromDb = application.getObjects();
        assertTrue(objectsFromDb.size() == 1);
        List<ObjectAttribute> objAttributes = objectsFromDb.get(0).getAttributes();
        assertTrue(objAttributes.size() == 1);
        assertEquals(attr3, objAttributes.get(0));
        assertTrue(objectsFromDb.get(0).getActions().size() == 0);
    }

    @Test
    public void testUpdateApplicationBehavior() throws Exception {
        // create app with behaviors
        ApplicationBehavior behavior = createBehavior();
        ArrayList<ApplicationBehavior> behaviors = new ArrayList<ApplicationBehavior>();
        behaviors.add(behavior);
        app.setBehaviors(behaviors);
        facade.createApplication(app);

        // update behavior
        ArrayList<Condition> conditions = new ArrayList<Condition>();
        Condition cond1 = behavior.getAction().getConditions().get(0);
        Condition cond2 = behavior.getAction().getConditions().get(0);
        cond2.setLogicOperation("Less Than");
        cond2.setValue("5");
        conditions.add(cond1); conditions.add(cond2);

        behavior.getAction().setConditions(conditions);
        facade.updateApplicationBehavior(app.getId(), app.getUsername(), behavior);

        // validation
        Application applicationFromDB = dataBase.getApplication(app.getId());
        ApplicationBehavior applicationBehavior = applicationFromDB.getBehaviors().get(0);
        List<Condition> conditionsFromDB = applicationBehavior.getAction().getConditions();
        assertTrue(conditionsFromDB.size() == 2);
        assertTrue(conditionsFromDB.contains(cond1));
        assertTrue(conditionsFromDB.contains(cond2));
    }

    @Test
    public void testAddUser() throws Exception, InvalidUserCredentialsException {
        User user = new User("user1", "user1", "user1@test");
        facade.addUser(user);
        // validation
        User userFromDB = dataBase.getUser(user.getUsername());
        assertEquals(user, userFromDB);
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "\\" + user.getUsername()));
    }


    @Test
    public void testAddExistingUser() throws Exception, InvalidUserCredentialsException {
        boolean userAlreadyExist = false;
        try{
            facade.addUser(user);
        } catch(InvalidUserCredentialsException e) {
            userAlreadyExist = true;
        }
        assertTrue(userAlreadyExist);
    }

    @Test
    public void testAddObjectInstance() throws Exception {
        AppInstance appInstance = TestUtils.createAppInstance();
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", appInstance.getId());
        jsonObject.put("app_id", appInstance.getApp_id());
        jsonObject.put("objName", "Attr1");
        jsonObject.put("attributesList", appInstance.getObjectInstances().get("Attr1"));

        facade.addObjectInstance(jsonObject);
        assertTrue(dataBase.isInstanceExist(appInstance.getId(), appInstance.getApp_id()));
    }

    @Test
    public void testRemoveObjectInstance() throws Exception {
        AppInstance appInstance = TestUtils.createAppInstance();
        JSONObject jsonObject = createJsonObjFromAppInstance(appInstance.getId(), appInstance.getApp_id(), 0);

        dataBase.addApplicationInstance(appInstance);
        AppInstance appInstanceFromDB = dataBase.getAppInstance(appInstance.getId(), appInstance.getApp_id());
        assertTrue(appInstanceFromDB.getObjectInstances().get("Attr1").size() == 2);

        facade.removeObjectInstance(jsonObject);
        appInstanceFromDB = dataBase.getAppInstance(appInstance.getId(), appInstance.getApp_id());
        assertTrue(appInstanceFromDB.getObjectInstances().get("Attr1").size() == 1);
    }

    @Test
    public void testRemoveNotExistObjectInstance() throws Exception {
        AppInstance appInstance = TestUtils.createAppInstance();
        JSONObject jsonObject = createJsonObjFromAppInstance(appInstance.getId(), appInstance.getApp_id(), 6);

        dataBase.addApplicationInstance(appInstance);
        AppInstance appInstanceFromDB = dataBase.getAppInstance(appInstance.getId(), appInstance.getApp_id());
        assertTrue(appInstanceFromDB.getObjectInstances().get("Attr1").size() == 2);
        // index not exist - instances size not changed
        facade.removeObjectInstance(jsonObject);
        appInstanceFromDB = dataBase.getAppInstance(appInstance.getId(), appInstance.getApp_id());
        assertTrue(appInstanceFromDB.getObjectInstances().get("Attr1").size() == 2);
    }

    @Test
    public void testLogin() throws Exception, InvalidUserCredentialsException {
        facade.createApplication(app);
        List<Application> userApplications = facade.login(user);
        assertTrue(userApplications.size() == 1);
        assertEquals(app, userApplications.get(0));
    }

    @Test
    public void testLoginWithNotExistUser() throws Exception {
        User user = new User("user", "user", "user@test");
        boolean userCredentialsException = false;
        try{
            facade.login(user);
        } catch (InvalidUserCredentialsException e) {
            e.printStackTrace();
            userCredentialsException = true;
        }
        assertTrue(userCredentialsException);
    }

    @Test
    public void testLoginWrongPassword() throws Exception {
        user.setPassword("wrongPass");
        boolean userCredentialsException = false;
        try{
            facade.login(user);
        } catch (InvalidUserCredentialsException e) {
            e.printStackTrace();
            userCredentialsException = true;
        }
        assertTrue(userCredentialsException);
    }

    @Test
    public void testBuildApplication() throws Exception {
        facade.createApplication(app);
        facade.buildApplication(app.getId(), app.getUsername());
        assertTrue(FileHandler.isFileExist(Strings.PATH_APPS + "/" + app.getUsername() + "/" +app.getName() + "/platforms/android/build/outputs/apk/android-debug.apk"));
    }

    private JSONObject createJsonObjFromAppInstance(String id2, String app_id2, int value) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", id2);
        jsonObject.put("app_id", app_id2);
        jsonObject.put("objName", "Attr1");
        jsonObject.put("index", value);
        return jsonObject;
    }

    private ApplicationBehavior createBehavior() {
        ApplicationObject object = createObject();
        ObjectAttribute attribute = object.getAttributes().get(0);
        Condition cond = new Condition(attribute, "Greater Than", "1");
        ObjectActionChain actionChain = new ObjectActionChain("chain", new ArrayList<ActionChain>());
        BehaviorAction bAction = new BehaviorAction(object, attribute, actionChain, Arrays.asList(cond), "SumOfAll");
        ApplicationBehavior behavior = new ApplicationBehavior("behaviorId", "behavior1", bAction);
        return behavior;
    }

    public ApplicationObject createObject() {
        // create attributes
        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ObjectAttribute attr1 = new ObjectAttribute("attr1", "Number");
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
        ApplicationObject object = new ApplicationObject("objectId", "objectName", attributes, actions,objectActionsChain);
        return object;
    }
}