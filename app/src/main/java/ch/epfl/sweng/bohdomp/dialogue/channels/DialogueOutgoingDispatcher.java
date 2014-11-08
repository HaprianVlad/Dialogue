package ch.epfl.sweng.bohdomp.dialogue.channels;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.channels.sms.SmsSenderService;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 * Dispatches the outgoing message to the channels.
 */
public final class DialogueOutgoingDispatcher extends IntentService {
    public static final String MESSAGE = "MESSAGE";
    private static final String ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE";



//FIXME:Adds logic to dispatch in Dialogue Data

    public DialogueOutgoingDispatcher() {
        super("DialogueOutgoingDispatcher");
    }

    public static void sendMessage(Context context, DialogueMessage message) {
        if (context == null) {
            throw new NullArgumentException("context");
        } else if (message == null) {
            throw new NullArgumentException("message");
        }

        /* Create intent and send to myself */
        Intent intent = new Intent(context, DialogueOutgoingDispatcher.class);
        intent.setAction(ACTION_SEND_MESSAGE);
        intent.putExtra(MESSAGE, message);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            throw new NullArgumentException("intent");
        }

        DialogueMessage message = extractMessage(intent);

        if (canSendSms(message)) {
            sendSms(message);
        } else if (canSendMms(message)) {
            sendMms(message);
        }
    }

    private void sendSms(DialogueMessage message) {
        if (BuildConfig.DEBUG && (message == null)) {
            throw new AssertionError("message == null");
        }

        Intent intent = new Intent(getApplicationContext(), SmsSenderService.class);
        intent.setAction(SmsSenderService.ACTION_SEND_SMS);
        intent.putExtra(SmsSenderService.MESSAGE, message);
        getApplicationContext().startService(intent);

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

        return message.getContact().availableChannels().contains(Contact.ChannelType.SMS);
    }

    private boolean canSendMms(DialogueMessage message) {
        if (BuildConfig.DEBUG && (message == null)) {
            throw new AssertionError("message == null");
        }

        return message.getContact().availableChannels().contains(Contact.ChannelType.MMS);
    }

    private DialogueMessage extractMessage(Intent intent) {
        if (BuildConfig.DEBUG && (intent == null)) {
            throw new AssertionError("intent == null");
        }

        return (DialogueMessage) intent.getExtras().getParcelable(MESSAGE);
    }

}


