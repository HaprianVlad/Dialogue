package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Tests the DataMessageBody.
 */
public class DataMessageBodyTest extends AndroidTestCase {
    public void testNullMessageBody() {
        try {
            MessageBody mMessageBody = new DataMessageBody(null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            // success
        }
    }
}