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
        super(Contract.throwIfNull(message, "message"));
    }

    public InvalidNumberException(Throwable throwable) {
        super(Contract.throwIfNull(throwable, "throwable"));
    }

    public InvalidNumberException(String message, Throwable throwable) {
        super(Contract.throwIfNull(message, "message"), Contract.throwIfNull(throwable, "throwable"));
    }
}
