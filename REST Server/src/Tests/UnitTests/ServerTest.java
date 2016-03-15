package Tests.UnitTests;

import Database.Database;
import Logic.*;
import Server.*;
import Server.Server;
import Utility.FileHandler;
import Utility.Strings;
import junit.framework.TestCase;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.junit.Test;
import org.w3c.dom.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


/**
 * Created by Almog on 11/01/2016.
 *
 */
public class ServerTest extends TestCase {

    private Server server;
    private static Facade facade;
    private Database database;

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
        //create application successfully
        ArrayList<String> platforms = new ArrayList<>();
        platforms.add("android");
        Application app = new Application("1", "testName", platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
        String result = server.createApplication(app);
        assertEquals("Created testName", result);
        Application getApp = Application.fromDocument(database.getData("1"));
        assertEquals(app,getApp);
    }

    @Test
    public void testCreateApplicationWithExistionName() throws JSONException {
        JSONObject json = new JSONObject();
        json = new JSONObject();
        json.put("name","testName");
        json.put("id", "1");
        json.put("platforms","[android,ios]");
        ArrayList<String> platforms = new ArrayList<>();
        platforms.add("android");
        Application app = new Application("1", "testName", platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
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
        platforms.add("android");
        Application app = new Application("1", "testName", platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
        String application = server.createApplication(app);

        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        attributes.add(attr);
        ArrayList<ObjectAction> actions = new ArrayList<ObjectAction>();
        ObjectAction action = new ObjectAction("action1", attr, "Increase By", "1");

        ApplicationObject obj = new ApplicationObject("1", "obj_test", attributes, actions);
        String result = server.createObject(obj);
        assertEquals("Object obj_test added!", result);

        //Document data = database.getData("1").get("objects").find().first();
        //return new ApplicationObject(.getString("name"), (ArrayList<ObjectAttribute>)data.get("attributes"), (ArrayList<ObjectAction>)data.get("actions"));
        ArrayList<ApplicationObject> objects = (ArrayList<ApplicationObject>)database.getData("1").get("objects");
        assertTrue(objects.size() == 1);
        System.out.println(ApplicationObject.fromDocument(objects.get(0)));
        //assertEquals(obj, ApplicationObject.fromDocument(objects.get(0)));
    }

    public void testRemoveObject() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        platforms.add("android");
        Application app = new Application("1", "testName", platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
        String application = server.createApplication(app);

        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        attributes.add(attr);
        ArrayList<ObjectAction> actions = new ArrayList<ObjectAction>();
        ObjectAction action = new ObjectAction("action1", attr, "Increase By", "1");

        ApplicationObject obj = new ApplicationObject("8", "obj_test", attributes, actions);
        String result = server.createObject(obj);
        assertEquals("Object obj_test added!", result);
        server.removeObject(obj);
        Application getApp = Application.fromDocument(database.getData("1"));
        assertEquals(app,getApp);
    }

    public void testCreateBehavior() throws Exception {

    }

    public void testRemoveBehavior() throws Exception {

    }

    public void testRemoveApplication() throws Exception {

    }

    public void testUpdateApplication() throws Exception {


    }

    public void testUpdateApplicationObject() throws Exception {
        ArrayList<String> platforms = new ArrayList<>();
        platforms.add("android");
        Application app = new Application("1", "testName", platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
        String application = server.createApplication(app);

        ArrayList<ObjectAttribute> attributes = new ArrayList<ObjectAttribute>();
        ObjectAttribute attr = new ObjectAttribute("attr1", "Number");
        attributes.add(attr);
        ArrayList<ObjectAction> actions = new ArrayList<ObjectAction>();
        ObjectAction action = new ObjectAction("action1", attr, "Increase By", "1");

        ApplicationObject obj = new ApplicationObject("1", "obj_test", attributes, actions);
        String result = server.createObject(obj);
        assertEquals("Object obj_test added!", result);

        attributes.clear();
        obj = new ApplicationObject("1", "obj_new", attributes, actions);
        app.addObject(obj);
        result = server.updateObject(obj);
        System.out.println(result);

        Application getApp = Application.fromDocument(database.getData("1"));
       // assertEquals(app,getApp);
    }
}