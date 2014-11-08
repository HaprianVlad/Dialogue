package ch.epfl.sweng.bohdomp.dialogue.channels;

import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 * Interface that handles the incoming messages.
 */
public interface IncomingDispatcher {

    /**
     * Puts the message in DialogueData.
     * @param message to be dispatched.
     */
    void receiveMessage(DialogueMessage message);
}
