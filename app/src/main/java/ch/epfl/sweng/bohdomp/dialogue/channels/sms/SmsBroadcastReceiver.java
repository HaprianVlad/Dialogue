package ch.epfl.sweng.bohdomp.dialogue.channels.sms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;


import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueSmsMessage;


/**
 * Defines an Sms Broadcast Receiver
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    private SmsMessage[] smsMessages;

    @Override
    public void onReceive(Context context, Intent intent) {

        smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i=0; i<smsMessages.length; i++) {

            //Testing
            Toast.makeText(context, "SMS RECEIVED from"+ smsMessages[i].getDisplayOriginatingAddress(),
                    Toast.LENGTH_SHORT).show();
            //Starting the SmsReceiverService for each received message
            DialogueSmsMessage dialogueSmsMessage = new DialogueSmsMessage(smsMessages[i]);
            Intent receiveMessageIntent = new Intent(context, SmsReceiverService.class);
            receiveMessageIntent.putExtra("message", dialogueSmsMessage);
            intent.setAction(SmsReceiverService.RECEIVE_SMS);

            context.startService(intent);

        }
    }
}