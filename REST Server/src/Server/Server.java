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

    // Silence the built-in jersey logger
    private final static java.util.logging.Logger COM_SUN_JERSEY_LOGGER = java.util.logging.Logger.getLogger( "com.sun.jersey" );
    static { COM_SUN_JERSEY_LOGGER.setLevel( Level.SEVERE ); }

    private static Facade facade = new hAPPiFacade();
    private Application currentlySelectedApplication;

    /**
     * Returns the main page of the application - "index.html".
     *
     * @return the HTML content of the main page.
     */
    @GET
    @Path(Strings.PATH_MAIN)
    @Produces(MediaType.WILDCARD)
    public String getMainPage() {
        return facade.getPage("index.html");
    }

    /**
     * Creates a new Cordova project in PATH_APPS according to user parameters.
     * @param data - a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_CREATE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String createApplication(Application data){
        try {
            Logger.INFO("Creating a new application: " + data.getId());
            this.currentlySelectedApplication = data;
            facade.createApplication(data);
            return "Created " + data.getName();
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed execute cordova command", e.getMessage());
            return "Error: failed to create application!";
        } catch (JSONException e) {
            Logger.ERROR("Incorrect JSON format", e.getMessage());
            return "Error: failed to create application!";
        }
    }

    /**
     * Builds an application package that can be installed on a mobile phone.
     * @param application - a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_BUILD_APP)
    @Produces(MediaType.TEXT_PLAIN)
    public String buildApplication(Application application) {
        try {
            return facade.buildApplication(application.getId());
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed to build application", e.getMessage());
            return "Error building application " + application;
        } catch (IOException e) {
            Logger.ERROR("Failed to read application files", e.getMessage());
            return "Error building application " + application;
        } catch (DbxException e) {
            Logger.ERROR("Failed to access Dropbox", e.getMessage());
            return "Error building application " + application;
        }
    }

    /**
     * Creates a new object in the currently selected application.
     * @param data - a JSON representation of the application object.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_CREATE_OBJECT)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String createObject(ApplicationObject data){
        try {
            facade.createObject(currentlySelectedApplication.getId(), data);
            return "Object " + data.getName() + " added!";
        }
        catch (Exception e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create object!";
        }

    }

    /**
     * Removes an object from the currently selected application.
     * @param data - a JSON representation of the application object.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_REMOVE_OBJECT)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String removeObject(ApplicationObject data) {
        try {
            facade.removeObject(currentlySelectedApplication.getId(), data);
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to remove object!";
        }
        return "Object Removed!";
    }

    /**
     * Creates a behavior for the currently selected application.
     * @param data - a JSON representation of the application behavior.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_CREATE_BEHAVIOR)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String createBehavior(ApplicationBehavior data){
        try {
            facade.createBehavior(currentlySelectedApplication.getId(), data);
        }  catch (Exception e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create behavior!";
        }
        return "Behavior added!";
    }

    /**
     * Removes a behavior from the currently selected application.
     * @param data - a JSON representation of the application behavior.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_REMOVE_BEHAVIOR)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String removeBehavior(ApplicationBehavior data) {
        try {
            facade.removeBehavior(currentlySelectedApplication.getId(), data);
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to remove behavior!";
        }
        return "Behavior Removed!";
    }

    /**
     * Removes an application.
     * @param application - a JSON representation of the application.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_REMOVE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String removeApplication(Application application) {
        try {
            facade.removeApplication(application.getId());
        }
        catch (Exception e){
            Logger.ERROR("Failed to remove application", e.getMessage());
            return "Failed to remove application!";
        }

        return "Application Removed!";
    }

    /**
     * Updates the content of an application.
     * @param data- a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_UPDATE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    public String UpdateApplication(Application data) {
        try {
            facade.updateApplication(data);
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed execute cordova command", e.getMessage());
            return "Error: failed to update application!";
        } catch (IOException e) {
            Logger.ERROR("Error reading data", e.getMessage());
            return "Error: Failed to update application!";
        }
        this.currentlySelectedApplication = data;
        return "The application " + data.getName() + " was updated successfully";
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
     * Starts the server and awaits termination.
     * @param args - null
     */
    public static void main(String[] args) {
        Logger.SEVERE("*** Starting Server ***");
        try {
            facade.connectToDatabase();
            start();
        } catch (IOException e) {
            Logger.ERROR("Error connecting to database!", e.getMessage());
        }
    }
}
