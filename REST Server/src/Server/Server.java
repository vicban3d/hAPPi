/**
 * Created by victor on 11/6/2015.
 *
 */

package Server;

import Exceptions.CordovaRuntimeException;
import Logic.*;
import Utility.Logger;
import Utility.Strings;
import com.dropbox.core.DbxException;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.spi.resource.Singleton;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

/**
 * hAPPi RESTful Server
 * The server will host at the URL http://localhost on port 80
 */
@Singleton
@Path(Strings.SRV_MAIN)
public class Server implements RESTServer {

    // Silence the built-in jersey logger
    private final static java.util.logging.Logger COM_SUN_JERSEY_LOGGER = java.util.logging.Logger.getLogger("com.sun.jersey");

    static {
        COM_SUN_JERSEY_LOGGER.setLevel(Level.SEVERE);
    }

    private static final Facade facade = new hAPPiFacade();

    @Override
    public Response handleCORS() {
        return Response.ok() //200
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT").build();
    }

    @Override
    public String getMainPage() {
        return facade.getPage("index.html");
    }

    @Override
    public String getResource(String folder, String resource) {
        return facade.getPage(folder + "\\" + resource);
    }

    @Override
    public byte[] getImage(String resource) {
        return facade.getImageAsBytes(resource);
    }

    @Override
    public String createApplication(String data) {
        Application application;
        try {
            JSONObject jsonObject = new JSONObject(data);
            application = createApplicationFromJsonObj(jsonObject);
            Logger.DEBUG("Creating a new application: " + application.getId());
            facade.createApplication(application);
            return "Created " + application.getName();
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed to execute cordova command", e);
            return "Failed to create application!";
        } catch (JSONException e) {
            Logger.ERROR("Incorrect JSON format", e);
            return "Failed to create application!";
        } catch (IOException e) {
            Logger.ERROR("Incorrect JSON format", e);
            return "Failed to create application!";
        }
    }

    @Override
    public String buildApplication(String data) {
        Application application = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            application = createApplicationFromJsonObj(jsonObject);
            return facade.buildApplication(application.getId(), jsonObject.getString("username"));
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed to build application", e);
            return "Error building application " + application.getName();
        } catch (IOException e) {
            Logger.ERROR("Failed to read application files", e);
            return "Error building application";
        } catch (DbxException e) {
            Logger.ERROR("Failed to access Dropbox", e);
            return "Error building application";
        } catch (JSONException e) {
            Logger.ERROR("Failed to build json", e);
            return "Error building application ";
        }
    }

    @Override
    public String createObject(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            ApplicationObject applicationObject = createApplicationObjectFromJson(jsonObject);
            facade.createObject(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationObject);
            return "Object " + applicationObject.getName() + " added!";
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e);
            return "Error: failed to create object!";
        }

    }

    private ApplicationObject createApplicationObjectFromJson(JSONObject data) throws JSONException, IOException {
        String[] names = {"id","name","attributes","actions"};
        JSONObject copy = new JSONObject(data,names);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(copy.toString(), ApplicationObject.class);
    }

    private Application createApplicationFromJsonObj(JSONObject data) throws JSONException, IOException {
        String[] names = {"id","name","username","platforms","objects","behaviors","events"};
        JSONObject copy = new JSONObject(data,names);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(copy.toString(), Application.class);
    }

    private ApplicationEvent createApplicationEventFromJsonObj(JSONObject data) throws JSONException, IOException {
        String[] names = {"id","name","object","attribute","operator","value"};
        JSONObject copy = new JSONObject(data,names);
        ObjectMapper mapper = new ObjectMapper();
        return  mapper.readValue(copy.toString(), ApplicationEvent.class);
    }

    private ApplicationBehavior createApplicationBehaviorFromJson(JSONObject data) throws JSONException, IOException {
        String[] names = {"id","name","action"};
        JSONObject copy = new JSONObject(data,names);
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(copy.toString(), ApplicationBehavior.class);
    }

    @Override
    public String removeObject(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            ApplicationObject applicationObject = createApplicationObjectFromJson(jsonObject);
            facade.removeObject(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationObject);
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e);
            return "Error: failed to remove object!";
        }
        return "Object Removed!";
    }

    @Override
    public String createBehavior(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            ApplicationBehavior applicationBehavior = createApplicationBehaviorFromJson(jsonObject);
            facade.createBehavior(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationBehavior);
            return "Behavior " + applicationBehavior.getName() + " added!";
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e);
            return "Error: failed to create behavior!";
        }
    }

    @Override
    public String removeBehavior(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            ApplicationBehavior applicationBehavior = createApplicationBehaviorFromJson(jsonObject);
            facade.removeBehavior(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationBehavior);
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e);
            return "Error: failed to remove behavior!";
        }
        return "Behavior Removed!";
    }

    @Override
    public String updateBehavior(String data) {
        ApplicationBehavior applicationBehavior;
        try {
            JSONObject jsonObject = new JSONObject(data);
            applicationBehavior = createApplicationBehaviorFromJson(jsonObject);
            facade.updateApplicationBehavior(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationBehavior);
        } catch (Exception e) {
            Logger.ERROR("Failed to update application object!", e);
            return "Error: Failed to update application object!";
        }
        return "The Behavior " + applicationBehavior.getName() + " was updated successfully";
    }

//    @Override
//    public String getSignupPage() {
//        Logger.DEBUG("get signup");
//        return facade.getPage("signup.html");
//    }

    @Override
    public String addUser(User data) {
        Logger.DEBUG("start addUser");
        try {
            facade.addUser(data);
        } catch (Exception e) {
            Logger.ERROR("Failed to add user!", e);
            return "Error: Failed to add user!";
        }
        return "The user " + data.getUsername() + " was created successfully!";
    }

    @Override
    public List<Application> login(User data) {
        Logger.DEBUG("get applications for specific users");
        try {
            return facade.login(data);
        } catch (Exception e) {
            Logger.ERROR("Username or password are wrong", e);
        }
        return null;
    }

    @Override
    public String CreateObjInstance(String createObjInstanceRequest) {
        Logger.DEBUG("start create instance");
        try {
            JSONObject jsonObject = new JSONObject(createObjInstanceRequest);
            facade.addObjectInstance(jsonObject);
        } catch (Exception e) {
            Logger.ERROR("Failed to create instance!", e);
            return "Error: Failed to create instance!";
        }
        Logger.DEBUG("Created Instance successfully");
        return "Created Instance successfully";
    }

    @Override
    public String removeEvent(String data) {
        ApplicationEvent event;
        try {
            JSONObject jsonObject = new JSONObject(data);
            event = createApplicationEventFromJsonObj(jsonObject);
            facade.removeEvent(jsonObject.getString("applicationId"), jsonObject.getString("username"), event);
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e);
            return "Error: failed to remove event!";
        }
        return "Event " + event.getName() + " Removed!";
    }

    @Override
    public String updateEvent(String data) {
        ApplicationEvent event;
        try {
            JSONObject jsonObject = new JSONObject(data);
            event = createApplicationEventFromJsonObj(jsonObject);
            facade.updateApplicationEvent(jsonObject.getString("applicationId"),jsonObject.getString("username"), event);
        } catch (Exception e) {
            Logger.ERROR("Failed to update application event!", e);
            return "Error: Failed to update application event!";
        }
        return "The event " + event.getName() + " was updated successfully";
    }

    @Override
    public String createEvent(String data) {
        ApplicationEvent event;
        try {
            JSONObject jsonObject = new JSONObject(data);
            event = createApplicationEventFromJsonObj(jsonObject);
            facade.createEvent(jsonObject.getString("applicationId"), jsonObject.getString("username"), event);
            return "Event " + event.getName() + " added!";
        }  catch (Exception e) {
            Logger.ERROR("Incorrect data format", e);
            return "Error: failed to create event!";
        }
    }

    @Override
    public String removeApplication(String data) {
        Application application;
        try {
            JSONObject jsonObject = new JSONObject(data);
            application = createApplicationFromJsonObj(jsonObject);
            facade.removeApplication(application.getId(), jsonObject.getString("username"));
        } catch (Exception e) {
            Logger.ERROR("Failed to remove application", e);
            return "Failed to remove application!";
        }

        return "Application "+ application.getName() +" Removed!";
    }

    @Override
    public String updateApplication(String data) {
        Application app;
        try {
            JSONObject jsonObject = new JSONObject(data);
            app = createApplicationFromJsonObj(jsonObject);
            facade.updateApplication(app, jsonObject.getString("username"));
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed execute cordova command", e);
            return "Error: failed to update application!";
        } catch (IOException e) {
            Logger.ERROR("Error reading data", e);
            return "Error: Failed to update application!";
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "The application was updated successfully";
    }

    @Override
    public String updateObject(String data) {
        ApplicationObject applicationObject;
        try {
            JSONObject jsonObject = new JSONObject(data);
            applicationObject = createApplicationObjectFromJson(jsonObject);
            facade.updateApplicationObject(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationObject);
        } catch (Exception e) {
            Logger.ERROR("Failed to update application object!", e);
            return "Error: Failed to update application object!";
        }
        return "The object " + applicationObject.getName() + " was updated successfully";
    }

    @Override
    public String removeObjInstance(String reqParam) {
        String objName;
        try {
            JSONObject jsonObject = new JSONObject(reqParam);
            facade.removeObjectInstance(jsonObject);
            objName = jsonObject.getString("objName");
        } catch (Exception e) {
            Logger.ERROR("Failed to remove object instance!", e);
            return "Error: Failed to remove object instance!";
        }
        Logger.DEBUG("The object " + objName + " instance was removed successfully");
        return "The object " + objName + " instance was removed successfully";
    }

    @Override
    public AppInstance getObjInstance(String reqParam) {
        try {
            JSONObject jsonObject = new JSONObject(reqParam);
            return facade.getObjectInstance(jsonObject);
        } catch (Exception e) {
            Logger.ERROR("Failed to get object instance!", e);
            return null;
        }
    }

    /**
     * Starts the Server.
     */
    private static void start() {
        HttpServer server;
        try {
            server = HttpServerFactory.create(Strings.SRV_HOST + ":" + Strings.SRV_PORT + "/");
            server.start();

        } catch (IOException e) {
            Logger.ERROR("Failed to start server", e);
            return;
        }
        Logger.SEVERE("*** Server running ***");

        System.out.println("Visit: " + Strings.SRV_HOST);
        System.out.println("Applications: " + Strings.PATH_APPS);
        System.out.println("Press ENTER to stop...");

        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException e) {
            Logger.ERROR("Error reading stop signal!", e);
            return;
        }
        Logger.SEVERE("Stopping server...");
        server.stop(0);
        Logger.SEVERE("Server stopped.");
        // TODO - remove in production!!!
//        facade.clearDatabase();// TODO - remove in production!!!
        // TODO - remove in production!!!
    }

//    public static Facade getFacade() {
//        return facade;
//    }

//    public static void setFacade(Facade facade) {
//        Server.facade = facade;
//    }

    /**
     * Starts the server and awaits termination.
     *
     * @param args - null
     */
    public static void main(String[] args) {
        Logger.SEVERE("*** Starting Server ***");
        try {
            facade.connectToDatabase();
            start();
        } catch (IOException e) {
            Logger.ERROR("Failed to connect to database!", e);
        }
    }
}