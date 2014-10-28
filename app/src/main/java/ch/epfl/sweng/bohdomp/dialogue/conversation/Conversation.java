package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 *
 */
public interface Conversation {

    /**
     * Getter for converstation id
     * @return converstation id
     */
    ConversationId getConversationId();

    /**
     * Getter for the set of contacts
     * @return the set of contacts of the conversation
     */
    Set<Contact> getConversationContacts();



    /**
     * Getter for the messages list of the conversation
     * @return list of messages
     */
    List<DialogueMessage> getConversationMessages();

    /**
     * Getter for the timeStamp of the conversation
     * @return the current timestamp
     */
    Timestamp getConversationTimeStamp();

    /**
     * Getter for the number of messages in the conversation
     * @return number of messages in the conversation
     */
    int getConversationMsgCount();

    /**
     * Getter for the unread boolean
     * @return conversationHasUnread paramater
     */
    boolean getConversationHasUnread();

    /**
     * Adds a contact to the conversation
     * @param contact the contact we want to add
     */
    void addConversationContact(Contact contact);

    /**
     * Removes a contact from the conversation
     * @param contact the contact we want to remove
     */
    void removeConversationContact(Contact contact);


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


    /**
     * Represents a DialogueConversation id. This class is immutable
     */
    public static final class ConversationId{

        private static long lastId = 0;

        public static ConversationId getNewConversationId() {
            lastId += 1;
            return  new ConversationId(lastId);
        }

        private final long id;

        private ConversationId(long idParameter) {
            this.id = idParameter;
        }

        public long getId() {
            return id;
        }
    }
}
