/**
 * Created by victor on 11/6/2015.
 *
 */

package Server;

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
    private String projectName = "TEMP_PROJECT_NAME";

    /**
     * Returns the main page of the application - "index.html".
     *
     * @return the HTML content of the main page.
     */
    @GET
    @Path(Strings.PATH_MAIN)
    @Produces(MediaType.WILDCARD)
    public String getMainPage() {
        String script = getPage("js/strings.js") + getPage("js/util.js") + getPage("js/index.js");
        String style = getPage("css/index.css");
        String page = getPage("index.html");
        return assemblePage(script, style, page);
    }

    /**
     * Creates a new Cordova project in PATH_PROJECTS according to user parameters.
     */
    @POST
    @Path(Strings.PATH_CREATE_PROJECT)
    @Consumes(MediaType.TEXT_PLAIN)
    public void createProject(String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        this.projectName = json.getString("name");
        facade.createProject(data);
    }

    /**
     * Add an android platform to a project
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_ANDROID)
    public void addAndroidModule(){
        facade.addAndroidToProject(projectName);
    }

    /**
     * Add an android platform to a project
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_IOS)
    public void addIOModuleS(){
        facade.addIOSToProject(projectName);
    }

    /**
     * Add an android platform to a project
     */
    @POST
    @Path(Strings.PATH_ADD_PLATFORM_WINDOWS_PHONE)
    public void addWindowsPhoneModule(){
        facade.addWindowsPhoneToProject(projectName);
    }

    /**
     * Build a project
     */
    @POST
    @Path(Strings.PATH_BUILD_PROJECT)
    public void buildProject(){
        facade.buildProject(projectName);
    }

    /**
     * Creates a new entity for the user.
     */
    @POST
    @Path(Strings.PATH_CREATE_ENTITY)
    @Consumes(MediaType.TEXT_PLAIN)
    public void createEntity(String data) {
        facade.createEntity(projectName, data);
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
        return "<html><head><script>" + script + "</script><style>" + style +"</style></head>" + page + "</html>";
    }

    /**
     * Starts the Server.
     */
    private static void start(){
        HttpServer server;
        try {
            server = HttpServerFactory.create(Strings.SRV_HOST + ":" + Strings.SRV_PORT + "/");
            server.start();
        } catch (IOException e) {
            Logger.logERROR("Failed to start server!", e.getMessage());
            return;
        }
        Logger.logSEVERE("Server running.");
        Logger.logINFO("Visit: " + Strings.SRV_FULL + Strings.PATH_MAIN);
        Logger.logINFO("Projects: " + Strings.PATH_PROJECTS);
        Logger.logINFO("Press ENTER to stop...");

        try {
            //noinspection ResultOfMethodCallIgnored
            System.in.read();
        } catch (IOException e) {
            Logger.logERROR("Error reading stop signal!", e.getMessage());
            return;
        }
        Logger.logSEVERE("Stopping server...");
        server.stop(0);
        Logger.logSEVERE("Server stopped.");
    }

    /**
     * Main - starts the server and awaits termination.
     * @param args - null
     */
    public static void main(String[] args) {
        facade = new hAPPiFacade();
        facade.connectToDatabase();
        start();
    }
}
