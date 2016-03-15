package Server;

import Logic.Application;
import Logic.ApplicationBehavior;
import Logic.ApplicationObject;
import Logic.Facade;
import Utility.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/**
 * Created by victor on 1/21/2016.
 */
public interface RESTServer {
    /**
     * Returns the main page of the application - "index.html".
     * @return the HTML content of the main page.
     */
    @GET
    @Path(Strings.PATH_MAIN)
    @Produces(MediaType.WILDCARD)
    String getMainPage();

    /**
     * Creates a new Cordova project in PATH_APPS according to user parameters.
     * @param data - a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_CREATE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    String createApplication(Application data);

    /**
     * Builds an application package that can be installed on a mobile phone.
     * @param application - a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_BUILD_APP)
    @Produces(MediaType.TEXT_PLAIN)
    String buildApplication(Application application);

    /**
     * Creates a new object in the currently selected application.
     * @param data - a JSON representation of the application object.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_CREATE_OBJECT)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    String createObject(ApplicationObject data);

    /**
     * Removes an object from the currently selected application.
     * @param data - a JSON representation of the application object.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_REMOVE_OBJECT)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    String removeObject(ApplicationObject data);

    /**
     * Creates a behavior for the currently selected application.
     * @param data - a JSON representation of the application behavior.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_CREATE_BEHAVIOR)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    String createBehavior(ApplicationBehavior data);

    /**
     * Removes a behavior from the currently selected application.
     * @param data - a JSON representation of the application behavior.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_REMOVE_BEHAVIOR)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    String removeBehavior(ApplicationBehavior data);

    /**
     * Removes an application.
     * @param application - a JSON representation of the application.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_REMOVE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    String removeApplication(Application application);

    /**
     * Updates the content of an application.
     * @param data- a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_UPDATE_APP)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    String updateApplication(Application data);

    /**
     * Updates the content of an object.
     * @param data- a JSON representation of the object.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_UPDATE_OBJECT)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    String updateObject(ApplicationObject data);
}
