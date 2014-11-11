package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
/**
 * Represents all conversation in the application
 * This interface should be directly connected to the GUI
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
    Conversation getConversation(ConversationId id);

    /**
     * Adds a dialogueConversation to the data
     * @param contact a new dialogueConversation
     * @return the new or found conversation
     */
    Conversation createOrGetConversation(Contact contact);


    /**
     * Delete the conversation with the given ID
     */
    void removeConversation(ConversationId id);

    /**
     * Adds a listener to Dialogue Data
     * @param listener the listener to add
     */
    void addListener(DialogueDataListener listener);


    /**
     * Removes a listener from Dialogue Data
     * @param listener the listener to be removed
     */
    void removeListener(DialogueDataListener listener);

}