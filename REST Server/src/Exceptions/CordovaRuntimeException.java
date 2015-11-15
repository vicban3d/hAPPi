package Exceptions;

import Utility.Logger;

/**
 * Created by victor on 11/14/2015.
 *
 */
public class CordovaRuntimeException extends Exception {
    public CordovaRuntimeException(String message) {
        super(message);
        Logger.ERROR("Failed to run Cordova command!", message);
    }
}
