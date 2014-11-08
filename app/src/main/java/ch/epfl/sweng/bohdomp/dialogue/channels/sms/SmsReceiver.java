package ch.epfl.sweng.bohdomp.dialogue.channels.sms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;


import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.channels.DialogueIncomingDispatcher;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueSmsMessage;


/**
 * Defines an Sms Broadcast Receiver
 */
public class SmsReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            throw new NullArgumentException("context");
        }

        if (intent == null) {
            throw new NullArgumentException("intent");
        }


        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage smsMessage : smsMessages) {

            if (BuildConfig.DEBUG && (smsMessage == null)) {
                throw new AssertionError("smsMessage == null");
            }

            //FIXME: remove me when we actually display it to the user.
            Toast.makeText(context, "SMS RECEIVED from" + smsMessage.getDisplayOriginatingAddress(),
                    Toast.LENGTH_SHORT).show();

            //Starting the MessageDispatcherService for each received message
            DialogueSmsMessage dialogueSmsMessage = new DialogueSmsMessage(smsMessage);
            Intent receiveMessageIntent = new Intent(context, DialogueIncomingDispatcher.class);

            receiveMessageIntent.putExtra("message", dialogueSmsMessage);
            //intent.setAction(DialogueIncomingDispatcher.RECEIVE_SMS);

            context.startService(intent);
        }
    }
}