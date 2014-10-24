package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;

import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueSmsMessage;

/**
 * The sms sender service sends the message passed to it via the intent and handles the different errors
 * that can occur when sending a sms.
 */
public class SmsSenderService extends IntentService {
    private static final String ACTION_SMS_SENT = "SMS_SENT";
    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";
    private static final String ACTION_SEND_SMS = "SEND_SMS";

    private static final String MESSAGE_BODY = "message";

    private BroadcastReceiver mSentBroadcastReceiver = new SmsSentBroadcastReceiver();
    private BroadcastReceiver mDeliveryBroadcastReceiver = new SmsDeliveryBroadcastReceiver();

    public SmsSenderService() {
        super("SmsSenderService");
    }

    /**
     * Sends the message passed through the intent.
     *
     * @param intent containing the message to be sent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        // setIntentRedelivery(true);

        if (intent.getAction().equals(ACTION_SEND_SMS)) {
            DialogueSmsMessage smsDialogueSmsMessage = getMessage(intent);

            SmsManager.getDefault().sendTextMessage(smsDialogueSmsMessage.getPhoneNumber(), null,
                    smsDialogueSmsMessage.getBody(), getSentPendingIntent(), getDeliveryPendingIntent());
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mSentBroadcastReceiver);
        unregisterReceiver(mDeliveryBroadcastReceiver);

        super.onDestroy();
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

        return (DialogueSmsMessage) intent.getExtras().getParcelable(MESSAGE_BODY);
    }

    /**
     * Creates a "sent" pending intent that will be handled by the "sent" broadcast receiver.
     *
     * @return "sent" pending intent.
     */
    private PendingIntent getSentPendingIntent() {

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_SENT),
                PendingIntent.FLAG_UPDATE_CURRENT);

        registerReceiver(mSentBroadcastReceiver, new IntentFilter(ACTION_SMS_SENT));

        return sentPendingIntent;
    }

    /**
     * Creates a "delivery" pending intent that will be handles by the "delivery" broadcast receiver.
     *
     * @return "delivery" pending intent.
     */
    private PendingIntent getDeliveryPendingIntent() {

        PendingIntent deliveryPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_DELIVERED),
                PendingIntent.FLAG_UPDATE_CURRENT);

        registerReceiver(mDeliveryBroadcastReceiver, new IntentFilter(ACTION_SMS_DELIVERED));

        return deliveryPendingIntent;
    }

}
