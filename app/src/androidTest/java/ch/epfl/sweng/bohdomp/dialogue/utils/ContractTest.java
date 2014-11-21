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

    public void testAssertTrueValidConditionValidMsg() {
        Contract.assertTrue(true, "msg");
    }

    public void testAssertTrueValidConditionInvalidMsg() {
        try {
            Contract.assertTrue(true, null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException nullArgumentException) {

        }
    }

    public void testAssertTrueInvalidConditionValidMsg() {
        try {
            Contract.assertTrue(false, "msg");
            fail("expected to throw AssertionError");
        } catch (Throwable e) {
            // this hack is needed since checkstyle doesn't like catching AssertionError
            if (!(e instanceof AssertionError)) {
                fail("expected AssertionError but threw: " + e.getClass());
            }
        }
    }

    public void testAssertTrueInvalidConditionInvalidMsg() {
        try {
            Contract.assertTrue(false, null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {

        }
    }

    public void testAssertFalseValidConditionValidMsg() {
        Contract.assertFalse(false, "msg");
    }

    public void testAssertFalseValidConditionInvalidMsg() {
        try {
            Contract.assertFalse(false, null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException nullArgumentException) {

        }
    }

    public void testAssertFalseInvalidConditionValidMsg() {
        try {
            Contract.assertFalse(true, "msg");
            fail("expected to throw AssertionError");
        } catch (Throwable e) {
            // this hack is needed since checkstyle doesn't like catching AssertionError
            if (!(e instanceof AssertionError)) {
                fail("expected AssertionError but threw: " + e.getClass());
            }
        }
    }

    public void testAssertFalseInvalidConditionInvalidMsg() {
        try {
            Contract.assertFalse(true, null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {

        }
    }

    public void testAssertNotNullValidArgValidMsg() {
        Contract.assertNotNull(new Object(), "msg");
    }

    public void testAssertNotNullValidArgInvalidMsg() {
        try {
            Contract.assertNotNull(new Object(), null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {

        }
    }

    public void testAssertionNotNullInvalidArgValidMsg() {
        try {
            Contract.assertNotNull(null, "msg");
            fail("expected to throw AssertionError");
        } catch (Throwable e) {
            // this hack is needed since checkstyle doesn't like catching AssertionError
            if (!(e instanceof AssertionError)) {
                fail("expected AssertionError but threw: " + e.getClass());
            }
        }
    }

    public void testAssertionNotNullInvalidArgInvalidMsg() {
        try {
            Contract.assertNotNull(null, null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {

        }
    }
}

