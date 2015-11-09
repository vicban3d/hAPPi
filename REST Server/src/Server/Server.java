/**
 * Created by victor on 11/6/2015.
 *
 */

package Server;

import Database.MongoDB;
import Util.Logger;
import Util.Strings;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
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
@Path("/hAPPi")
public class Server {
    // Global definitions

    private static MongoDB db;
    private String projectName = "TEST_PROJECT";

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
        //TODO - Dummy implementation, needs to be implemented.
        JSONObject json = new JSONObject(data);
        projectName = json.getString("name");
        Logger.logINFO("Creating Project " + json.getString("name") + "...");
        try {
            Process p = Runtime.getRuntime().exec(Strings.PATH_CORDOVA + " create " + Strings.PATH_PROJECTS + "/" + projectName + " com.example.hello " + projectName);
            p.waitFor();
            Logger.logINFO("Created new project " + json.getString("name") + ".");
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates a new entity for the user.
     */
    @POST
    @Path(Strings.PATH_CREATE_ENTITY)
    @Consumes(MediaType.TEXT_PLAIN)
    public void createEntity(String data) {
        db.addData(projectName, "Entities", data);
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
            return Util.FileHandler.readFile(Strings.PATH_WEB_CONTENT + src);
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
        HttpServer server = HttpServerFactory.create(Strings.SRV_HOST + ":" + Strings.SRV_PORT + "/");
        // Start the server
        server.start();

        db = new MongoDB();
        db.connect();

        Logger.logSEVERE("Server running...");
        Logger.logINFO("Visit: " + Strings.SRV_FULL + Strings.PATH_MAIN);
        Logger.logINFO("Projects: " + Strings.PATH_PROJECTS);
        Logger.logINFO("Press ENTER to stop...");
        System.in.read();
        Logger.logSEVERE("Stopping server...");
        server.stop(0);
        Logger.logSEVERE("Server stopped.");
    }
}
