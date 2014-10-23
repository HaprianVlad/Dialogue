package ch.epfl.sweng.bohdomp.dialogue.channels.sms;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;

import ch.epfl.sweng.bohdomp.dialogue.messaging.MessageDispatcherService;

/**
 * Defines an Sms Broadcast Receiver
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    private SmsMessage[] smsMessages;

    //@TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (int i=1; i<smsMessages.length; i++) {

            MessageDispatcherService.startReceiveSms(context, smsMessages[i]);


        }
    }
}