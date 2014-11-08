package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
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
    ConversationId getId();

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
    public static final class ConversationId implements Parcelable, Comparable<ConversationId> {
        private final long mId;
        private static long mPreviousId = System.currentTimeMillis();

        private ConversationId(long id) {
            this.mId = id;
        }

        public long getLong() {
            return mId;
        }

        public static ConversationId fromLong(long id) {
            if (id < 0) {
                throw new IllegalArgumentException("Given long to construct ConversationId is smaller than 0!");
            }
            return new ConversationId(id);
        }

        public static synchronized ConversationId getNewConversationId() {
            if (BuildConfig.DEBUG && mPreviousId > 0) {
                throw new AssertionError();
            }
            mPreviousId += 1;
            return  new ConversationId(mPreviousId);
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
            return Long.compare(this.mId, another.mId);
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
            return mId == that.mId;

        }

        @Override
        public int hashCode() {
            return Long.valueOf(mId).hashCode();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            out.writeLong(mId);
        }

        public static final Parcelable.Creator<ConversationId> CREATOR
            = new Parcelable.Creator<ConversationId>() {
                public ConversationId createFromParcel(Parcel in) {
                    return new ConversationId(in.readLong());
                }

                public ConversationId[] newArray(int size) {
                    return new ConversationId[size];
                }
            };

    }
}
