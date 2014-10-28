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
public class SmsDeliveryBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            throw new NullArgumentException("context");
        }

        if (context == null) {
            throw new NullArgumentException("intent");
        }


        if (intent.getAction().equals(ACTION_SMS_DELIVERED)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    Toast.makeText(context, "SMS delivered",
                            Toast.LENGTH_SHORT).show();
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "SMS not delivered",
                            Toast.LENGTH_SHORT).show();
                    break;

                default:
                    Toast.makeText(context, "Default delivery",
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
