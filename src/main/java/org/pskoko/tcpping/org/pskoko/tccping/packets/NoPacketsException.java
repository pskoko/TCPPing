package org.pskoko.tcpping.org.pskoko.tccping.packets;

/**
 * Exception thrown when there are no more packets to receive
 * Created by pskoko on 7/31/17.
 */
public class NoPacketsException extends Exception{
    public NoPacketsException() {
        super();
    }

    public NoPacketsException(String message) {
        super(message);
    }

    public NoPacketsException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPacketsException(Throwable cause) {
        super(cause);
    }

    protected NoPacketsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
