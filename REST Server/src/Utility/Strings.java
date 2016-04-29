package Utility;

/**
 * Created by victor on 11/9/2015.
 *
 * An interface containing all String Constants for the system.
 */
public class Strings {
    private static final String username = System.getProperty("user.name");
    public static final String SRV_HOST =  "http://localhost";
    public static final String SRV_PORT = "80";
    public static final String SRV_MAIN = "/";
    public static final String SRV_FULL = SRV_HOST + ":" + SRV_PORT + SRV_MAIN;
    public static final String PATH_WEB_CONTENT = "C:\\users\\" + username + "\\IdeaProjects\\hAPPi\\REST Server\\src\\Web\\";
    public static final String PATH_CORDOVA = "C:\\users\\" + username + "\\AppData\\Roaming\\npm\\cordova.cmd";
    public static final String PATH_APPS = "C:\\users\\" + username + "\\HAPPI\\Applications";
    public static final String PATH_MAIN = "/";
    public static final String PATH_IMAGES = "/img/{resource}";
    public static final String PATH_RESOURCE = "/{folder}/{resource}";
    public static final String PATH_CREATE_APP = "/createApplication";
    public static final String PATH_BUILD_APP = "/build";
    public static final String PATH_CREATE_OBJECT = "/createObject";
    public static final String PATH_CREATE_BEHAVIOR = "/createBehavior";
    public static final String PATH_REMOVE_BEHAVIOR = "/removeBehavior";
    public static final String PATH_REMOVE_OBJECT = "/removeObject";
    public static final String PATH_REMOVE_APP = "/removeApplication";
    public static final String PATH_UPDATE_APP = "/updateApplication";
    public static final String PATH_UPDATE_OBJECT = "/updateObject";
    public static final String PATH_UPDATE_BEHAVIOR = "/updateBehavior";
    public static final String PATH_SIGNUP_PAGE = "/signup";
    public static final String PATH_CREATE_USER = "/addUser";
    public static final String PATH_LOGIN = "/login";
    public static final String PATH_CREATE_OBJECT_INSTANCE = "/AddObjInstance";
    public static final String PATH_REMOVE_OBJECT_INSTANCE = "/RemoveObjInstance";
    public static final String DB_NAME = "hAPPiDB";
    public static final String DB_PORT = "27017";
    public static final String PATH_CREATE_EVENT = "/createEvent";
    public static final String PATH_REMOVE_EVENT = "/removeEvent";
    public static final String PATH_UPDATE_EVENT = "updateEvent";
}