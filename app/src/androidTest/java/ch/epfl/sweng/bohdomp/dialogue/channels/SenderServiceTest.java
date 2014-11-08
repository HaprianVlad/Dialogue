package ch.epfl.sweng.bohdomp.dialogue.channels;

import android.app.IntentService;
import android.content.Intent;
import android.test.AndroidTestCase;

import junit.framework.Assert;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Test for ch.epfl.sweng.bohdomp.dialogue.channels.SenderService
 */
public class SenderServiceTest extends AndroidTestCase {

    /**
     * Mock implementation of ch.epfl.sweng.bohdomp.dialogue.channels.SenderService
     */
    private class TestImplSenderService extends IntentService {
        public TestImplSenderService(String name) {
            super(name);
        }

        @Override
        protected void onHandleIntent(Intent intent) {
            // Do nothing, we are testing only implemented functions of the abstract class
        }
    }

    public void testNullNameConstr() {
        try {
            new TestImplSenderService(null);
            Assert.fail();
        } catch (NullArgumentException e) {

        }
    }

    public void testValidNameConstr() {
        new TestImplSenderService("SmsSenderService");
    }
}