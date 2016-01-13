/**
 * Created by victor on 11/6/2015.
 *
 */

package Server;

import Exceptions.CordovaRuntimeException;
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
    private String currentApp = "";

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
    public String createApplication(String data){
        JSONObject json;
        String appName;
        try {
            json = new JSONObject(data);
            appName = json.getString("name");
            facade.createApplication(data);

            this.currentApp = data;
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
            return "The application " + appName + " was created successfully";
    }

    /**
     * Add an android platform to an application
     */
    /*@POST
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
*/
    /**
     * Add an android platform to an application
     */
   /* @POST
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
*/
    /**
     * Add an android platform to a an application
     */
   /* @POST
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
*/
    /**
     * Build an application
     */
    @POST
    @Path(Strings.PATH_BUILD_APP)
    @Produces(MediaType.TEXT_XML)
    public String buildApplication(String application) throws CordovaRuntimeException, JSONException{
        JSONObject json;
        try {
            json = new JSONObject(application);
            facade.buildApplication(json.getString("id"));
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
        return json.getString("name") + " built successfully!";
    }

    /**
     * Creates a new object for the user.
     */
    @POST
    @Path(Strings.PATH_CREATE_OBJECT)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String createObject(String data){
        try {
        JSONObject json = new JSONObject(currentApp);
        facade.createObject(json.getString("id"), data);
        }  catch (JSONException e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create object!";
        }
        return "Object added!";
    }

    /**
     * Creates a new object for the user.
     */
    @POST
    @Path(Strings.PATH_REMOVE_OBJECT)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String removeObject(String data) throws JSONException{
        JSONObject json = new JSONObject(currentApp);
        facade.removeObject(json.getString("id"), data);
        return "Object Removed!";
    }

    @POST
    @Path(Strings.PATH_CREATE_BEHAVIOR)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String createBehavior(String data){
        try {
            JSONObject json = new JSONObject(currentApp);
            facade.createBehavior(json.getString("id"), json.getString("name"), data);
        }  catch (JSONException e) {
            Logger.ERROR("Incorrect data format", e.getMessage());
            return "Error: failed to create behavior!";
        }
        return "Behavior added!";
    }

    /**
     * Creates a new object for the user.
     */
    @POST
    @Path(Strings.PATH_REMOVE_BEHAVIOR)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String removeBehavior(String data) throws JSONException{
        JSONObject json = new JSONObject(currentApp);
        facade.removeBehavior(json.getString("id"), data);
        return "Behavior Removed!";
    }

    /**
     * Creates a new object for the user.
     */
    @POST //change to delete
    @Path(Strings.PATH_REMOVE_APP)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String removeApplication(String data) {
        facade.removeApplication(data);
        return "Application Removed!";
    }

    @POST
    @Path(Strings.PATH_UPDATE_APP)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String UpdateApplication(String data) throws JSONException, CordovaRuntimeException, IOException{
        facade.updateApplication(data);
        this.currentApp = data;
        JSONObject json = new JSONObject(data);
        return "The application " + json.getString("name") + " was updated successfully";
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

    public void setFacade(Facade newFacade){
        facade = newFacade;
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
