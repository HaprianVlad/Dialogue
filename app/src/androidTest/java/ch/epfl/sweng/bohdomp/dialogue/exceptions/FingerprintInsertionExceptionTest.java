package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import android.test.AndroidTestCase;

/**
 * tests for FingerprintInsertionExceptions
 */
public class FingerprintInsertionExceptionTest extends AndroidTestCase {

    public void testConstrNullString() {
        try {
            new FingerprintInsertionException((String) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullThrowable() {
        try {
            new FingerprintInsertionException((Throwable) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringValidThrowable() {
        try {
            new FingerprintInsertionException(null, new Throwable());
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrValidStringNullThrowable() {
        try {
            new FingerprintInsertionException("message", null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringNullThrowable() {
        try {
            new FingerprintInsertionException(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        new FingerprintInsertionException();
    }

    public void testValidConstrString() {
        new FingerprintInsertionException("message");
    }


    public void testValidConstrThrowable() {
        new FingerprintInsertionException(new Throwable());
    }


    public void testValidConstrStringThrowable() {
        new FingerprintInsertionException("message", new Throwable());
    }
}
