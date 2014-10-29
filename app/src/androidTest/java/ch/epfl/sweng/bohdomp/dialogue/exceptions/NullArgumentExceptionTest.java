package ch.epfl.sweng.bohdomp.dialogue.exceptions;


import android.test.AndroidTestCase;

import junit.framework.Assert;

/**
    We are using JUnit 3.0 ...
    http://stackoverflow.com/questions/5912240/android-junit-testing-how-to-expect-an-exception
 */
public class NullArgumentExceptionTest extends AndroidTestCase {

    public void testNullConstr() {
        try {
            new NullArgumentException(null);
            Assert.fail();
        } catch (NullArgumentException e) {

        }
    }

    public void testValidConstr() {
        new NullArgumentException("name");
    }
}