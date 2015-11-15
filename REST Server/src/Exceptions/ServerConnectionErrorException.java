package Exceptions;

import Utility.Logger;

/**
 * Created by victor on 11/14/2015.
 *
 */
public class ServerConnectionErrorException extends Exception {

    public ServerConnectionErrorException(String message){
        super(message);
        Logger.ERROR("Server Connection Error!", message);
    }
}
