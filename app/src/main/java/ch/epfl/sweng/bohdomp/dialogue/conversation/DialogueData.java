package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
/**
 * Represents all conversation in the application
 * This class should be directly connected to the GUI
 * @author BohDomp!
 *
 */
public interface DialogueData {
    /**
     *
     * @return List of all conversations ordered by their most recent activity
     */
    List<Conversation> getConversations();

    /**
     * Getter for a conversation
     * @param id of the conversation we are looking for.
     * @return DialogueConversation associated to the given id.
     */
    Conversation getConversation(Conversation.ConversationId id);

    /**
     * Adds a dialogueConversation to the data
     * @param contact a new dialogueConversation
     * @return the new or found conversation
     */
    Conversation createOrGetConversation(Contact contact);

}