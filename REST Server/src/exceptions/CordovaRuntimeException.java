package exceptions;

import utility.Logger;

/**
 * Created by victor on 11/14/2015.
 *
 */
public class CordovaRuntimeException extends Throwable {
    public CordovaRuntimeException(Throwable e) {
        Logger.ERROR("Failed to run Cordova command!", e);
    }
}
