package utility;


import java.util.GregorianCalendar;

/**
 * Created by victor on 11/9/2015.
 *
 */
public class Logger {

    private static final boolean DEBUG = true;

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger("hAPPi_Logger");

    public static void DEBUG(String s){
        if (DEBUG) {
            System.out.println("[" + new GregorianCalendar().getTime() + "] DEBUG:");
            System.out.println(s);
        }
    }

    public static void SEVERE(String s){
        logger.severe(s);
    }

    public static void ERROR(String cause, Throwable e){
        System.err.println("ERROR: " + cause);
        e.printStackTrace();
    }
}