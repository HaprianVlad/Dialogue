package ch.epfl.sweng.bohdomp.dialogue.crypto.hkp;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Thrown when a keyserver did something unexpected, not conforming to the HKP protocol
 */
public class HkpServerException extends Exception {

    private static final long serialVersionUID = 1L;

    public HkpServerException() {
        super();
    }

    public HkpServerException(String message) {
        super(Contract.throwIfArgNull(message, "message"));
    }

    public HkpServerException(Throwable throwable) {
        super(Contract.throwIfArgNull(throwable, "throwable"));
    }

    public HkpServerException(String message, Throwable throwable) {
        super(Contract.throwIfArgNull(message, "message"), Contract.throwIfArgNull(throwable, "throwable"));
    }
}
