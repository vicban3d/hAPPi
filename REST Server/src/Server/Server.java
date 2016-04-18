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

import javax.ws.rs.Path;
import java.io.IOException;
import java.util.logging.Level;

/**
 * hAPPi RESTful Server
 * The server will host at the URL http://localhost on port 80
 */
@Singleton
@Path(Strings.SRV_MAIN)
public class Server implements RESTServer {

    // Silence the built-in jersey logger
    private final static java.util.logging.Logger COM_SUN_JERSEY_LOGGER = java.util.logging.Logger.getLogger( "com.sun.jersey" );
    static { COM_SUN_JERSEY_LOGGER.setLevel( Level.SEVERE ); }
    private static Facade facade = new hAPPiFacade();
    private Application currentlySelectedApplication;
    private User currentUser = new User("tempUsername", "tempPass", "tempMail");

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
    public String createApplication(Application data){
        try {
            Logger.INFO("Creating a new application: " + data.getId());
            this.currentlySelectedApplication = data;
            facade.createApplication(data, currentUser.getUsername());
            return "Created " + data.getName();
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed execute cordova command", e.getMessage());
            return "Error: failed to create application!";
        } catch (JSONException e) {
            Logger.ERROR("Incorrect JSON format", e.getMessage());
            return "Error: failed to create application!";
        }
    }

    @Override
    public String buildApplication(Application application) {
        try {
            return facade.buildApplication(application.getId(), currentUser.getUsername());
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

    @Override
    public String createObject(ApplicationObject data){
        try {
            facade.createObject(currentlySelectedApplication.getId(), currentUser.getUsername(), data);
            return "Object " + data.getName() + " added!";
        }
        catch (Exception e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create object!";
        }

    }

    @Override
    public String removeObject(ApplicationObject data) {
        try {
            facade.removeObject(currentlySelectedApplication.getId(), currentUser.getUsername(), data);
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to remove object!";
        }
        return "Object Removed!";
    }

    @Override
    public String createBehavior(ApplicationBehavior data){
        try {
            facade.createBehavior(currentlySelectedApplication.getId(), currentUser.getUsername(), data);
            return "Behavior " + data.getName() + " added!";
        }  catch (Exception e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create behavior!";
        }
    }

    @Override
    public String removeBehavior(ApplicationBehavior data) {
        try {
            facade.removeBehavior(currentlySelectedApplication.getId(), currentUser.getUsername(), data);
        } catch (Exception e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to remove behavior!";
        }
        return "Behavior Removed!";
    }

    @Override
    public String updateBehavior(ApplicationBehavior data) {
        try {
            facade.updateApplicationBehavior(currentlySelectedApplication.getId(), currentUser.getUsername(), data);
        } catch (Exception e) {
            Logger.ERROR("Error : failed to update application object!", e.getMessage());
            return "Error: Failed to update application object!";
        }
        return "The object " + data.getName() + " was updated successfully";
    }

    @Override
    public String getSignupPage() {
        Logger.INFO("get signup");
        return facade.getPage("signup.html");
    }

    @Override
    public String addUser(User data) {
        Logger.INFO("start addUser");
        try{
            facade.addUser(data);
        } catch (Exception e){
            Logger.ERROR("Error : Failed to add user!", e.getMessage());
            return "Error: Failed to add user!";
        }
        return "The user " + data.getUsername() + " was created successfully!";
    }

    @Override
    public String removeApplication(Application application) {
        try {
            facade.removeApplication(application.getId(), currentUser.getUsername());
        }
        catch (Exception e){
            Logger.ERROR("Failed to remove application", e.getMessage());
            return "Failed to remove application!";
        }

        return "Application Removed!";
    }

    @Override
    public String updateApplication(Application data) {
        try {
            facade.updateApplication(data, currentUser.getUsername());
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

    @Override
    public String updateObject(ApplicationObject data) {
        try {
            facade.updateApplicationObject(currentlySelectedApplication.getId(),currentUser.getUsername(), data);
        } catch (Exception e) {
            Logger.ERROR("Error : failed to update application object!", e.getMessage());
            return "Error: Failed to update application object!";
        }
        return "The object " + data.getName() + " was updated successfully";
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

        System.out.println("Visit: " + Strings.SRV_HOST);
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

    public static Facade getFacade() {
        return facade;
    }

    public static void setFacade(Facade facade) {
        Server.facade = facade;
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
