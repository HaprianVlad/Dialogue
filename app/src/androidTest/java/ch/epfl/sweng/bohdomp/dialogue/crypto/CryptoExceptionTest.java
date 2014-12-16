package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * tests for FingerprintInsertionExceptions
 */
public class CryptoExceptionTest extends AndroidTestCase {

    public void testConstrNullString() {
        try {
            new CryptoException((String) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullThrowable() {
        try {
            new CryptoException((Throwable) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringValidThrowable() {
        try {
            new CryptoException(null, new Throwable());
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrValidStringNullThrowable() {
        try {
            new CryptoException("message", null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringNullThrowable() {
        try {
            new CryptoException(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        new CryptoException();
    }

    public void testValidConstrString() {
        new CryptoException("message");
    }


    public void testValidConstrThrowable() {
        new CryptoException(new Throwable());
    }


    public void testValidConstrStringThrowable() {
        new CryptoException("message", new Throwable());
    }
}
