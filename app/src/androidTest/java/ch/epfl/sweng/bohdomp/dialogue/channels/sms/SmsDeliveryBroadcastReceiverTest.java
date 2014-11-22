package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.content.Intent;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

public class SmsDeliveryBroadcastReceiverTest extends AndroidTestCase {
    private SmsDeliveryBroadcastReceiver mReceiver = new SmsDeliveryBroadcastReceiver();

    public void testConstructorValidArgument() {
        try {
            new SmsDeliveryBroadcastReceiver(2);
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    public void testConstructorInvalidArgument() {
        try {
            new SmsDeliveryBroadcastReceiver(0);
            fail();
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    public void testOnReceiveValidContextNullIntent() {
        try {
            mReceiver.onReceive(getContext(), null);
            fail();
        } catch(NullArgumentException e) {
            // OK
        }
    }

    public void testOnReceiveNullContextValidIntent() {
        Intent intent = new Intent(getContext(), SmsDeliveryBroadcastReceiver.class);

        try {
            mReceiver.onReceive(null, intent);
            fail();
        } catch(NullArgumentException e) {
            // OK
        }
    }
}