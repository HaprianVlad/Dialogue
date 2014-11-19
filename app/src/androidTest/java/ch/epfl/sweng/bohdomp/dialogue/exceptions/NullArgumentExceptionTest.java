package ch.epfl.sweng.bohdomp.dialogue.exceptions;


import android.test.AndroidTestCase;


/**
    We are using JUnit 3.0 ...
    http://stackoverflow.com/questions/5912240/android-junit-testing-how-to-expect-an-exception
 */
public final class NullArgumentExceptionTest extends AndroidTestCase {
    public void testConstrNullString() {
        try {
            new NullArgumentException((String) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullThrowable() {
        try {
            new NullArgumentException((Throwable) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringValidThrowable() {
        try {
            new NullArgumentException(null, new Throwable());
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrValidStringNullThrowable() {
        try {
            new NullArgumentException("message", null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringNullThrowable() {
        try {
            new NullArgumentException(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        new NullArgumentException();
    }

    public void testValidConstrString() {
        new NullArgumentException("message");
    }


    public void testValidConstrThrowable() {
        new NullArgumentException(new Throwable());
    }


    public void testValidConstrStringThrowable() {
        new NullArgumentException("message", new Throwable());
    }
}