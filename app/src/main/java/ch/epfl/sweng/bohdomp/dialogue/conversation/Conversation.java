package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.sql.Timestamp;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 *
 */
public interface Conversation {

    /**
     * Getter for conversation id
     * @return conversation id
     */
    ConversationId getId();

    /**
     * Returns the name of the conversation
     * @return the name of the conversation
     */
    String getName();

    /**
     * Getter for the set of contacts
     * @return the set of contacts of the conversation
     */

    List<Contact> getContacts();


    /**
     * Getter for the messages list of the conversation
     * @return list of messages
     */
    List<DialogueMessage> getMessages();

    /**
     * Getter for the timeStamp of the conversation
     * @return the current timestamp
     */
    Timestamp getLastActivityTime();

    /**
     * Getter for the number of messages in the conversation
     * @return number of messages in the conversation
     */
    int getMessageCount();

    /**
     * Getter for the unread boolean
     * @return conversationHasUnread parameter
     */
    boolean hasUnread();

    /**
     * Adds a contact to the conversation
     * @param contact the contact we want to add
     */
    void addContact(Contact contact);

    /**
     * Removes a contact from the conversation
     * @param contact the contact we want to remove
     */
    void removeContact(Contact contact);


    /**
     * Adds a message to the conversation
     * @param message the message we want to add
     */
    void addMessage(DialogueMessage message);

    /**
     * Adds a listener to the converstation
     * @param listener
     */
    void addListener(ConversationListener listener);

    /**
     * Removes a listener from the conversation
     * @param listener the listener to be removed
     */
    void removeListener(ConversationListener listener);

    /**
     * Sets all messages as read
     */
    void setAllMessagesAsRead();
}
