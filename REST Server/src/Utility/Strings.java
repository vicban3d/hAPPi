package Utility;

/**
 * Created by victor on 11/9/2015.
 *
 */

/**
 * An interface containing all String Constants for the system.
 */
public interface Strings {
    String username = System.getProperty("user.name");
    String SRV_HOST = "http://localhost";
    String SRV_PORT = "9998";
    String SRV_MAIN = "/hAPPi";
    String SRV_FULL = SRV_HOST + ":" + SRV_PORT + SRV_MAIN;
    String PATH_WEB_CONTENT = "C:\\users\\" + username + "\\IdeaProjects\\hAPPi\\REST Server\\src\\Web\\";
    String PATH_CORDOVA = "C:\\users\\" + username + "\\AppData\\Roaming\\npm\\cordova.cmd";
    String PATH_APPS = "C:\\users\\" + username + "\\HAPPI\\Applications";
    String PATH_MAIN = "/main";
    String PATH_EMULATE_ANDROID = "/emulateAndroid";
    String PATH_CREATE_APP = "/createApplication";
    String PATH_ADD_PLATFORM_ANDROID = "/addAndroid";
    String PATH_ADD_PLATFORM_IOS = "/addIOS";
    String PATH_ADD_PLATFORM_WINDOWS_PHONE = "/addWindowsPhone";
    String PATH_BUILD_APP = "/build";
    String PATH_CREATE_ENTITY = "/createEntity";
    String DB_NAME = "hAPPiDB";
    String DB_PORT = "27017";
}