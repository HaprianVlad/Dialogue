package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import android.test.AndroidTestCase;

/**
 * tests for NoFingerprintException
 */
public class NoFingerprintExceptionTest extends AndroidTestCase {
    public void testConstrNullString() {
        try {
            new NoFingerprintException((String) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullThrowable() {
        try {
            new NoFingerprintException((Throwable) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringValidThrowable() {
        try {
            new NoFingerprintException(null, new Throwable());
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrValidStringNullThrowable() {
        try {
            new NoFingerprintException("message", null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringNullThrowable() {
        try {
            new NoFingerprintException(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        new NoFingerprintException();
    }

    public void testValidConstrString() {
        new NoFingerprintException("message");
    }


    public void testValidConstrThrowable() {
        new NoFingerprintException(new Throwable());
    }


    public void testValidConstrStringThrowable() {
        new NoFingerprintException("message", new Throwable());
    }
}
