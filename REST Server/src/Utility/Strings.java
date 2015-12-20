package Utility;

/**
 * Created by victor on 11/9/2015.
 *
 */

import java.io.IOException;
import java.net.InetAddress;

/**
 * An interface containing all String Constants for the system.
 */
public class Strings {
    private static final String username = System.getProperty("user.name");
    public static final String SRV_IP = "132.72.665.110";
    public static final String SRV_HOST;
    static{
        String temp;
        try {
            temp = (InetAddress.getByName(SRV_IP).isReachable(10)) ? ("http://" + SRV_IP) : ("http://localhost");
        } catch (IOException e) {
            temp = "http://localhost";
        }
        SRV_HOST = temp;
        System.out.println(SRV_HOST);
    }
    public static final String SRV_PORT = "9998";
    public static final String SRV_MAIN = "/hAPPi";
    public static final String SRV_FULL = SRV_HOST + ":" + SRV_PORT + SRV_MAIN;
    public static final String PATH_WEB_CONTENT = "C:\\users\\" + username + "\\IdeaProjects\\hAPPi\\REST Server\\src\\Web\\";
    public static final String PATH_CORDOVA = "C:\\users\\" + username + "\\AppData\\Roaming\\npm\\cordova.cmd";
    public static final String PATH_APPS = "C:\\users\\" + username + "\\HAPPI\\Applications";

    public static final String PATH_MAIN = "/main";
    public static final String PATH_EMULATE_ANDROID = "/emulateAndroid";
    public static final String PATH_CREATE_APP = "/createApplication";
    public static final String PATH_ADD_PLATFORM_ANDROID = "/addAndroid";
    public static final String PATH_ADD_PLATFORM_IOS = "/addIOS";
    public static final String PATH_ADD_PLATFORM_WINDOWS_PHONE = "/addWindowsPhone";
    public static final String PATH_BUILD_APP = "/build";
    public static final String PATH_CREATE_ENTITY = "/createEntity";
    public static final String PATH_REMOVE_ENTITY = "/removeEntity";
    public static final String PATH_REMOVE_APP = "/removeApplication";
    public static final String DB_NAME = "hAPPiDB";
    public static final String DB_PORT = "27017";
}