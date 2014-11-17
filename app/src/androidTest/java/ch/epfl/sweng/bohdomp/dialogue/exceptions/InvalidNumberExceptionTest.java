package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import android.test.AndroidTestCase;

/**
 * Class representing a test for InvalidNumber Exception
 */
public final class InvalidNumberExceptionTest extends AndroidTestCase {

    public void testNullConstrString() {
        try {
            String s = null;
            new InvalidNumberException(s);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testNullConstrThrowable() {
        try {
            Throwable t = null;
            new InvalidNumberException(t);

            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        DialogueException exception1 = new InvalidNumberException();
        DialogueException exception2 = new InvalidNumberException("message");
        DialogueException exception3 = new InvalidNumberException(new Throwable());
    }
}

