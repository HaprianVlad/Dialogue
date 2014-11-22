package ch.epfl.sweng.bohdomp.dialogue.channels;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Handles the incoming messages.
 */
public final class DialogueIncomingDispatcher extends IntentService{
    public static final String ACTION_RECEIVE_MESSAGE = "ACTION_RECEIVE_MESSAGE";

    private static boolean sIsRunning;


    private Notificator mNotificator;

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

        //An outgoing message should not arrive here
        if (message.getStatus() == DialogueMessage.MessageStatus.OUTGOING) {
            throw new IllegalArgumentException();
        }

        /* Create intent and send to myself */
        Intent intent = new Intent(context, DialogueIncomingDispatcher.class);
        intent.setAction(ACTION_RECEIVE_MESSAGE);
        intent.putExtra(DialogueMessage.MESSAGE, message);

        context.startService(intent);
    }

    public static boolean isRunning() {
        return sIsRunning;
    }

    @Override
    public void onHandleIntent(Intent intent) {

        Contract.throwIfArgNull(intent, "intent");

        if (intent.getAction() == ACTION_RECEIVE_MESSAGE) {

            DialogueMessage message = DialogueMessage.extractMessage(intent);

            mNotificator= new Notificator(getApplicationContext());
            mNotificator.update(message);

            DefaultDialogData.getInstance().addMessageToConversation(message);

            sIsRunning=true;
        }
        //ignore when receiving other commands
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sIsRunning=false;

    }

}