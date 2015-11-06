package Server;
import com.sun.jersey.api.container.httpserver.HttpServerFactory;
import com.sun.net.httpserver.HttpServer;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.io.IOException;

/**
 * Created by victor on 11/6/2015.
 */

/**
 * hAPPi RESTful Server
 * The server will host at the URL http://loaclhost/hAPPi on port 9998
 */
@Path("/hAPPi")
public class Server {
    private final static String SRV_HOST =  "http://localhost"; // Server host name.
    private final static String SRV_PORT =  "9998"; // Server port.
    private final String PATH_MAIN =  "/main"; //Path to main page.
    private final String PATH_WEB_CONTENT = "C:\\Users\\victor\\IdeaProjects\\HAPPY Server\\RESTful Service\\src\\Web\\";

    /**
     * Returns the main page of the application - "index.html".
     * @return
     */
    @GET
    @Path(PATH_MAIN)
    @Produces("text/html")
    public String getMainPage() {
        return getPage("index.html");
    }

    /**
     * Returns the file at given source to the client.
     * @param src - path to the requested file
     * @return the requested file or an error page.
     */
    private String getPage(String src){
        try {
            return Util.FileHandler.readFile(PATH_WEB_CONTENT + src);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR - Requested file not found!"; // Should return error page.
    }

    public static void main(String[] args) throws IOException {
        // Define new server
        HttpServer server = HttpServerFactory.create(SRV_HOST + ":" + SRV_PORT + "/");
        // Start the server
        server.start();

        System.out.println("Server running");
        System.out.println("Visit: " + SRV_HOST + ":" + SRV_PORT + "/hAPPi" + "/main");
        System.out.println("Hit return to stop...");
        System.in.read();

        System.out.println("Stopping server");
        // Stop the server
        server.stop(0);
        System.out.println("Server stopped");
    }
}
