package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.widget.Toast;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * The "sms sent" broadcast receiver handles the result code (the message was sent or not) returned
 * by the sms manager when sending a sms message.
 */
public final class SmsSentBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "SmsSentBroadcastReceiver";

    private static final String ACTION_SMS_SENT = "SMS_SENT";

    private int mNParts;
    private int partsReceived = 0;

    private boolean hasSucceeded = true;

    public SmsSentBroadcastReceiver() {
        super();

        this.mNParts = 1;
    }

    public SmsSentBroadcastReceiver(int nParts) {
        super();

        Contract.throwIfArg(nParts <= 0, "Need a least 1 part");

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

        if (intent.getAction().equals(ACTION_SMS_SENT)) {
            switch (getResultCode()) {
                case Activity.RESULT_OK:
                    break;
                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                case SmsManager.RESULT_ERROR_NO_SERVICE:
                case SmsManager.RESULT_ERROR_NULL_PDU:
                case SmsManager.RESULT_ERROR_RADIO_OFF:
                    hasSucceeded = false;
                    break;
                default:
                    Toast.makeText(context, R.string.message_defaultSent, Toast.LENGTH_SHORT).show();
                    break;
            }

            partsReceived += 1;

            if (partsReceived == mNParts) {
                if (hasSucceeded) {
                    Toast.makeText(context, R.string.message_sent, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, R.string.message_notSent, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
