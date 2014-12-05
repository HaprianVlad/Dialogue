package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * exception for trying to get non-existing fingerpint
 */
public class NoFingerprintException extends UnsupportedOperationException {
    private static final long serialVersionUID = 1L;

    public NoFingerprintException() {
        super();
    }

    public NoFingerprintException(String message) {
        super(Contract.throwIfArgNull(message, "message"));
    }

    public NoFingerprintException(Throwable throwable) {
        super(Contract.throwIfArgNull(throwable, "throwable"));
    }

    public NoFingerprintException(String message, Throwable throwable) {
        super(Contract.throwIfArgNull(message, "message"), Contract.throwIfArgNull(throwable, "throwable"));
    }
}
