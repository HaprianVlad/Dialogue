package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import android.test.AndroidTestCase;


/**
 * Class representing a test for Dialogue Exception
 */
public final class DialogueExceptionTest extends AndroidTestCase {

    public void testNullConstrString() {
        try {
            String s = null;
            new DialogueException(s);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testNullConstrThrowable() {
        try {
            Throwable s = null;
            new DialogueException(s);

            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        DialogueException exception1 = new DialogueException();
        DialogueException exception2 = new DialogueException("message");
        DialogueException exception3 = new DialogueException(new Throwable());
    }
}

