/**
 * Created by victor on 11/6/2015.
 *
 */

package server;

import com.sun.jersey.spi.resource.Singleton;
import exceptions.CordovaRuntimeException;
import exceptions.InvalidUserCredentialsException;
import logic.*;
import org.codehaus.jettison.json.JSONArray;
import utility.Logger;
import utility.Strings;
import com.dropbox.core.DbxException;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
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
 * hAPPi RESTful server
 * The server will host at the URL http://132.72.23.136 on port 80
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

    private Response respondOK(String data) {
        //Logger.DEBUG(data);
        return Response.ok() //200
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .entity(data)
                .build();
    }

    private Response respondERROR(String data, Throwable e) {
        Logger.ERROR(data, e);
        return Response.serverError()//500
                .header("Access-Control-Allow-Origin", "*")
                .header("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT")
                .entity(data)
                .build();
    }

    @Override
    public Response getMainPage() {
        try {
            return respondOK(facade.getPage("index.html"));
        } catch (IOException e) {
            return respondERROR("Failed to retrieve main page!", e);
        }
    }

    @Override
    public Response getResource(String folder, String resource) {
        try {
            return respondOK(facade.getPage(folder + "\\" + resource));
        } catch (IOException e) {
            return respondERROR("Failed to retrieve resource " + resource + "!", e);
        }
    }

    @Override
    public byte[] getImage(String resource) {
        return facade.getImageAsBytes(resource);
    }

    @Override
    public Response createApplication(String data) {
        Application application;
        try {
            JSONObject jsonObject = new JSONObject(data);
            application = createApplicationFromJsonObj(jsonObject);
            Logger.DEBUG("Creating a new application: " + application.getId());
            facade.createApplication(application);
            return respondOK("Created " + application.getName());
        } catch (CordovaRuntimeException e) {
            return respondERROR("Failed to create application!", e);
        } catch (JSONException e) {
            return respondERROR("Failed to create application!", e);
        } catch (IOException e) {
            return respondERROR("Failed to create application!", e);
        }
    }

    @Override
    public Response buildApplication(String data) {
        Application application = null;
        try {
            JSONObject jsonObject = new JSONObject(data);
            application = createApplicationFromJsonObj(jsonObject);
            return respondOK(facade.buildApplication(application.getId(), jsonObject.getString("username")));
        } catch (CordovaRuntimeException e) {
            return respondERROR("Error building application " + application.getName(), e);
        } catch (IOException e) {
            return respondERROR("Error building application", e);
        } catch (DbxException e) {
            return respondERROR("Error building application", e);
        } catch (JSONException e) {
            return respondERROR("Error building application ", e);
        }
    }

    @Override
    public Response createObject(String data) {
        try {
            System.out.println("Create Object: " + data);
            JSONObject jsonObject = new JSONObject(data);
            ApplicationObject applicationObject = createApplicationObjectFromJson(jsonObject);
            facade.createObject(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationObject);
            return respondOK("Object " + applicationObject.getName() + " added!");
        } catch (JSONException e) {
            return respondERROR("Error: failed to create object!", e);
        } catch (IOException e) {
            return respondERROR("Error: failed to create object!", e);
        }
    }

    @Override
    public Response removeObject(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            ApplicationObject applicationObject = createApplicationObjectFromJson(jsonObject);
            facade.removeObject(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationObject);
        } catch (JSONException e) {
            return respondERROR("Failed to remove object!", e);
        } catch (IOException e) {
            return respondERROR("Failed to remove object!", e);
        }
        return respondOK("Object Removed!");
    }

    @Override
    public Response createBehavior(String data) {
        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(data);
            ApplicationBehavior applicationBehavior = createApplicationBehaviorFromJson(jsonObject);
            facade.createBehavior(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationBehavior);
            return respondOK("Behavior " + applicationBehavior.getName() + " added!");
        } catch (JSONException e) {
            return respondERROR("Failed to create behavior!", e);
        } catch (IOException e) {
            return respondERROR("Failed to create behavior!", e);
        }
    }

    @Override
    public Response removeBehavior(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            ApplicationBehavior applicationBehavior = createApplicationBehaviorFromJson(jsonObject);
            facade.removeBehavior(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationBehavior);
        } catch (JSONException e) {
            return respondERROR("Failed to remove behavior!", e);
        } catch (IOException e) {
            return respondERROR("Failed to remove behavior!", e);
        }


        return respondOK("Behavior Removed!");
    }

    @Override
    public Response updateBehavior(String data) {
        ApplicationBehavior applicationBehavior;
        try {
            JSONObject jsonObject = new JSONObject(data);
            applicationBehavior = createApplicationBehaviorFromJson(jsonObject);
            facade.updateApplicationBehavior(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationBehavior);
        } catch (JSONException e) {
            return respondERROR("Failed to update application object!", e);
        } catch (IOException e) {
            return respondERROR("Failed to update application object!", e);
        }
        return respondOK("The Behavior " + applicationBehavior.getName() + " was updated successfully");
    }

    @Override
    public Response addUser(User data) {
        Logger.DEBUG("start addUser");
        try {
            facade.addUser(data);
        } catch (InvalidUserCredentialsException e) {
            return respondERROR("Failed to add user!", e);
        }
        return respondOK("The user " + data.getUsername() + " was created successfully!");
    }

    @Override
    public Response login(User data) {
        JSONArray arrayOfApplication = new JSONArray();
        List<Application> apps;
        try {
            apps = facade.login(data);
        } catch (InvalidUserCredentialsException e) {
            return respondERROR("Invalid username of password!", e);
        }
        for (Application app : apps){
            arrayOfApplication.put(app.toJson());
        }
        return respondOK(arrayOfApplication.toString());
    }

    @Override
    public Response CreateObjInstance(String createObjInstanceRequest) {
        Logger.DEBUG("start create instance");
        try {
            JSONObject jsonObject = new JSONObject(createObjInstanceRequest);
            facade.addObjectInstance(jsonObject);
        } catch (JSONException e) {
            return respondERROR("Failed to create instance!", e);
        }
        Logger.DEBUG("Created Instance successfully");
        return respondOK("Created Instance successfully");
    }

    @Override
    public Response removeEvent(String data) {
        ApplicationEvent event;
        try {
            JSONObject jsonObject = new JSONObject(data);
            event = createApplicationEventFromJsonObj(jsonObject);
            facade.removeEvent(jsonObject.getString("applicationId"), jsonObject.getString("username"), event);
        } catch (JSONException e) {
            return respondERROR("Failed to remove event!", e);
        } catch (IOException e) {
            return respondERROR("Failed to remove event!", e);
        }


        return respondOK("Event " + event.getName() + " Removed!");
    }

    @Override
    public Response updateEvent(String data) {
        ApplicationEvent event;
        try {
            JSONObject jsonObject = new JSONObject(data);
            event = createApplicationEventFromJsonObj(jsonObject);
            facade.updateApplicationEvent(jsonObject.getString("applicationId"),jsonObject.getString("username"), event);
        } catch (JSONException e) {
            return respondERROR("Error: Failed to update application event!", e);
        } catch (IOException e) {
            return respondERROR("Error: Failed to update application event!", e);
        }
        return respondOK("The event " + event.getName() + " was updated successfully");
    }

    @Override
    public Response createEvent(String data) {
        ApplicationEvent event;
        try {
            JSONObject jsonObject = new JSONObject(data);
            event = createApplicationEventFromJsonObj(jsonObject);
            facade.createEvent(jsonObject.getString("applicationId"), jsonObject.getString("username"), event);
            return respondOK("Event " + event.getName() + " added!");
        } catch (JSONException e) {
            return respondERROR("Failed to create event!", e);
        } catch (IOException e) {
            return respondERROR("Failed to create event!", e);
        }
    }

    @Override
    public Response removeApplication(String data) {
        Application application;
        try {
            JSONObject jsonObject = new JSONObject(data);
            application = createApplicationFromJsonObj(jsonObject);
            facade.removeApplication(application.getId(), jsonObject.getString("username"));
        } catch (JSONException e) {
            return respondERROR("Failed to remove application!", e);
        } catch (IOException e) {
            return respondERROR("Failed to remove application!", e);
        } catch (DbxException e) {
            return respondERROR("Failed to remove application!", e);
        }
        return respondOK("Application "+ application.getName() +" Removed!");
    }

    @Override
    public Response updateApplication(String data) {
        Application app;
        try {
            JSONObject jsonObject = new JSONObject(data);
            app = createApplicationFromJsonObj(jsonObject);
            facade.updateApplication(app, jsonObject.getString("username"));
        } catch (CordovaRuntimeException e) {
            return respondERROR("Failed to update application!", e);
        } catch (IOException e) {
            return respondERROR("Failed to update application!", e);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return respondOK("The application was updated successfully");
    }

    @Override
    public Response updateObject(String data) {
        ApplicationObject applicationObject;
        try {
            JSONObject jsonObject = new JSONObject(data);
            applicationObject = createApplicationObjectFromJson(jsonObject);
            facade.updateApplicationObject(jsonObject.getString("applicationId"), jsonObject.getString("username"), applicationObject);
        } catch (JSONException e) {
            return respondERROR("Failed to update application object!", e);
        } catch (IOException e) {
            return respondERROR("Failed to update application object!", e);
        }
        return respondOK("The object " + applicationObject.getName() + " was updated successfully");
    }

    @Override
    public Response removeObjInstance(String reqParam) {
        String objName;
        try {
            JSONObject jsonObject = new JSONObject(reqParam);
            facade.removeObjectInstance(jsonObject);
            objName = jsonObject.getString("objName");
        } catch (JSONException e) {
            return respondERROR("Error: Failed to remove object instance!", e);
        }
        Logger.DEBUG("The object " + objName + " instance was removed successfully");
        return respondOK("The object " + objName + " instance was removed successfully");
    }

    @Override
    public AppInstance getObjInstance(String reqParam) {
        try {
            JSONObject jsonObject = new JSONObject(reqParam);
            return facade.getObjectInstance(jsonObject);
        } catch (JSONException e) {
            return null;
        }
    }

    /**
     * Starts the server.
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
        Logger.SEVERE("*** server running ***");

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
        Logger.SEVERE("server stopped.");
        // TODO - remove in production!!!
//        facade.clearDatabase();// TODO - remove in production!!!
        // TODO - remove in production!!!
    }

    private ApplicationObject createApplicationObjectFromJson(JSONObject data) throws JSONException, IOException {
        String[] names = {"id","name","attributes","actions","actionChains"};
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

    /**
     * Starts the server and awaits termination.
     *
     * @param args - null
     */
    public static void main(String[] args) {
        Logger.SEVERE("*** Starting server ***");
        try {
            facade.connectToDatabase();
            start();
        } catch (IOException e) {
            Logger.ERROR("Failed to connect to database!", e);
        }
    }
}