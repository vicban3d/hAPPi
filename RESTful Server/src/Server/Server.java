package Server;

import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.io.IOException;

/**
 * Created by victor on 11/6/2015.
 *
 */

/**
 * hAPPi RESTful Server
 * The server will host at the URL http://loaclhost/hAPPi on port 9998
 */
@Path("/hAPPi")
public class Server {
    // Global definitions
    private final static String SRV_HOST = "http://localhost"; // Server host name.
    private final static String SRV_PORT = "9998"; // Server port.
    private final String PATH_WEB_CONTENT = "C:\\Users\\victor\\IdeaProjects\\hAPPi\\RESTful Server\\src\\Web\\";
    private final String PATH_CORDOVA = "C:\\Users\\victor\\AppData\\Roaming\\npm\\cordova.cmd";
    private final String PATH_PROJECTS = "C:\\Users\\victor\\HAPPI\\Projects";

    // Paths to web pages
    private final String PATH_MAIN = "/main"; //Path to main page.
    private final String PATH_CREATE_PROJECT = "/createProject"; //Path to main page.

    /**
     * Returns the main page of the application - "index.html".
     *
     * @return the HTML content of the main page.
     */
    @GET
    @Path(PATH_MAIN)
    @Produces(MediaType.WILDCARD)
    public String getMainPage() {
        return assemblePage(getPage("js/index.js"), getPage("css/index.css"), getPage("index.html"));
    }

    /**
     * Creates a new Cordova project in PATH_PROJECTS according to user parameters.
     * @return page upon success or failure.
     */
    @GET
    @Path(PATH_CREATE_PROJECT)
    @Produces(MediaType.TEXT_PLAIN)
    public String createProject() {
        try {
            Process p = Runtime.getRuntime().exec(PATH_CORDOVA + " create " + PATH_PROJECTS + "/hello com.example.hello HelloWorld");
            p.waitFor();
            return "Created Project!";
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "ERROR - Requested file not found!"; // Should return error page.
    }

    /**
     * Returns the file at given source to the client.
     *
     * @param src - path to the requested file
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

    private String assemblePage(String script, String style, String page){
        return "<html><head><script>" + script + "</script><style>" + style +"</style></head>" + page;
    }

    public static void main(String[] args) throws IOException {
        // Define new server
        HttpServer server = HttpServerFactory.create(SRV_HOST + ":" + SRV_PORT + "/");
        // Start the server
        server.start();

        System.out.println("Server running");
        System.out.println("Visit: " + SRV_HOST + ":" + SRV_PORT + "/hAPPi" + "/main");
        System.out.println("Projects: " + "C:\\Users\\victor\\HAPPI\\Projects");
        System.out.println("Press ENTER to stop...");
        System.in.read();

        System.out.println("Stopping server...");
        // Stop the server
        server.stop(0);
        System.out.println("Server stopped");
    }
}
