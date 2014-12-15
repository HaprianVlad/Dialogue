package ch.epfl.sweng.bohdomp.dialogue.crypto;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Thrown when a high-level cryptographic function fails.
 */
public class CryptoException extends Exception {

    private static final long serialVersionUID = 1L;

    public CryptoException() {
        super();
    }

    public CryptoException(String message) {
        super(Contract.throwIfArgNull(message, "message"));
    }

    public CryptoException(Throwable throwable) {
        super(Contract.throwIfArgNull(throwable, "throwable"));
    }

    public CryptoException(String message, Throwable throwable) {
        super(Contract.throwIfArgNull(message, "message"), Contract.throwIfArgNull(throwable, "throwable"));
    }
}
