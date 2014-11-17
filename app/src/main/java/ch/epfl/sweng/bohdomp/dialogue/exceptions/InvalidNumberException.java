package ch.epfl.sweng.bohdomp.dialogue.exceptions;

/**
 * Class representing an excpetion for an invalid number.
 */
public class InvalidNumberException extends DialogueException {
    private static final long serialVersionUID = 1L;

    public InvalidNumberException() {
        super();
    }

    public InvalidNumberException(String message) {
        super(message);

        if (message == null) {
            throw new NullArgumentException("message");
        }
    }

    public InvalidNumberException(Throwable throwable) {
        super(throwable);

        if (throwable == null) {
            throw new NullArgumentException("throwable");
        }
    }
}
