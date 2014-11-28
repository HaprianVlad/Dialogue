package ch.epfl.sweng.bohdomp.dialogue.exceptions;

import android.test.AndroidTestCase;

/**
 * test ContactLookupException
 */
public class ContactLookupExceptionTest extends AndroidTestCase {
    public void testConstrNullString() {
        try {
            new ContactLookupException((String) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullThrowable() {
        try {
            new ContactLookupException((Throwable) null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringValidThrowable() {
        try {
            new ContactLookupException(null, new Throwable());
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrValidStringNullThrowable() {
        try {
            new ContactLookupException("message", null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testConstrNullStringNullThrowable() {
        try {
            new ContactLookupException(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testValidConstr() {
        new ContactLookupException();
    }

    public void testValidConstrString() {
        new ContactLookupException("message");
    }


    public void testValidConstrThrowable() {
        new ContactLookupException(new Throwable());
    }


    public void testValidConstrStringThrowable() {
        new ContactLookupException("message", new Throwable());
    }
}
