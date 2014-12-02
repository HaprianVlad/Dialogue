package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;

import java.util.ArrayList;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * The sms sender service sends the message passed to it via the intent and handles the different errors
 * that can occur when sending a sms.
 */
public class SmsSenderService extends IntentService {
    public static final String ACTION_SEND_SMS = "SEND_SMS";

    private static final String ACTION_SMS_SENT = "SMS_SENT";
    private static final String ACTION_SMS_DELIVERED = "SMS_DELIVERED";

    private BroadcastReceiver mSentBroadcastReceiver = new SmsSentBroadcastReceiver();
    private BroadcastReceiver mDeliveryBroadcastReceiver = new SmsDeliveryBroadcastReceiver();

    private SmsManager mSmsManager = SmsManager.getDefault();

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
        Contract.assertNotNull(intent, "intent");

        if (intent.getAction().equals(ACTION_SEND_SMS)) {
            DialogueMessage message = DialogueMessage.extractMessage(intent);
            sendMessage(message);
        }
    }

    private void sendMessage(DialogueMessage message) {
        Contract.assertNotNull(message, "message");

        if (!needsPartitioning(message)) {
            sendMonoPartMessage(message);
        } else {
            ArrayList<String> messages = mSmsManager.divideMessage(message.getBody().getMessageBody());

            mSentBroadcastReceiver = new SmsSentBroadcastReceiver(messages.size());
            mDeliveryBroadcastReceiver = new SmsDeliveryBroadcastReceiver(messages.size());

            Contact.PhoneNumber number = message.getPhoneNumber();
            sendMultiPartMessage(message, messages, number);
        }
    }

    private void sendMultiPartMessage(DialogueMessage message, ArrayList<String> messages, Contact.PhoneNumber number) {
        Contract.assertNotNull(number, "number");

        mSmsManager.sendMultipartTextMessage(number.number(), null, messages,
                getSentPendingIntentList(message, messages.size()),
                getDeliveredPendingIntentList(message, messages.size()));
    }

    private void sendMonoPartMessage(DialogueMessage message) {
        String phoneNumber = message.getPhoneNumber().number();
        String messageBody = message.getBody().getMessageBody();

        mSentBroadcastReceiver = new SmsDeliveryBroadcastReceiver();
        mDeliveryBroadcastReceiver = new SmsDeliveryBroadcastReceiver();

        mSmsManager.sendTextMessage(phoneNumber, null, messageBody,
                getSentPendingIntent(message), getDeliveryPendingIntent(message));
    }

    private boolean needsPartitioning(DialogueMessage message) {
        Contract.throwIfArgNull(message, "message");

        return message.getBody().getMessageBody().getBytes().length <= SmsMessage.MAX_USER_DATA_BYTES;
    }

    @Override
    public void onDestroy() {
        Contract.assertNotNull(mSentBroadcastReceiver, "mSentBroadcastReceiver");
        Contract.assertNotNull(mDeliveryBroadcastReceiver, "mDeliveryBroadcastReceiver");

        try {
            unregisterReceiver(mSentBroadcastReceiver);
            unregisterReceiver(mDeliveryBroadcastReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            //It can happen only during testing
        }

        super.onDestroy();
    }

    /**
     * Creates a "sent" pending intent that will be handled by the "sent" broadcast receiver.
     *
     * @return "sent" pending intent.
     */
    private PendingIntent getSentPendingIntent() {
        Contract.assertNotNull(mSentBroadcastReceiver, "mSentBroadcastReceiver");

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_SENT),
                PendingIntent.FLAG_UPDATE_CURRENT);

        registerReceiver(mSentBroadcastReceiver, new IntentFilter(ACTION_SMS_SENT));

        return sentPendingIntent;
    }

    private PendingIntent getSentPendingIntent(DialogueMessage message) {
        Contract.throwIfArgNull(message, "message");

        Intent intent = new Intent(getApplicationContext(), SmsSentBroadcastReceiver.class);
        intent.setAction(SmsSenderService.ACTION_SMS_SENT);
        intent.putExtra(DialogueMessage.MESSAGE, message);

        PendingIntent sentPendingIntent = PendingIntent.getBroadcast(this, 0, intent,
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
        Contract.assertNotNull(mDeliveryBroadcastReceiver, "mDeliveryBroadcastReceiver");

        PendingIntent deliveryPendingIntent = PendingIntent.getBroadcast(this, 0, new Intent(ACTION_SMS_DELIVERED),
                PendingIntent.FLAG_UPDATE_CURRENT);

        registerReceiver(mDeliveryBroadcastReceiver, new IntentFilter(ACTION_SMS_DELIVERED));

        return deliveryPendingIntent;
    }

    private PendingIntent getDeliveryPendingIntent(DialogueMessage message) {
        Contract.throwIfArgNull(message, "message");

        Intent intent = new Intent(getApplicationContext(), SmsDeliveryBroadcastReceiver.class);
        intent.setAction(SmsSenderService.ACTION_SMS_DELIVERED);
        intent.putExtra(DialogueMessage.MESSAGE, message);

        PendingIntent deliveryPendingIntent = PendingIntent.getBroadcast(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        registerReceiver(mDeliveryBroadcastReceiver, new IntentFilter(ACTION_SMS_DELIVERED));

        return deliveryPendingIntent;
    }

    private ArrayList<PendingIntent> getSentPendingIntentList(DialogueMessage message, int copies) {
        Contract.throwIfArgNull(message, "message");
        Contract.throwIfArg(copies <= 0, "Copies should be at least 1");

        ArrayList<PendingIntent> list = new ArrayList<PendingIntent>();

        /*
        In order to know which message has been delivered we pass it
        with the last pending intent so that the receiver can set the flag.
         */
        for (int i = 0; i < copies - 1; i++) {
            list.add(getSentPendingIntent());
        }

        list.add(getSentPendingIntent(message));

        return list;
    }

    private ArrayList<PendingIntent> getDeliveredPendingIntentList(DialogueMessage message, int copies) {
        Contract.throwIfArgNull(message, "message");
        Contract.throwIfArg(copies <= 0, "Copies should be at least 1");

        ArrayList<PendingIntent> list = new ArrayList<PendingIntent>();

        /*
        In order to know which message has been delivered we pass it
        with the last pending intent so that the receiver can set the flag.
         */
        for (int i = 0; i < copies - 1; i++) {
            list.add(getDeliveryPendingIntent());
        }

        list.add(getDeliveryPendingIntent(message));

        return list;
    }
}
