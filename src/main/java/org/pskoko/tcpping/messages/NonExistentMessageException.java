package org.pskoko.tcpping.messages;

/**
 * Exception thrown when answer
 * for nonexistent message is received
 * Created by pskoko on 7/28/17.
 */
public class NonExistentMessageException extends Exception {
    public NonExistentMessageException() {
    }

    public NonExistentMessageException(String message) {
        super(message);
    }

    public NonExistentMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public NonExistentMessageException(Throwable cause) {
        super(cause);
    }

    public NonExistentMessageException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
