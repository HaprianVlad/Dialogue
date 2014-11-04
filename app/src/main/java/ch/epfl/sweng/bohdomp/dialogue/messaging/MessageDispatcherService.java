package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;

/**
 * FIXME : THIS CLASS HAS TO BE COMPLETLY CHANGED
 *
 */
public class MessageDispatcherService extends IntentService {
    private static final String TAG = "MessageDispatcherService";

    // IntentService can perform, e.g. RECEIVE_SMS
    public static final String RECEIVE_SMS = "ReceiveSMS";
    public static final String SEND_SMS = "SendSMS";



    private static final String MESSAGE_BODY = "messageBody";
    private static final String DESTINATION_ADDRESS ="destinationAddress";
    private static final String SENDER_ADDRESS = "senderAddress";


    /**
     * Starts this service to perform action RECEIVE_SMS with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startReceiveSms(Context context, SmsMessage smsMessage) {
        Log.e(TAG, "Start Intent To Message DispatcherService");

        Intent intent = new Intent(context, MessageDispatcherService.class);
        intent.setAction(RECEIVE_SMS);
        intent.putExtra(MESSAGE_BODY, smsMessage.getMessageBody());
        intent.putExtra(SENDER_ADDRESS, smsMessage.getOriginatingAddress());
        context.startService(intent);
    }

    /**
     * Starts this service to perform action SEND_SMS with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startSendSms(Context context, DialogueSmsMessage dialogueSmsMessage) {
        Intent intent = new Intent(context, MessageDispatcherService.class);
        intent.setAction(SEND_SMS);
        intent.putExtra(DESTINATION_ADDRESS, dialogueSmsMessage.getPhoneNumber());
        intent.putExtra(MESSAGE_BODY, dialogueSmsMessage.getBody());

        context.startService(intent);
    }

    private final SmsManager smsManager = SmsManager.getDefault();

    public MessageDispatcherService() {
        super("MessageDispatcherService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            final String messageBody = intent.getStringExtra(MESSAGE_BODY);

            if (SEND_SMS.equals(action)) {
                final String destinationAddress  = intent.getStringExtra(DESTINATION_ADDRESS);
                handleSendSms(destinationAddress, messageBody);
            } else if (RECEIVE_SMS.equals(action)) {
                final String senderAddress = intent.getStringExtra(SENDER_ADDRESS);
                handleReceiveSms(senderAddress, messageBody);
            }
        }
    }

    /**
     * Handle action RECEIVE_SMS in the provided background thread with the provided
     * parameters.
     */
    private void handleReceiveSms(String senderAddress, String smsMessageBody) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    /**
     * Handle action SEND_SMS in the provided background thread with the provided
     * parameters.
     */
    private void handleSendSms(String destinationAddress, String messageBody) {
        Log.e(TAG, destinationAddress + " : " + messageBody);

        //FIXME: use the last 2 parameters which are null to make reactive UI when message is well sent or not
        smsManager.sendTextMessage(destinationAddress, null, messageBody, null, null);
    }
}