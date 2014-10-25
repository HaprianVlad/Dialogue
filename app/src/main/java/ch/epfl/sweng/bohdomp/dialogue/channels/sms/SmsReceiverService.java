package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueSmsMessage;


/**
 *
 * Service representing an SmsReceiver
 *
 */
public class SmsReceiverService extends IntentService {
    private static final String TAG = "SmsReceiverService";

    // IntentService can perform, RECEIVE_SMS
    public static final String RECEIVE_SMS = "RECEIVE_SMS";

    public SmsReceiverService() {

        super("SmsReceiverService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (RECEIVE_SMS.equals(action)) {
                handleReceivedSms(getMessage(intent));
            }
        }
    }

    /**
     * Handle action RECEIVE_SMS in the provided background thread with the provided
     * parameters.
     */
    private void handleReceivedSms(DialogueSmsMessage receivedDialogueSmsMessage) {
        Toast.makeText(this, receivedDialogueSmsMessage.getBody(),
                Toast.LENGTH_LONG).show();
    }

    /**
     * Extracts the message out of the intent.
     *
     * @param intent containing the message to be sent.
     * @return the message to be sent.
     */
    private DialogueSmsMessage getMessage(Intent intent) {
        if (intent == null) {
            throw new IllegalArgumentException("Intent is null");
        }

        return (DialogueSmsMessage) intent.getExtras().getParcelable("message");
    }


}
