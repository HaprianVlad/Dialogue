package ch.epfl.sweng.bohdomp.dialogue.exceptions;


import android.test.AndroidTestCase;


/**
    We are using JUnit 3.0 ...
    http://stackoverflow.com/questions/5912240/android-junit-testing-how-to-expect-an-exception
 */
public final class NullArgumentExceptionTest extends AndroidTestCase {

    public void testNullConstr() {
        try {
            new NullArgumentException(null);

            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {

        }
    }

    public void testValidConstr() {
        new NullArgumentException("name");
    }
}