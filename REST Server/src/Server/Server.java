/**
 * Created by victor on 11/6/2015.
 *
 */

package Server;

import Exceptions.CordovaRuntimeException;
import Exceptions.ServerConnectionErrorException;
import Logic.Facade;
import Logic.hAPPiFacade;
import Utility.Logger;
import Utility.Strings;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.jersey.spi.resource.Singleton;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * hAPPi RESTful Server
 * The server will host at the URL http://loaclhost/hAPPi on port 9998
 */
@Singleton
@Path(Strings.SRV_MAIN)
public class Server {

    private static Facade facade;
    private String currentAppName = "";

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
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String createApplication(String data) {
        try {
            JSONObject json = new JSONObject(data);
            this.currentAppName = json.getString("name");
            facade.createApplication(data);
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
        return "The application " + currentAppName + " was created successfully";
    }

    /**
     * Add an android platform to an application
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_ANDROID)
    @Produces(MediaType.TEXT_XML)
    public String addAndroidModule() {
        try {
            facade.addAndroidToApplication(currentAppName);
            return "Added android to " + currentAppName + "!";
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed to add android platform", e.getMessage());
            return "Error: failed to create object!";
        }

    }

    /**
     * Add an android platform to an application
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_IOS)
    @Produces(MediaType.TEXT_XML)
    public String addIOSModule() {
        try {
            facade.addIOSToApplication(currentAppName);
            return "Added ios to " + currentAppName + "!";
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed to add ios platform", e.getMessage());
            return "Error: failed to create object!";
        }
    }

    /**
     * Add an android platform to a an application
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_WINDOWS_PHONE)
    @Produces(MediaType.TEXT_XML)
    public String addWindowsPhoneModule() {
        try {
            facade.addWindowsPhoneToApplication(currentAppName);
            return "Added windows phone to " + currentAppName + "!";
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed to add wp8 platform", e.getMessage());
            return "Error: failed to create object!";
        }

    }

    /**
     * Build an application
     */
    @POST
    @Path(Strings.PATH_BUILD_APP)
    @Produces(MediaType.TEXT_XML)
    public String buildApplication() {
        try {
            facade.buildApplication(currentAppName);
            return currentAppName + " built successfully!";
        } catch (CordovaRuntimeException e) {
            Logger.ERROR("Failed to build application", e.getMessage());
            return "Error building application " + currentAppName;
        }
    }

    /**
     * Creates a new entity for the user.
     */
    @POST
    @Path(Strings.PATH_CREATE_ENTITY)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String createEntity(String data) {
        try {
            facade.createEntity(currentAppName, data);
        } catch (IOException e) {
            Logger.ERROR("Failed to create object", e.getMessage());
            return "Error: failed to create object!";
        } catch (JSONException e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create object!";
        }
        return "Object added!";
    }

    /**
     * Creates a new entity for the user.
     */
    @POST
    @Path(Strings.PATH_REMOVE_ENTITY)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String removeEntity(String data) {
        try {
            facade.removeEntity(currentAppName, data);
        } catch (IOException e) {
            Logger.ERROR("Failed to remove object", e.getMessage());
            return "Error: failed to remove object!";
        }
        return "Object Removed!";
    }

    /**
     * Creates a new entity for the user.
     */
    @POST //change to delete
    @Path(Strings.PATH_REMOVE_APP)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String removeApplication(String data) {
        try {
            facade.removeApplication(data);
        } catch (JSONException e) {
            Logger.ERROR("Failed to remove application", e.getMessage());
            return "Error: failed to remove application!";
        }
        return "Application Removed!";
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
        Logger.SEVERE("Server running.");

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

    /**
     * Main - starts the server and awaits termination.
     * @param args - null
     */
    public static void main(String[] args) {
        facade = new hAPPiFacade();
        try {
            facade.connectToDatabase();
            start();
        } catch (IOException e) {
            Logger.ERROR("Error connecting to database!", e.getMessage());
        }
    }
}
