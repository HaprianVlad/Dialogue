package ch.epfl.sweng.bohdomp.dialogue.channels.sms;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.widget.Toast;


import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.channels.DialogueIncomingDispatcher;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
/**
 * Defines an Sms Broadcast Receiver
 */
public final class SmsReceiver extends BroadcastReceiver {
    private  ContactFactory mContactFactory;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (context == null) {
            throw new NullArgumentException("context");
        }

        if (intent == null) {
            throw new NullArgumentException("intent");
        }

        if (mContactFactory == null) {
            mContactFactory = new ContactFactory(context);
        }

        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage smsMessage : smsMessages) {
            if (BuildConfig.DEBUG && (smsMessage == null)) {
                throw new AssertionError("smsMessage == null");
            }

            sendToIncomingDispatcher(context, smsMessage);
        }
    }

    private void sendToIncomingDispatcher(Context context, SmsMessage smsMessage) {
        DialogueMessage dialogueMessage = null;

        try {
            dialogueMessage = convertFromSmsMessage(smsMessage);
        } catch (InvalidNumberException e) {
            Toast.makeText(context, "Incoming message from strange address: "
                    + smsMessage.getDisplayOriginatingAddress(), Toast.LENGTH_LONG).show();
        }

        DialogueIncomingDispatcher.receiveMessage(context, dialogueMessage);
    }

    private DialogueTextMessage convertFromSmsMessage(SmsMessage smsMessage) throws InvalidNumberException {
        Contact contact = mContactFactory.contactFromNumber(smsMessage.getDisplayOriginatingAddress());
        String stringBody = smsMessage.getMessageBody();

        return new DialogueTextMessage(contact, stringBody, DialogueMessage.MessageStatus.INCOMING);
    }
}