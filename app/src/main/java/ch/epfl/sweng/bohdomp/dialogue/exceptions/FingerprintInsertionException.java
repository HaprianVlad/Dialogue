package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * exception for failed fingerprint insertions
 */
public class FingerprintInsertionException extends DialogueException {
    private static final long serialVersionUID = 1L;

    public FingerprintInsertionException() {
        super();
    }

    public FingerprintInsertionException(String message) {
        super(Contract.throwIfArgNull(message, "message"));
    }

    public FingerprintInsertionException(Throwable throwable) {
        super(Contract.throwIfArgNull(throwable, "throwable"));
    }

    public FingerprintInsertionException(String message, Throwable throwable) {
        super(Contract.throwIfArgNull(message, "message"), Contract.throwIfArgNull(throwable, "throwable"));
    }
}
