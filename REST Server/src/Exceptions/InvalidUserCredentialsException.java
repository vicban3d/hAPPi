package exceptions;

import utility.Logger;

/**
 * Created by victor on 5/18/2016.
 */
public class InvalidUserCredentialsException extends Throwable {
    public InvalidUserCredentialsException(Exception e) {
        Logger.ERROR("Failed to run Cordova command!", e);
    }
}
