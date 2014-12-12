package ch.epfl.sweng.bohdomp.dialogue.channels;


import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import android.widget.Toast;

import ch.epfl.sweng.bohdomp.dialogue.crypto.Crypto;
import ch.epfl.sweng.bohdomp.dialogue.crypto.CryptoException;
import ch.epfl.sweng.bohdomp.dialogue.data.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.data.StorageManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DecryptedDialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.TextMessageBody;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Handles the incoming messages.
 */
public final class DialogueIncomingDispatcher extends IntentService {
    public static final String ACTION_RECEIVE_MESSAGE = "ACTION_RECEIVE_MESSAGE";

    private static boolean sIsRunning;

    public DialogueIncomingDispatcher() {
        super("DialogueIncomingDispatcher");
    }

    /**
     * Handles the incoming messages.
     *
     * @param context of the application.
     * @param message to be received.
     */
    public static void receiveMessage(final Context context, final DialogueMessage message) {
        Contract.throwIfArgNull(context, "context");
        Contract.throwIfArgNull(message, "message");
        Contract.throwIfArg(message.getDirection() == DialogueMessage.MessageDirection.OUTGOING,
                "An outgoing message should not arrive to the incoming dispatcher");

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

        if (intent.getAction().equals(ACTION_RECEIVE_MESSAGE)) {

            DialogueMessage message = DialogueMessage.extractMessage(intent);

            if (Crypto.isEncrypted(message.getBody().getMessageBody())) {

                try {
                    TextMessageBody decryptedBody = new TextMessageBody(Crypto.decrypt(getApplicationContext(),
                            message.getBody().getMessageBody()));

                    DialogueMessage decryptedMessage = new DecryptedDialogueTextMessage(message.getContact(),
                            message.getChannel(), message.getPhoneNumber(), decryptedBody.getMessageBody(),
                            message.getDirection());

                    DefaultDialogData.getInstance().addMessageToConversation(decryptedMessage);
                } catch (CryptoException e) {
                    
                    Toast.makeText(getApplicationContext(),
                            "Could not decrypt message from" + message.getContact().getDisplayName(),
                            Toast.LENGTH_SHORT).show();

                    /* Add encrypted message so that we don't lose it. */
                    DefaultDialogData.getInstance().addMessageToConversation(message);
                }
            } else {
                DefaultDialogData.getInstance().addMessageToConversation(message);
            }
            Notificator notificator = new Notificator(getApplicationContext());
            notificator.update(message);
            StorageManager storageManager = new StorageManager(getApplicationContext());
            storageManager.saveData();
        }
        //ignore when receiving other commands

        sIsRunning = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sIsRunning = false;
    }
}