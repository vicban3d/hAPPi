package Tests;

import Database.Database;
import Logic.Facade;
import Logic.hAPPiFacade;
import Server.Server;
import Utility.FileHandler;
import Utility.Strings;
import com.mongodb.DBCollection;
import com.sun.org.apache.xpath.internal.SourceTree;
import junit.framework.TestCase;
import org.codehaus.jettison.json.JSONObject;

import javax.xml.transform.sax.SAXSource;
import java.util.UUID;


/**
 * Created by Almog on 11/01/2016.
 */
public class ServerTest extends TestCase {

    Server server;
    private static Facade facade;
    private String currentApp;
    private Database database;

    @org.junit.Before
    public void setUp() throws Exception {
        server = new Server();
        currentApp = "";
        facade = new hAPPiFacade();
        facade.connectToDatabase();
        server.setFacade(facade);
        database = facade.getDataBase();
    }

    @org.junit.After
    public void tearDown() throws Exception {
        facade.clearDatabase();
    }

    @org.junit.Test
    public void testGetMainPage() throws Exception {
        String mainPage = server.getMainPage();
        String indexPage = FileHandler.readFile(Strings.PATH_WEB_CONTENT + "index.html");
        assertEquals(indexPage,mainPage);
    }

    @org.junit.Test
    public void testCreateApplication() throws Exception {
        //create application successfully
        String appName = UUID.randomUUID().toString();
        String appId = appName;
        JSONObject json = new JSONObject();
        json.put("name",appName);
        json.put("id", appId);
        json.put("platforms","[android,ios]");
        String application = server.createApplication(json.toString());
        assertEquals("The application " + appName + " was created successfully",application);
        DBCollection app = database.getData(appName,"name");
        assertEquals(null,app);

        //create application with existing name
        json = new JSONObject();
        json.put("name",appName);
        json.put("id", appId);
        json.put("platforms","[android,ios]");
        application = server.createApplication(json.toString());
        assertEquals("Error: failed to create application!",application);
    }

    @org.junit.Test
    public void testBuildApplication() throws Exception {

    }

    @org.junit.Test
    public void testCreateObject() throws Exception {

    }

    @org.junit.Test
    public void testRemoveObject() throws Exception {

    }

    @org.junit.Test
    public void testCreateBehavior() throws Exception {

    }

    @org.junit.Test
    public void testRemoveBehavior() throws Exception {

    }

    @org.junit.Test
    public void testRemoveApplication() throws Exception {

    }

    @org.junit.Test
    public void testUpdateApplication() throws Exception {

    }

    @org.junit.Test
    public void testMain() throws Exception {

    }
}