package org.pskoko.tcpping.arguments;

/**
 * Exception thrown when there are errors in command line arguments
 * Created by pskoko on 7/31/17.
 */
public class ArgumentsUtilityException extends Exception {

    public ArgumentsUtilityException() {
        super();
    }

    public ArgumentsUtilityException(String message) {
        super(message);
    }

    public ArgumentsUtilityException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentsUtilityException(Throwable cause) {
        super(cause);
    }

    protected ArgumentsUtilityException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
