package ch.epfl.sweng.bohdomp.dialogue.utils;

import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Class representing a test for InvalidNumber Exception
 */
public final class ContractTest extends AndroidTestCase {

    public void testThrowIfArgFalseMsgValid() {
        Contract.throwIfArg(false, "message");
    }

    public void testThrowIfArgFalseMsgNull() {
        try {
            Contract.throwIfArg(false, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testThrowIfArgTrueMsgNull() {
        try {
            Contract.throwIfArg(true, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testThrowIfArgTrueMsgValid() {
        try {
            Contract.throwIfArg(true, "message");
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            //Everything is ok
        }
    }

    public void testThrowIfArgNullObjValidMsgValid() {
        Contract.throwIfArgNull(new Object(), "message");
    }

    public void testThrowIfArgNullObjValidMsgNull() {
        try {
            Contract.throwIfArgNull(new Object(), null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testThrowIfArgNullObjNullMsgNull() {
        try {
            Contract.throwIfArgNull(null, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }

    public void testThrowIfArgNullObjNullMsgValid() {
        try {
            Contract.throwIfArgNull(null, "message");
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //Everything is ok
        }
    }
}

