package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.os.Bundle;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;


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
     * Adds the message to the correct conversation.
     * @param message to be added.
     */
    void addMessageToConversation(DialogueMessage message);

    /**
     * Adds a listener to Dialogue Data
     * @param listener the listener to add
     */
    void addListener(DialogueDataListener listener);

    /**
     * Adds an listener to a conversation in dialogue data
     * @param listener the listener to add
     * @param conversationId the conversation where to add the listener
     */
    void addListenerForConversation(ConversationListener listener, ConversationId conversationId);


    /**
     * Removes an listener from a conversation in dialogue data
     * @param listener the listener to remove
     * @param conversationId the conversation where to remove the listener
     */
    void removeListenerForConversation(ConversationListener listener, ConversationId conversationId);


    /**
     * Removes a listener from Dialogue Data
     * @param listener the listener to be removed
     */
    void removeListener(DialogueDataListener listener);

    /**
     * Method that recreates the dialogue data from a bundle saved state
     * @param savedData the save state of the application
     */
    void restoreFromBundle(Bundle savedData);

    /**
     * Method that creates a bundle containing the state of the application
     * @return bundle representing the state of the application
     */
    Bundle createBundle();

}