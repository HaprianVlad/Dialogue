package ch.epfl.sweng.bohdomp.dialogue.channels;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import ch.epfl.sweng.bohdomp.dialogue.channels.sms.SmsSenderService;
import ch.epfl.sweng.bohdomp.dialogue.data.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Dispatches the outgoing message to the channels.
 */
public final class DialogueOutgoingDispatcher extends IntentService {
    private static final String ACTION_SEND_MESSAGE = "ACTION_SEND_MESSAGE";

    public DialogueOutgoingDispatcher() {
        super("DialogueOutgoingDispatcher");
    }

    /**
     * Handles the sending of messages.
     *
     * @param context of the application.
     * @param message to be sent.
     */
    public static void sendMessage(Context context, DialogueMessage message) {
        if (context == null) {
            throw new NullArgumentException("context");
        }
        if (message == null) {
            throw new NullArgumentException("message");
        }
        if (message.getStatus() == DialogueMessage.MessageStatus.INCOMING) {
            throw new IllegalArgumentException();
        }

        /* Create intent and send to myself */
        Intent intent = new Intent(context, DialogueOutgoingDispatcher.class);
        intent.setAction(ACTION_SEND_MESSAGE);
        intent.putExtra(DialogueMessage.MESSAGE, message);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Contract.throwIfArgNull(intent, "intent");

        DialogueMessage message = DialogueMessage.extractMessage(intent);

        DefaultDialogData.getInstance().addMessageToConversation(message);

        if (canSendSms(message)) {
            sendSms(message);
        } else if (canSendMms(message)) {
            sendMms(message);
        }
    }

    private void sendSms(DialogueMessage message) {
        Contract.assertNotNull(message, "message");

        Log.i("DialogueOutgoingDispatcher", "3");

        /* Create intent and send to SmsSenderService */
        Intent intent = new Intent(getApplicationContext(), SmsSenderService.class);
        intent.setAction(SmsSenderService.ACTION_SEND_SMS);
        intent.putExtra(DialogueMessage.MESSAGE, message);
        getApplicationContext().startService(intent);

    }

    private void sendMms(DialogueMessage message) {
        Contract.assertNotNull(message, "message");

        /* Create intent */
        /* Send intent to service */
    }

    private boolean canSendSms(DialogueMessage message) {
        Contract.assertNotNull(message, "message");
        return message.getContact().availableChannels().contains(Contact.ChannelType.SMS);
    }

    private boolean canSendMms(DialogueMessage message) {
        Contract.assertNotNull(message, "message");
        return message.getContact().availableChannels().contains(Contact.ChannelType.MMS);
    }
}


