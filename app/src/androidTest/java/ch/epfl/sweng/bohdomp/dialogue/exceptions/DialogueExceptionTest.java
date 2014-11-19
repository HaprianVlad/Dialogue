package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import android.test.AndroidTestCase;


/**
 * Class representing a test for Dialogue Exception
 */
public final class DialogueExceptionTest extends AndroidTestCase {

    public void testConstrNullString() {
        try {
            new DialogueException((String) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullThrowable() {
        try {
            new DialogueException((Throwable) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringValidThrowable() {
        try {
            new DialogueException(null, new Throwable());
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrValidStringNullThrowable() {
        try {
            new DialogueException("message", null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringNullThrowable() {
        try {
            new DialogueException(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        new DialogueException();
    }

    public void testValidConstrString() {
        new DialogueException("message");
    }


    public void testValidConstrThrowable() {
        new DialogueException(new Throwable());
    }


    public void testValidConstrStringThrowable() {
        new DialogueException("message", new Throwable());
    }
}

