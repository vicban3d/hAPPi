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
// The Java class will be hosted at the URI path "/main"
@Path("/main")
public class Server {
    private static String SRV_HOST =  "http://localhost";
    private static String SRV_PORT =  "9998";
    private static String PATH_MAIN =  "/main";

    // The Java method will process HTTP GET requests
    @GET
    // The Java method will produce content identified by the MIME Media type "text/htm"
    @Produces("text/html")
    public String getMainPage() {
        try {
            return Util.FileHandler.readFile("C:\\Users\\victor\\IdeaProjects\\HAPPY Server\\RESTful Service\\src\\Web\\index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ERROR - Requested file not found!"; // Should return error page.
    }

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServerFactory.create(SRV_HOST + ":" + SRV_PORT + "/");
        server.start();

        System.out.println("Server running");
        System.out.println("Visit: " + SRV_HOST + ":" + SRV_PORT + PATH_MAIN);
        System.out.println("Hit return to stop...");
        System.in.read();
        System.out.println("Stopping server");
        server.stop(0);
        System.out.println("Server stopped");
    }
}
