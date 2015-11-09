/**
 * Created by victor on 11/6/2015.
 *
 */

package Server;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * hAPPi RESTful Server
 * The server will host at the URL http://loaclhost/hAPPi on port 9998
 */
@Path("/hAPPi")
public class Server {
    // Global definitions
    private final static String SRV_HOST = "http://localhost"; // Server host name.
    private final static String SRV_PORT = "9998"; // Server port.
    private final static String SRV_MAIN = "/hAPPi";
    private final static String SRV_FULL = SRV_HOST + ":" + SRV_PORT + SRV_MAIN;
    private final String PATH_WEB_CONTENT = "C:\\Users\\victor\\IdeaProjects\\hAPPi\\REST Server\\src\\Web\\";
    private final String PATH_CORDOVA = "C:\\Users\\victor\\AppData\\Roaming\\npm\\cordova.cmd";
    private final String PATH_PROJECTS = "C:\\Users\\victor\\HAPPI\\Projects";


    // Paths to web pages
    private static final String PATH_MAIN = "/main"; //Path to main page.
    private final String PATH_CREATE_PROJECT = "/createProject"; //Path to project creation.
    private final String PATH_CREATE_ENTITY = "/createEntity"; //Path to entity creation.

    public static Logger logger = Logger.getLogger("ServerLogger");

    /**
     * Returns the main page of the application - "index.html".
     *
     * @return the HTML content of the main page.
     */
    @GET
    @Path(PATH_MAIN)
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
    @Path(PATH_CREATE_PROJECT)
    @Consumes(MediaType.TEXT_PLAIN)
    public void createProject(String data) throws JSONException {
        //TODO - Dummy implementation, needs to be implemented.
        JSONObject json = new JSONObject(data);
        String projectName = json.getString("name");
        logger.log(Level.INFO, "Creating Project " + json.getString("name") + "...");
        try {
            Process p = Runtime.getRuntime().exec(PATH_CORDOVA + " create " + PATH_PROJECTS + "/" + projectName + " com.example.hello " + projectName);
            p.waitFor();
            logger.log(Level.INFO, "Successfully created Project " + json.getString("name") + ".");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    /**
     * Creates a new entity for the user.
     */
    @POST
    @Path(PATH_CREATE_ENTITY)
    //@Consumes(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public void createEntity(String data) throws JSONException {
        JSONObject json = new JSONObject(data);
        //TODO - create entity object.
    }

    /**
     * Returns the file at given source to the client.
     *
     * @param src - path to the requested file.
     * @return the requested file or an error page.
     */
    private String getPage(String src) {
        try {
            return Util.FileHandler.readFile(PATH_WEB_CONTENT + src);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR - Requested file not found!"; // Should return error page.
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
     * Main - starts the server and awaits termination.
     * @param args - null
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        // Define new server
        HttpServer server = HttpServerFactory.create(SRV_HOST + ":" + SRV_PORT + "/");
        // Start the server
        server.start();

        logger.log(Level.SEVERE, "Server running...");
        logger.log(Level.INFO, "Visit: " + SRV_FULL + PATH_MAIN);
        logger.log(Level.INFO, "Projects: " + "C:\\Users\\victor\\HAPPI\\Projects");
        logger.log(Level.INFO, "Press ENTER to stop...");
        System.in.read();
        logger.log(Level.SEVERE, "Stopping server...");
        server.stop(0);
        logger.log(Level.SEVERE, "Server stopped.");
    }
}
