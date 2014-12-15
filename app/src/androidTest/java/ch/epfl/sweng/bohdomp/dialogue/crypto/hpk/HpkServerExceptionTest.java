package ch.epfl.sweng.bohdomp.dialogue.crypto.hpk;

import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.crypto.hkp.HkpServerException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.ContactLookupException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * test ContactLookupException
 */
public class HpkServerExceptionTest extends AndroidTestCase {
    public void testConstrNullString() {
        try {
            new HkpServerException((String) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullThrowable() {
        try {
            new HkpServerException((Throwable) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringValidThrowable() {
        try {
            new HkpServerException(null, new Throwable());
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrValidStringNullThrowable() {
        try {
            new HkpServerException("message", null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringNullThrowable() {
        try {
            new HkpServerException(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        new HkpServerException();
    }

    public void testValidConstrString() {
        new HkpServerException("message");
    }


    public void testValidConstrThrowable() {
        new HkpServerException(new Throwable());
    }


    public void testValidConstrStringThrowable() {
        new HkpServerException("message", new Throwable());
    }
}
