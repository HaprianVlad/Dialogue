package ch.epfl.sweng.bohdomp.dialogue.exceptions;

/**
 * Class representing a null argument exception
 * Inspired by
 * https://commons.apache.org/proper/commons-lang/javadocs/
 *  api-2.6/org/apache/commons/lang/NullArgumentException.html
 * Avoid the pain of complicated IllegalArgumentExceptions
 */
public class NullArgumentException extends IllegalArgumentException {

    private static final long serialVersionUID = 1L;

    public NullArgumentException(String message) {
        super("Argument can not be null: " + message);

        if (message == null) {
            throw new NullArgumentException("message");
        }
    }
}
