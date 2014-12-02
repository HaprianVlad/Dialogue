package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

/**
 * Thrown when a required password was incorrect.
 */
public class IncorrectPassphraseException extends Exception {
    private static final long serialVersionUID = 1L;

    IncorrectPassphraseException() {
        super();
    }

    IncorrectPassphraseException(String message) {
        super(message);
    }

    IncorrectPassphraseException(String message, Throwable cause) {
        super(message, cause);
    }

    IncorrectPassphraseException(Throwable cause) {
        super(cause);
    }

}
