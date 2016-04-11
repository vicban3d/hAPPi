package Tests.UnitTests;

import Database.Database;
import Logic.*;
import Server.Server;
import Utility.FileHandler;
import Utility.Strings;
import junit.framework.TestCase;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import java.util.ArrayList;


/**
 * Created by Almog on 11/01/2016.
 *
 */
public class ServerTest extends TestCase {

    private Server server;
    private static Facade facade;
    private Database database;
    private User user;

    @org.junit.Before
    public void setUp() throws Exception {
        server = new Server();
        facade = new hAPPiFacade();
        facade.connectToDatabase();
        database = facade.getDataBase();
        facade.clearDatabase();
        server.setFacade(facade);
        FileHandler.deleteFolder(Strings.PATH_APPS);
        FileHandler.createFolder(Strings.PATH_APPS);
        user = new User("gil","11","1111");
    }

    @org.junit.After
    public void tearDown() throws Exception {

    }

    @Test
    public void testGetMainPage() throws Exception {
        String mainPage = server.getMainPage();
        String indexPage = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "index.html");
        assertEquals(indexPage,mainPage);
    }

    @Test
    public void testCreateApplication() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        Application app = createApp(platforms);

        Application getApp = Application.fromDocument(database.getData("1", user.getUsername()));
        assertEquals(app,getApp);
    }

    @Test
    public void testCreateApplicationWithExistionName() throws JSONException {
        JSONObject json = new JSONObject();
        json = new JSONObject();
        json.put("name","testName");
        json.put("id", "1");
        json.put("user",user);
        json.put("platforms","[android,ios]");
        ArrayList<String> platforms = new ArrayList<>();
        platforms.add("android");
        Application app = new Application("1", "testName", user, platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
        String result = server.createApplication(app);
        assertEquals("Created testName", result);
        result = server.createApplication(app);
        assertEquals("Error: failed to create application!",result);
    }

    public void testBuildApplication() throws Exception {

    }

    @Test
    public void testCreateObject() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ArrayList<ObjectAction> actions = new ArrayList<ObjectAction>();
        ApplicationObject obj = new ApplicationObject("1", "obj_test", attributes, actions);

        createApp(platforms);
        createAttributeAndAction(attributes, actions);
        String result = server.createObject(obj);
        assertEquals("Object obj_test added!", result);
        //Document data = database.getData("1").get("objects").find().first();
        //return new ApplicationObject(.getString("name"), (ArrayList<ObjectAttribute>)data.get("attributes"), (ArrayList<ObjectAction>)data.get("actions"));
        ArrayList<ApplicationObject> objects = (ArrayList<ApplicationObject>)database.getData("1", user.getUsername()).get("objects");
        assertTrue(objects.size() == 1);
        System.out.println(ApplicationObject.fromDocument(objects.get(0)));
        //assertEquals(obj, ApplicationObject.fromDocument(objects.get(0)));
    }

    public void testRemoveObject() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ArrayList<ObjectAction> actions = new ArrayList<ObjectAction>();
        ApplicationObject obj = new ApplicationObject("8", "obj_test", attributes, actions);

        Application app = createApp(platforms);
        createAttributeAndAction(attributes, actions);
        String result = server.createObject(obj);
        assertEquals("Object obj_test added!", result);
        server.removeObject(obj);
        Application getApp = Application.fromDocument(database.getData("1", user.getUsername()));
        assertEquals(app,getApp);
    }

    public void testRemoveBehavior() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ArrayList<ObjectAction> actionsObject = new ArrayList<ObjectAction>();
        ApplicationObject obj = new ApplicationObject("8", "obj_test", attributes, actionsObject);
        ArrayList<BehaviorAction> actionsBehavior = new ArrayList<BehaviorAction>();
        ObjectAttribute attribute = new ObjectAttribute("attr2", "Number");
        BehaviorAction action1 = new BehaviorAction(obj,attribute,null,"operator");
        ApplicationBehavior behavior = new ApplicationBehavior("6","behavior_test",actionsBehavior);

        Application app = createApp(platforms);
        createAttributeAndAction(attributes, actionsObject);
        actionsBehavior.add(action1);
        server.createBehavior(behavior);
        server.removeBehavior(behavior);
        Application getApp = Application.fromDocument(database.getData("1", user.getUsername()));
        assertEquals(app,getApp);
    }

    public void testCreateBehavior() throws Exception {

    }

    public void testRemoveApplication() throws Exception {

    }

    public void testUpdateApplication() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        Application app = createApp(platforms);
        platforms.remove("android");
        platforms.add("ios");
        app = new Application("1", "new_testName", user, platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
        server.updateApplication(app);

        Application getApp = Application.fromDocument(database.getData("1", user.getUsername()));
        assertEquals(app,getApp);
    }

    public void testUpdateApplicationBehavior() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ArrayList<ObjectAction> actionsObject = new ArrayList<ObjectAction>();
        ApplicationObject obj = new ApplicationObject("8", "obj_test", attributes, actionsObject);
        ArrayList<BehaviorAction> actionsBehavior = new ArrayList<BehaviorAction>();
        ObjectAttribute attribute = new ObjectAttribute("attr2", "Number");
        BehaviorAction action1 = new BehaviorAction(obj,attribute,null,"operator");
        ApplicationBehavior behavior = new ApplicationBehavior("6","behavior_test",actionsBehavior);
        BehaviorAction action2 = new BehaviorAction(obj,attribute,null,"operator2");
        behavior = new ApplicationBehavior("6","behavior_new",actionsBehavior);

        Application app = createApp(platforms);
        createAttributeAndAction(attributes, actionsObject);
        actionsBehavior.add(action1);
        String result = server.createBehavior(behavior);
        assertEquals("Behavior behavior_test added!", result);
        actionsBehavior.clear();
        actionsBehavior.add(action2);
        app.addBehavior(behavior);
        server.updateBehavior(behavior);

        Application getApp = Application.fromDocument(database.getData("1", user.getUsername()));
        assertEquals(app,getApp);
    }

    public void testUpdateApplicationObject() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        ArrayList<ObjectAttribute> attributes1 = new ArrayList<ObjectAttribute>();
        ArrayList<ObjectAttribute> attributes2 = new ArrayList<ObjectAttribute>();
        ObjectAttribute objectAttribute1 = new ObjectAttribute("name1", "type1");
        ObjectAttribute objectAttribute2 = new ObjectAttribute("name2", "type2");
        attributes1.add(objectAttribute1);
        ArrayList<ObjectAction> actions = new ArrayList<ObjectAction>();
        ApplicationObject obj1 = new ApplicationObject("1", "obj_test", attributes1, actions);
        attributes2.add(objectAttribute2);
        ApplicationObject obj2 = new ApplicationObject("1", "obj_new", attributes2, actions);

        Application app = createApp(platforms);
        createAttributeAndAction(attributes1, actions);
        String result = server.createObject(obj1);
        assertEquals("Object obj_test added!", result);
        attributes1.clear();
        app.addObject(obj2);
        server.updateObject(obj2);
        Application getApp = Application.fromDocument(database.getData("1", user.getUsername()));
        // assertEquals(app,getApp);
    }

    private void createAttributeAndAction(ArrayList<ObjectAttribute> attributes, ArrayList<ObjectAction> actions) {
        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        attributes.add(attr);
        ObjectAction action = new ObjectAction("action1", attr, "Increase By", "1");
        actions.add(action);
    }

    private Application createApp(ArrayList<String> platforms) {
        platforms.add("android");
        Application app = new Application("1", "testName",user, platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
        server.createApplication(app);
        return app;
    }
}