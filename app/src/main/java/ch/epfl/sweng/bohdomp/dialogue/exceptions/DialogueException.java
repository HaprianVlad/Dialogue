package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Class representing an Dialogue exception
 */
public class DialogueException extends Exception {
    private static final long serialVersionUID = 1L;

    public DialogueException() {
        super();
    }

    public DialogueException(String message) {
        super(Contract.throwIfArgNull(message, "message"));
    }

    public DialogueException(Throwable throwable) {
        super(Contract.throwIfArgNull(throwable, "throwable"));
    }

    public DialogueException(String message, Throwable throwable) {
        super(Contract.throwIfArgNull(message, "message"), Contract.throwIfArgNull(throwable, "throwable"));
    }
}
