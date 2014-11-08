package ch.epfl.sweng.bohdomp.dialogue.channels;


import android.app.IntentService;
import android.content.Intent;

/**
 * Handles the incoming messages.
 */
public final class DialogueIncomingDispatcher extends IntentService{

    @Override
    protected void onHandleIntent(Intent intent) {

    }
    public DialogueIncomingDispatcher() {
        super("DialogueIncomingDispatcher");
    }
}