/**
 * Created by victor on 11/6/2015.
 *
 */

package Server;

import Exceptions.CordovaRuntimeException;
import Logic.*;
import Utility.Logger;
import Utility.Strings;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.spi.resource.Singleton;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jettison.json.JSONException;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.logging.Level;

/**
 * hAPPi RESTful Server
 * The server will host at the URL http://loaclhost/hAPPi on port 9998
 */
@Singleton
@Path(Strings.SRV_MAIN)
public class Server {

    private final static java.util.logging.Logger COM_SUN_JERSEY_LOGGER = java.util.logging.Logger.getLogger( "com.sun.jersey" );
    static { COM_SUN_JERSEY_LOGGER.setLevel( Level.SEVERE ); }

    private static Facade facade;
    private Application currentApp;

    /**
     * Returns the main page of the application - "index.html".
     *
     * @return the HTML content of the main page.
     */
    @GET
    @Path(Strings.PATH_MAIN)
    @Produces(MediaType.WILDCARD)
    public String getMainPage() {
        return getPage("index.html");
    }

    /**
     * Creates a new Cordova project in PATH_APPS according to user parameters.
     */
    @POST
    @Path(Strings.PATH_CREATE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String createApplication(Application data){
        try {
            Logger.INFO("Creating a new application: " + data.getId());
            this.currentApp = data;
            facade.createApplication(data);
            return "Created " + data.getName();
        } catch (IOException e) {
            Logger.ERROR("Failed to create application", e.getMessage());
            return "Error: failed to create application!";
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed execute cordova command", e.getMessage());
            return "Error: failed to create application!";
        } catch (JSONException e) {
            Logger.ERROR("Incorrect JSON format", e.getMessage());
            return "Error: failed to create application!";
        }
    }

    /**
     * Build an application
     */
    @POST
    @Path(Strings.PATH_BUILD_APP)
    @Produces(MediaType.TEXT_PLAIN)
    public String buildApplication(Application application) throws CordovaRuntimeException, JSONException{
        try {
            facade.buildApplication(application.getId());
            return application.getName() + " built successfully!";
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed to build application", e.getMessage());
            return "Error building application " + application;
        }
        catch (JSONException e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error building application " + application;
        } catch (IOException e) {
            Logger.ERROR("Failed to read application files", e.getMessage());
            return "Error building application " + application;
        }

    }

    /**
     * Creates a new object for the user.
     */
    @POST
    @Path(Strings.PATH_CREATE_OBJECT)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String createObject(ApplicationObject data){
        try {
            facade.createObject(currentApp.getId(), data);
            return "Object " + data.getName() + " added!";
        }
        catch (JSONException e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create object!";
        } catch (IOException e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create object!";
        }

    }

    /**
     * Creates a new object for the user.
     */
    @POST
    @Path(Strings.PATH_REMOVE_OBJECT)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String removeObject(ApplicationObject data) throws JSONException, IOException {
        facade.removeObject(currentApp.getId(), data);
        return "Object Removed!";
    }

    @POST
    @Path(Strings.PATH_CREATE_BEHAVIOR)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String createBehavior(ApplicationBehavior data){
        try {
            facade.createBehavior(currentApp.getId(), data);
        }  catch (JSONException e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create behavior!";
        } catch (IOException e) {
            Logger.ERROR("Error reading data", e.getMessage());
            return "Error: Failed to create behavior!";
        }
        return "Behavior added!";
    }

    /**
     * Creates a new object for the user.
     */
    @POST
    @Path(Strings.PATH_REMOVE_BEHAVIOR)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String removeBehavior(ApplicationBehavior data) throws JSONException{
        try {
            facade.removeBehavior(currentApp.getId(), data);
        } catch (IOException e) {
            Logger.ERROR("Error reading data", e.getMessage());
            return "Error: Failed to remove behavior!";
        }
        return "Behavior Removed!";
    }

    /**
     * Creates a new object for the user.
     * @param application - the application to delete
     */
    @POST
    @Path(Strings.PATH_REMOVE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String removeApplication(Application application) {
        try {
            facade.removeApplication(application.getId());
        } catch (JSONException e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create behavior!";
        } catch (IOException e) {
            Logger.ERROR("Error reading data", e.getMessage());
            return "Error: Failed to remove behavior!";
        }
        return "Application Removed!";
    }

    @POST
    @Path(Strings.PATH_UPDATE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String UpdateApplication(Application data) throws JSONException, CordovaRuntimeException, IOException{
        facade.updateApplication(data);
        this.currentApp = data;
        return "The application " + data.getName() + " was updated successfully";
    }

    /**
     * Returns the file at given source to the client.
     *
     * @param src - path to the requested file.
     * @return the requested file or an error page.
     */
    private String getPage(String src) {
        return Utility.FileHandler.readFile(Strings.PATH_WEB_CONTENT + src);
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
            Logger.ERROR("Failed to start server", e.getMessage());
            return;
        }
        Logger.SEVERE("*** Server running ***");

        System.out.println("Visit: " + Strings.SRV_FULL + Strings.PATH_MAIN);
        System.out.println("Applications: " + Strings.PATH_APPS);
        System.out.println("Press ENTER to stop...");

        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException e) {
            Logger.ERROR("Error reading stop signal!", e.getMessage());
            return;
        }
        Logger.SEVERE("Stopping server...");
        server.stop(0);
        Logger.SEVERE("Server stopped.");
        // TODO - remove in production!!!
        facade.clearDatabase();// TODO - remove in production!!!
        // TODO - remove in production!!!
    }

    public void setFacade(Facade newFacade){
        facade = newFacade;
    }

    /**
     * Main - starts the server and awaits termination.
     * @param args - null
     */
    public static void main(String[] args) {
        Logger.SEVERE("*** Starting Server ***");
        facade = new hAPPiFacade();
        try {
            facade.connectToDatabase();
            start();
        } catch (IOException e) {
            Logger.ERROR("Error connecting to database!", e.getMessage());
        }
    }
}
