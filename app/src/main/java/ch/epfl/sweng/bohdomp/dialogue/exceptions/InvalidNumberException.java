package ch.epfl.sweng.bohdomp.dialogue.exceptions;

/**
 * Class representing an exception for an invalid number.
 */
public class InvalidNumberException extends DialogueException {
    private static final long serialVersionUID = 1L;

    public InvalidNumberException() {
        super();
    }

    public InvalidNumberException(String message) {
        super(message);
    }

    public InvalidNumberException(Throwable throwable) {
        super(throwable);

    }
}
