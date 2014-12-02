package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.os.Bundle;
import android.os.ResultReceiver;
import android.test.ServiceTestCase;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * Class creating a Tester for a Dialogue Incoming Dispatcher
 */
public final class CryptoServiceTest extends ServiceTestCase<CryptoService> {

    //timeout in ms for tests
    private static final int TIMEOUT = 1000;

    private String mMessage;

    private void assertComplete(CountDownLatch latch) throws Exception {
        boolean complete = latch.await(TIMEOUT, TimeUnit.MILLISECONDS);
        if (!complete) {
            fail("Timed out while awaiting result from service.");
        }
    }

    public CryptoServiceTest() {
        super(CryptoService.class);

        mMessage = "test message";
    }

    public void testEncryption() throws Exception {
        setupService();
        final CountDownLatch latch = new CountDownLatch(1);

        CryptoService.startActionEncrypt(mContext, TestKeyData.FINGERPRINT, mMessage, new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Assert.assertEquals(CryptoService.RESULT_SUCCESS, resultCode);
                latch.countDown();
            }
        });

        assertComplete(latch);
    }

    public void testLoop() throws Exception {
        setupService();
        final CountDownLatch encrypted = new CountDownLatch(1);
        final CountDownLatch decrypted = new CountDownLatch(1);

        final ResultReceiver decryptionReceiver = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Assert.assertEquals(CryptoService.RESULT_SUCCESS, resultCode);
                Assert.assertEquals(mMessage, resultData.getString(CryptoService.EXTRA_CLEAR_TEXT));
                decrypted.countDown();
            }
        };

        ResultReceiver encryptionReceiver = new ResultReceiver(null) {
            @Override
            protected void onReceiveResult(int resultCode, Bundle resultData) {
                Assert.assertEquals(CryptoService.RESULT_SUCCESS, resultCode);
                CryptoService.startActionDecrypt(mContext, resultData.getString(CryptoService.EXTRA_ENCRYPTED_TEXT),
                        decryptionReceiver);
                encrypted.countDown();
            }
        };

        CryptoService.startActionEncrypt(mContext, TestKeyData.FINGERPRINT, mMessage, encryptionReceiver);

        assertComplete(encrypted);
        assertComplete(decrypted);
    }

}
