package Tests.UnitTests;

import Database.Database;
import Logic.*;
import Server.*;
import Server.Server;
import Utility.FileHandler;
import Utility.Strings;
import junit.framework.TestCase;
import org.codehaus.jettison.json.JSONObject;

import java.util.ArrayList;
import java.util.UUID;


/**
 * Created by Almog on 11/01/2016.
 *
 */
public class ServerTest extends TestCase {

    private RESTServer server;
    private static Facade facade;
    private Database database;

    @org.junit.Before
    public void setUp() throws Exception {
        server = new Server();
        facade = new hAPPiFacade();
        facade.connectToDatabase();
        database = facade.getDataBase();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        facade.clearDatabase();
    }

    public void testGetMainPage() throws Exception {
        String mainPage = server.getMainPage();
        String indexPage = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "index.html");
        assertEquals(indexPage,mainPage);
    }

    public void testCreateApplication() throws Exception {
        //create application successfully
        String appName = UUID.randomUUID().toString();
        JSONObject json = new JSONObject();
        json.put("name",appName);
        json.put("id", appName);
        json.put("platforms","[android,ios]");
        ArrayList<String> platforms = new ArrayList<>();
        platforms.add("android");
        platforms.add("ios");
        Application app = new Application(appName, appName, platforms, new ArrayList<ApplicationObject>(),new ArrayList<ApplicationBehavior>());
        String application = server.createApplication(app);
        assertEquals("Created " + appName, application);
        Application getApp = Application.fromDocument(database.getData(appName));
        assertEquals(app,getApp);

        //create application with existing name
        json = new JSONObject();
        json.put("name",appName);
        json.put("id", appName);
        json.put("platforms","[android,ios]");
        application = server.createApplication(app);
        assertEquals("Error: failed to create application!",application);
    }

    public void testBuildApplication() throws Exception {

    }

    public void testCreateObject() throws Exception {

    }

    public void testRemoveObject() throws Exception {

    }

    public void testCreateBehavior() throws Exception {

    }

    public void testRemoveBehavior() throws Exception {

    }

    public void testRemoveApplication() throws Exception {

    }

    public void testUpdateApplication() throws Exception {

    }

    public void testMain() throws Exception {

    }
}