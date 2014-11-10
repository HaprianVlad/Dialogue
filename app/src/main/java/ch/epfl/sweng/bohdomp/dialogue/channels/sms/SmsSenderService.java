package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcel;
import android.telephony.SmsManager;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;


/**
 * The sms sender service sends the message passed to it via the intent and handles the different errors
 * that can occur when sending a sms.
 */
public class SmsSenderService extends IntentService {

    public static final String ACTION_SEND_SMS = "SEND_SMS";
    private static final String ACTION_SMS_SENT = "SMS_SENT";
    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    public static final String MESSAGE = "message";

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
        if (BuildConfig.DEBUG && (intent == null)) {
            throw new AssertionError("intent == null");
        }

        // setIntentRedelivery(true);

        if (intent.getAction().equals(ACTION_SEND_SMS)) {
            DialogueMessage message = getMessage(intent);
            SmsManager.getDefault().sendTextMessage(message.getContact().getPhoneNumbers().iterator().next(), null,
                    message.getBody().getMessageBody(), getSentPendingIntent(), getDeliveryPendingIntent());
        }
    }

    @Override
    public void onDestroy() {
        if (BuildConfig.DEBUG && (mSentBroadcastReceiver == null)) {
            throw new AssertionError("mSentBroadcastReceiver == null");
        }

        if (BuildConfig.DEBUG && (mDeliveryBroadcastReceiver == null)) {
            throw new AssertionError("mDeliveryBroadcastReceiver == null");
        }

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
    private DialogueMessage getMessage(Intent intent) {
        if (BuildConfig.DEBUG && (intent == null)) {
            throw new AssertionError("intent == null");
        }

        Parcel parcel = Parcel.obtain();
        int flag = 1;
        intent.getExtras().getParcelable(MESSAGE).writeToParcel(parcel, flag);

        return  DialogueTextMessage.CREATOR.createFromParcel(parcel);
    }

    /**
     * Creates a "sent" pending intent that will be handled by the "sent" broadcast receiver.
     *
     * @return "sent" pending intent.
     */
    private PendingIntent getSentPendingIntent() {
        if (BuildConfig.DEBUG && (mSentBroadcastReceiver == null)) {
            throw new AssertionError("mSentBroadcastReceiver == null");
        }

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
        if (BuildConfig.DEBUG && (mDeliveryBroadcastReceiver == null)) {
            throw new AssertionError("mDeliveryBroadcastReceiver == null");
        }

        PendingIntent deliveryPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_DELIVERED),
                PendingIntent.FLAG_UPDATE_CURRENT);

        registerReceiver(mDeliveryBroadcastReceiver, new IntentFilter(ACTION_SMS_DELIVERED));

        return deliveryPendingIntent;
    }

}
