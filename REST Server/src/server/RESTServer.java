package server;

import logic.*;
import utility.Strings;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by victor on 1/21/2016.
 */
public interface RESTServer {

//    @OPTIONS
//    @Produces(MediaType.WILDCARD)
//    Response respondOK(String data);
//
//    @OPTIONS
//    @Produces(MediaType.WILDCARD)
//    Response respondERROR(String data);

    /**
     * Returns the main page of the application - "index.html".
     * @return the HTML content of the main page.
     */
    @GET
    @Path(Strings.PATH_MAIN)
    @Produces(MediaType.WILDCARD)
    Response getMainPage();

    /**
     * Returns requested image file.
     * @param resource - requested resource name.
     * @return the requested resource.
     */
    @GET
    @Path(Strings.PATH_IMAGES)
    @Produces("image/*")
    byte[] getImage(@SuppressWarnings("RSReferenceInspection") @PathParam("resource") String resource);

    /**
     * Returns requested resource file.
     * @param folder - requested folder.
     * @param resource - requested resource name.
     * @return the requested resource.
     */
    @GET
    @Path(Strings.PATH_RESOURCE)
    @Produces({"text/html", "text/css"})
    Response getResource(@SuppressWarnings("RSReferenceInspection") @PathParam("folder") String folder, @SuppressWarnings("RSReferenceInspection") @PathParam("resource") String resource);



    /**
     * Creates a new Cordova project in PATH_APPS according to user parameters.
     * @param data - a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_CREATE_APP)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response createApplication(String data);

    /**
     * Builds an application package that can be installed on a mobile phone.
     * @param data - a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_BUILD_APP)
    @Produces(MediaType.TEXT_PLAIN)
    Response buildApplication(String data);

    /**
     * Creates a new object in the currently selected application.
     * @param data - a JSON representation of the application object.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_CREATE_OBJECT)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response createObject(String data);

    /**
     * Removes an object from the currently selected application.
     * @param data - a JSON representation of the application object.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_REMOVE_OBJECT)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response removeObject(String data);

    /**
     * Creates a behavior for the currently selected application.
     * @param data - a JSON representation of the application behavior.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_CREATE_BEHAVIOR)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response createBehavior(String data);

    /**
     * Removes a behavior from the currently selected application.
     * @param data - a JSON representation of the application behavior.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_REMOVE_BEHAVIOR)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response removeBehavior(String data);

    /**
     * Removes an application.
     * @param data - a JSON representation of the application.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_REMOVE_APP)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response removeApplication(String data);

    /**
     * Updates the content of an application.
     * @param data - a JSON representation of the application.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_UPDATE_APP)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response updateApplication(String data);

    /**
     * Updates the content of an object.
     * @param data - a JSON representation of the object.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_UPDATE_OBJECT)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response updateObject(String data);

    /**
     * Updates the content of a behavior.
     * @param data - a JSON representation of the behavior.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_UPDATE_BEHAVIOR)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response updateBehavior(String data);

//    /**
//     * get signup page
//     * @return - signup page.
//     */
//    @GET
//    @Path(Strings.PATH_SIGNUP_PAGE)
//    @Produces(MediaType.WILDCARD)
//    String getSignupPage();


    /**
     * Creates new user in db
     * @param data - a JSON representation user.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_CREATE_USER)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.TEXT_PLAIN)
    Response addUser(User data);

    /**
     * Creates a behavior for the currently selected application.
     * @param data - a JSON representation of the application behavior.
     * @return - the request status.
     */
    @POST
    @Path(Strings.PATH_CREATE_EVENT)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response createEvent(String data);

    /**
     * Checks user login credentials.
     * @param data - a JSON representation user.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_LOGIN)
    @Consumes({MediaType.APPLICATION_JSON})
    @Produces(MediaType.APPLICATION_JSON)
    Response login(User data);

    /**
     * Creates new user in db
     * @param createObjInstanceRequest - request
     * */
    @POST
    @Path(Strings.PATH_CREATE_OBJECT_INSTANCE)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response CreateObjInstance(String createObjInstanceRequest);

    /**
     * Creates new user in db
     * @param reqParam - request
     */
    @POST
    @Path(Strings.PATH_REMOVE_OBJECT_INSTANCE)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response removeObjInstance(String reqParam);

    /**
     * Creates new user in db
     * @param reqParam - request
     */
    @POST
    @Path(Strings.PATH_UPDATE_OBJECT_INSTANCE)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response updateObjInstance(String reqParam);

    /**
     * Creates new user in db
     * @param reqParam - request
     * */
    @POST
    @Path(Strings.PATH_GET_OBJECT_INSTANCE)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.APPLICATION_JSON)
    Response getObjInstance(String reqParam);

    /**
     * Removes an event from the currently selected application.
     * @param data - a JSON representation of the application event.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_REMOVE_EVENT)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response removeEvent(String data);

    /**
     * Updates the content of an event.
     * @param data - a JSON representation of the event.
     * @return - the status of the request.
     */
    @POST
    @Path(Strings.PATH_UPDATE_EVENT)
    @Consumes({MediaType.TEXT_PLAIN})
    @Produces(MediaType.TEXT_PLAIN)
    Response updateEvent(String data);

}
