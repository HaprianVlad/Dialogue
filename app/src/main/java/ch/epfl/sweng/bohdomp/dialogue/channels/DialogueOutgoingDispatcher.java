package ch.epfl.sweng.bohdomp.dialogue.channels;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.channels.sms.SmsSenderService;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueSmsMessage;

/**
 * Dispatches the outgoing message to the channels.
 */
public final class DialogueOutgoingDispatcher extends IntentService {
    public static final String MESSAGE = "MESSAGE";
    private static final String ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE";

    public DialogueOutgoingDispatcher(String name) {
        super(name);
    }

    public static void sendMessage(Context context, DialogueMessage message) {
        if (context == null) {
            throw new NullArgumentException("context");
        } else if (message == null) {
            throw new NullArgumentException("message");
        }

        /* Create intent and send to myself */
        Intent intent = new Intent(ACTION_SEND_MESSAGE);
        // put message in intent

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            throw new NullArgumentException("intent");
        }

        DialogueMessage message = extractMessage(intent);

        /*
        1. Find out where to send to (contact numbers + connection status)
        2. Create intent
        3. Send intent to correct service
         */

        if (canSendSms(message)) {
            sendSms(message);
        } else if (canSendMms(message)) {
            sendMms(message);
        } else {
            /* BLALALALALAALALAL */
        }
    }

    private void sendSms(DialogueMessage message) {
        if (BuildConfig.DEBUG && (message == null)) {
            throw new AssertionError("message == null");
        }

        Intent intent = new Intent(SmsSenderService.ACTION_SEND_SMS);
        // intent.putExtra(SmsSenderService.MESSAGE_BODY, message.getBody());
    }

    private void sendMms(DialogueMessage message) {
        if (BuildConfig.DEBUG && (message == null)) {
            throw new AssertionError("message == null");
        }

        /* Create intent */
        /* Send intent to service */
    }

    private boolean canSendSms(DialogueMessage message) {
        if (BuildConfig.DEBUG && (message == null)) {
            throw new AssertionError("message == null");
        }

        return message.getAllowedChannels().contains(Contact.ChannelType.SMS);
    }

    private boolean canSendMms(DialogueMessage message) {
        if (BuildConfig.DEBUG && (message == null)) {
            throw new AssertionError("message == null");
        }

        return message.getAllowedChannels().contains(Contact.ChannelType.MMS);
    }

    private DialogueMessage extractMessage(Intent intent) {
        if (BuildConfig.DEBUG && (intent == null)) {
            throw new AssertionError("intent == null");
        }

        return (DialogueMessage) intent.getExtras().getParcelable(MESSAGE);
    }
}


