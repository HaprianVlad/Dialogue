package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 *
 */
public interface Conversation {

    /**
     * Getter for conversation id
     * @return conversation id
     */
    long getId();

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
    public static final class ConversationId implements Comparable<ConversationId> {
        private final UUID mId;

        private ConversationId(UUID id) {
            this.mId = id;
        }

        public long getId() {
            //HACK: for now until we get to change the reset
            return mId.getLeastSignificantBits();
        }

        public static ConversationId getNewConversationId() {
            return  new ConversationId(UUID.randomUUID());
        }

        /**
         * Compares this object to the specified object to determine their relative
         * order.
         *
         * @param another the object to compare to this instance.
         * @return a negative integer if this instance is less than {@code another};
         * a positive integer if this instance is greater than
         * {@code another}; 0 if this instance has the same order as
         * {@code another}.
         * @throws ClassCastException if {@code another} cannot be converted into something
         *                            comparable to {@code this} instance.
         */
        @Override
        public int compareTo(ConversationId another) {
            return this.mId.compareTo(another.mId);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            ConversationId that = (ConversationId) o;
            return mId.equals(that.mId);

        }

        @Override
        public int hashCode() {
            return mId.hashCode();
        }
    }
}
