package ch.epfl.sweng.bohdomp.dialogue.exceptions;

/**
 * Class representing an Dialogue exception
 */
public class DialogueException extends Exception {
    private static final long serialVersionUID = 1L;

    public DialogueException() {
        super();
    }

    public DialogueException(String message) {
        super(message);

        if (message == null) {
            throw new NullArgumentException("message");
        }
    }

    public DialogueException(Throwable throwable) {
        super(throwable);

        if (throwable == null) {
            throw new NullArgumentException("throwable");
        }


    }
}
