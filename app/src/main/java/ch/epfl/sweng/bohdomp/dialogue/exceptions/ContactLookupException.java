package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Exception for failed contact look-ups
 */
public class ContactLookupException extends DialogueException {
    private static final long serialVersionUID = 1L;

    public ContactLookupException() {
        super();
    }

    public ContactLookupException(String message) {
        super(Contract.throwIfArgNull(message, "message"));
    }

    public ContactLookupException(Throwable throwable) {
        super(Contract.throwIfArgNull(throwable, "throwable"));
    }

    public ContactLookupException(String message, Throwable throwable) {
        super(Contract.throwIfArgNull(message, "message"), Contract.throwIfArgNull(throwable, "throwable"));
    }
}
