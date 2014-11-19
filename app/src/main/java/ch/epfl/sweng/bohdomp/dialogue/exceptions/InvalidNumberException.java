package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Class representing an exception for an invalid number.
 */
public class InvalidNumberException extends DialogueException {
    private static final long serialVersionUID = 1L;

    public InvalidNumberException() {
        super();
    }

    public InvalidNumberException(String message) {
        super(Contract.throwIfArgNull(message, "message"));
    }

    public InvalidNumberException(Throwable throwable) {
        super(Contract.throwIfArgNull(throwable, "throwable"));
    }

    public InvalidNumberException(String message, Throwable throwable) {
        super(Contract.throwIfArgNull(message, "message"), Contract.throwIfArgNull(throwable, "throwable"));
    }
}
