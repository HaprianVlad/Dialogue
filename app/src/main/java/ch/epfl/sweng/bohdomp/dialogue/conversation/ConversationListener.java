package ch.epfl.sweng.bohdomp.dialogue.conversation;


import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;

/**
 * Interface representing a listener for conversation
 */
public interface ConversationListener  {

    /**
     * This method is called when the conversation is changed
     * @param id - id of the conversation that changed
     */
    void onConversationChanged(ConversationId id);
}
