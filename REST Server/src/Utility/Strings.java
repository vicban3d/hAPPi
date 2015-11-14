package Utility;

/**
 * Created by victor on 11/9/2015.
 *
 */

/**
 * An interface containing all String Constants for the system.
 */
public interface Strings {
    String SRV_HOST = "http://localhost";
    String SRV_PORT = "9998";
    String SRV_MAIN = "/hAPPi";
    String SRV_FULL = SRV_HOST + ":" + SRV_PORT + SRV_MAIN;
    String PATH_WEB_CONTENT = "C:\\users\\victor\\IdeaProjects\\hAPPi\\REST Server\\src\\Web\\";
    String PATH_CORDOVA = "C:\\users\\victor\\AppData\\Roaming\\npm\\cordova.cmd";
    String PATH_PROJECTS = "C:\\users\\victor\\HAPPI\\Projects";
    String PATH_MAIN = "/main";
    String PATH_CREATE_PROJECT = "/createProject";
    String PATH_ADD_PLATFORM_ANDROID = "/addAndroid";
    String PATH_ADD_PLATFORM_IOS = "/addIOS";
    String PATH_ADD_PLATFORM_WINDOWS_PHONE = "/addWindowsPhone";
    String PATH_BUILD_PROJECT = "/build";
    String PATH_CREATE_ENTITY = "/createEntity";
    String DB_NAME = "hAPPiDB";
    String DB_PORT = "27017";
}