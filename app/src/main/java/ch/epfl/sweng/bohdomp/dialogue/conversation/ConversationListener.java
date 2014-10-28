package ch.epfl.sweng.bohdomp.dialogue.conversation;

/**
 * Interface representing a listener for conversation
 */
public interface ConversationListener {

    /**
     * This method is called when the conversation is changed
     * @param conversation - conversation that changed
     */
    void onConversationChanged(Conversation conversation);
}
