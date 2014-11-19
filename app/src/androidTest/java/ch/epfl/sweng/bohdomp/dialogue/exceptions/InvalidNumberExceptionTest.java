package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import android.test.AndroidTestCase;

/**
 * Class representing a test for InvalidNumber Exception
 */
public final class InvalidNumberExceptionTest extends AndroidTestCase {

    public void testConstrNullString() {
        try {
            new InvalidNumberException((String) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullThrowable() {
        try {
            new InvalidNumberException((Throwable) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringValidThrowable() {
        try {
            new InvalidNumberException(null, new Throwable());
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrValidStringNullThrowable() {
        try {
            new InvalidNumberException("message", null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringNullThrowable() {
        try {
            new InvalidNumberException(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        new InvalidNumberException();
    }

    public void testValidConstrString() {
        new InvalidNumberException("message");
    }


    public void testValidConstrThrowable() {
        new InvalidNumberException(new Throwable());
    }


    public void testValidConstrStringThrowable() {
        new InvalidNumberException("message", new Throwable());
    }
}

