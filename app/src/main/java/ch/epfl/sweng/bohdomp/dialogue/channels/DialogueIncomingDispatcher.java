package ch.epfl.sweng.bohdomp.dialogue.channels;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 * Handles the incoming messages.
 */
public final class DialogueIncomingDispatcher extends IntentService{
    public static final String MESSAGE = "MESSAGE";
    public static final String ACTION_RECEIVE_MESSAGE = "ACTION_RECEIVE_MESSAGE";

    public static void receiveMessage(Context context, DialogueMessage message) {
        if (context == null) {
            throw new NullArgumentException("context");
        }

        if (message == null) {
            throw new NullArgumentException("message");
        }

        /* Create intent and send to myself */
        Intent intent = new Intent(context, DialogueIncomingDispatcher.class);
        intent.setAction(ACTION_RECEIVE_MESSAGE);
        intent.putExtra(MESSAGE, message);
        context.startService(intent);
    }


    //FIXME:Adds logic to dispatch in Dialogue Data
    @Override
    protected void onHandleIntent(Intent intent) {
        DialogueMessage message = extractMessage(intent);
        Contact contact = message.getContact();
        Conversation conversation = DefaultDialogData.getInstance().createOrGetConversation(contact);
        conversation.addMessage(message);
    }

    public DialogueIncomingDispatcher() {
        super("DialogueIncomingDispatcher");
    }

    private DialogueMessage extractMessage(Intent intent) {
        if (BuildConfig.DEBUG && (intent == null)) {
            throw new AssertionError("intent == null");
        }

        return (DialogueMessage) intent.getExtras().getParcelable(MESSAGE);
    }
}