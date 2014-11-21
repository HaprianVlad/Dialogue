package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * The "sms delivery" broadcast receiver handles the result code (the message was delivered or not)
 * return by the sms manager when sending a sms message.
 */
public final class SmsDeliveryBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    private int mNParts;
    private int partsReceived = 0;

    private boolean hasSucceeded = true;

    public SmsDeliveryBroadcastReceiver() {
        super();

        this.mNParts = 1;
    }

    public SmsDeliveryBroadcastReceiver(int nParts) {
        super();

        if (nParts <= 0) {
            throw new IllegalArgumentException("Need at least 1 part");
        }

        this.mNParts = nParts;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            throw new NullArgumentException("context");
        }

        if (intent == null) {
            throw new NullArgumentException("intent");
        }

        if (intent.getAction().equals(ACTION_SMS_DELIVERED)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    break;
                case Activity.RESULT_CANCELED:
                    hasSucceeded = false;
                    break;
                default:
                    Toast.makeText(context, "Default delivery", Toast.LENGTH_SHORT).show();
                    break;
            }

            partsReceived += 1;
            if (partsReceived == mNParts) {
                if (hasSucceeded) {
                    Toast.makeText(context, "SMS was delivered successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "SMS wasn't delivered", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
