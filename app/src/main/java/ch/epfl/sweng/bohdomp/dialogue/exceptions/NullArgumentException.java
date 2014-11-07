package ch.epfl.sweng.bohdomp.dialogue.exceptions;

/**
 * Created by oziegamma on 28.10.14.
 *
 * Inspired by
 * https://commons.apache.org/proper/commons-lang/javadocs/
 *  api-2.6/org/apache/commons/lang/NullArgumentException.html
 * Avoid the pain of complicated IllegalArgumentExceptions
 */
public class NullArgumentException extends IllegalArgumentException {
    public NullArgumentException(Object message) {
        super("Argument can not be null: " + message);

        if (message == null) {
            throw new NullArgumentException("message");
        }
    }
}
