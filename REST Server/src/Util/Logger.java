package Util;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class Logger {

    public static java.util.logging.Logger logger = java.util.logging.Logger.getLogger("hAPPi_Logger");

    public static void logINFO(String s){
        logger.info(s);
    }

    public static void logSEVERE(String s){
        logger.severe(s);
    }

    public static void logWARNING(String s){
        logger.warning(s);
    }

    public static void logERROR(String cause, String message){
        logger.severe(cause + "\n*** " + message);
    }
}
