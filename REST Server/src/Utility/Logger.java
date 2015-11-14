package Utility;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class Logger {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("hAPPi_Logger");

    public static void INFO(String s){
        logger.info(s);
    }

    public static void SEVERE(String s){
        logger.severe(s);
    }

    public static void WARNING(String s){
        logger.warning(s);
    }
    public static void ERROR(String cause, String message){
        logger.severe(cause + "\n*** " + message);
    }
}