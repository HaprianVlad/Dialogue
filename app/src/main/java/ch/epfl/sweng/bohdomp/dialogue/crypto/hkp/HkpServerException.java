package ch.epfl.sweng.bohdomp.dialogue.crypto.hkp;

/**
 * Thrown when a keyserver did something unexpected, not conforming to the HKP protocol
 */
public class HkpServerException extends Exception {

    private static final long serialVersionUID = 1L;

    public HkpServerException() {
        super();
    }

    public HkpServerException(String message) {
        super(message);
    }

    public HkpServerException(String message, Throwable cause) {
        super(message, cause);
    }

    public HkpServerException(Throwable cause) {
        super(cause);
    }

}
