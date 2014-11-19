package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Class representing a null argument exception
 * Inspired by
 * https://commons.apache.org/proper/commons-lang/javadocs/
 *  api-2.6/org/apache/commons/lang/NullArgumentException.html
 * Avoid the pain of complicated IllegalArgumentExceptions
 */
public class NullArgumentException extends IllegalArgumentException {
    private static final long serialVersionUID = 1L;

    public NullArgumentException() {
        super();
    }

    public NullArgumentException(String message) {
        super(Contract.throwIfNull(message, "message"));
    }

    public NullArgumentException(Throwable throwable) {
        super(Contract.throwIfNull(throwable, "throwable"));
    }

    public NullArgumentException(String message, Throwable throwable) {
        super(Contract.throwIfNull(message, "message"), Contract.throwIfNull(throwable, "throwable"));
    }

    private static String makeMessage(String message) {
        return "Argument can not be null: " + Contract.throwIfNull(message, "message");
    }
}
