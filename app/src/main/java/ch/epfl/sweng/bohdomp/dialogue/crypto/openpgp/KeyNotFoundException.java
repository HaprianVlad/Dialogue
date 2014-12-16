package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Thrown when a key cannot be found.
 */
public class KeyNotFoundException extends Exception {
    private static final long serialVersionUID = 1L;

    public KeyNotFoundException() {
        super();
    }

    public KeyNotFoundException(String message) {
        super(Contract.throwIfArgNull(message, "message"));
    }

    public KeyNotFoundException(Throwable throwable) {
        super(Contract.throwIfArgNull(throwable, "throwable"));
    }

    public KeyNotFoundException(String message, Throwable throwable) {
        super(Contract.throwIfArgNull(message, "message"), Contract.throwIfArgNull(throwable, "throwable"));
    }
}
