/**
 * Created by victor on 11/6/2015.
 *
 */

package Server;

import Exceptions.CordovaRuntimeException;
import Exceptions.DatabaseConnectionErrorException;
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
@Path("/hAPPi")
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
        String script = getPage("js/strings.js") + getPage("js/util.js") + getPage("js/index.js") + getPage("js/angular_index.js");
        String style = getPage("css/index.css");
        String page = getPage("index.html");
        return assemblePage(script, style, page);
    }

    /**
     * Returns the application page of the current application - "index.html".
     *
     * @return the HTML content of the main page.
     */
    @GET
    @Path(Strings.PATH_EMULATE_ANDROID)
    @Produces(MediaType.WILDCARD)
    public String emulateAndroid() {
        return "aaaa";
    }

    /**
     * Creates a new Cordova project in PATH_APPS according to user parameters.
     */
    @POST
    @Path(Strings.PATH_CREATE_APP)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String createApplication(String data) throws JSONException, CordovaRuntimeException {
        JSONObject json = new JSONObject(data);
        this.currentAppName = json.getString("name");
        facade.createApplication(data);
        return "The application " + currentAppName + " was created successfully";
    }

    /**
     * Add an android platform to an application
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_ANDROID)
    @Produces(MediaType.TEXT_XML)
    public String addAndroidModule() throws CordovaRuntimeException {
        facade.addAndroidToApplication(currentAppName);
        return "Added android to " + currentAppName + "!";
    }

    /**
     * Add an android platform to an application
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_IOS)
    @Produces(MediaType.TEXT_XML)
    public String addIOModuleS() throws CordovaRuntimeException {
        facade.addIOSToApplication(currentAppName);
        return "Added ios to " + currentAppName + "!";
    }

    /**
     * Add an android platform to a an application
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_WINDOWS_PHONE)
    @Produces(MediaType.TEXT_XML)
    public String addWindowsPhoneModule() throws CordovaRuntimeException {
        facade.addWindowsPhoneToApplication(currentAppName);
        return "Added windows phone to " + currentAppName + "!";
    }

    /**
     * Build an application
     */
    @POST
    @Path(Strings.PATH_BUILD_APP)
    @Produces(MediaType.TEXT_XML)
    public String buildApplication() throws CordovaRuntimeException {
        facade.buildApplication(currentAppName);
        return currentAppName + " built successfully!";
    }

    /**
     * Creates a new entity for the user.
     */
    @POST
    @Path(Strings.PATH_CREATE_ENTITY)
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_XML)
    public String createEntity(String data) {
        facade.createEntity(currentAppName, data);
        return "Object added!";
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
     * Assembles a web page from components
     * @param script - the concatenated string of script files.
     * @param style - the concatenated string of stylesheet files.
     * @param page - the HTML file.
     * @return the assembled web page.
     */
    private String assemblePage(String script, String style, String page){
        page = page.replace("<html>","").replace("<head>", "").replace("</head>", "").replace("<body>", "");
        return "<html><head><script src=\"http://ajax.googleapis.com/ajax/libs/angularjs/1.4.8/angular.min.js\"></script></head><body><script>" + script + "</script><style>" + style +"</style>" + page;
    }

    /**
     * Starts the Server.
     */
    private static void start() throws ServerConnectionErrorException {
        HttpServer server;
        try {
            server = HttpServerFactory.create(Strings.SRV_HOST + ":" + Strings.SRV_PORT + "/");
            server.start();
        } catch (IOException e) {
            throw new ServerConnectionErrorException(e.getMessage());
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
    public static void main(String[] args) throws ServerConnectionErrorException, DatabaseConnectionErrorException {
        facade = new hAPPiFacade();
        facade.connectToDatabase();
        start();
    }
}
