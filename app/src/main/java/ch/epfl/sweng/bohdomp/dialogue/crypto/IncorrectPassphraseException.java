package ch.epfl.sweng.bohdomp.dialogue.crypto;

/**
 * Thrown when a required password was incorrect.
 */
public class IncorrectPassphraseException extends Exception {

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
