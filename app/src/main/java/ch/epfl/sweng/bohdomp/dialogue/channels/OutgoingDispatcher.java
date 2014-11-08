package ch.epfl.sweng.bohdomp.dialogue.channels;

import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 * Interface that handles the outgoing messages.
 */
public interface OutgoingDispatcher {

    /**
     * Sends the message to the best available channel.
     * @param message to be sent.
     */
    void sendMessage(DialogueMessage message);
}
