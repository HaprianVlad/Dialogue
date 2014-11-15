package ch.epfl.sweng.bohdomp.dialogue.channels;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 * Handles the incoming messages.
 */
public final class DialogueIncomingDispatcher extends IntentService{
    public static final String ACTION_RECEIVE_MESSAGE = "ACTION_RECEIVE_MESSAGE";

    public DialogueIncomingDispatcher() {
        super("DialogueIncomingDispatcher");
    }

    /**
     * Handles the incoming messages.
     * @param context of the application.
     * @param message to be received.
     */
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
        intent.putExtra(DialogueMessage.MESSAGE, message);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DialogueMessage message = DialogueMessage.extractMessage(intent);

        addMessageToConversation(message);
    }

    private void addMessageToConversation(DialogueMessage message) {
        if (BuildConfig.DEBUG && message == null) {
            throw new AssertionError("message == null");
        }

        Conversation conversation = DefaultDialogData.getInstance().createOrGetConversation(message.getContact());
        conversation.addMessage(message);
    }
}