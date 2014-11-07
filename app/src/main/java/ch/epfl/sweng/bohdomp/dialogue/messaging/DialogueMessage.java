package ch.epfl.sweng.bohdomp.dialogue.messaging;


import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Abstract class representing an message. This class is mutable.
 */
public abstract class DialogueMessage {


    /**
     * Represents a DialogueConversation id. This class is immutable
     */
    public static final class DialogueMessageId implements Parcelable, Comparable<DialogueMessageId> {
        private final long mId;

        private DialogueMessageId(long id) {
            this.mId = id;
        }

        public long getLong() {
            return mId;
        }

        public static DialogueMessageId fromLong(long id) {
            if (id < 0) {
                throw new IllegalArgumentException("Given long to construct ConversationId is smaller than 0!");
            }
            return new DialogueMessageId(id);
        }

        public static DialogueMessageId getNewDialogueMessageId() {
            // TODO generate IDs in a better way?
            return  new DialogueMessageId(System.currentTimeMillis());
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
        public int compareTo(DialogueMessageId another) {
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

            DialogueMessageId that = (DialogueMessageId) o;
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

        public static final Parcelable.Creator<DialogueMessageId> CREATOR =new Parcelable.Creator<DialogueMessageId>() {
            public DialogueMessageId createFromParcel(Parcel in) {
                return new DialogueMessageId(in.readLong());
            }

            public DialogueMessageId[] newArray(int size) {
                return new DialogueMessageId[size];
            }
        };

    }
    /**
     * Enumeration representing the state of a message
     */
    public static enum MessageStatus {
        INCOMING, OUTGOING
    }

    private final Contact contact;
    private final MessageBody messageBody;
    private final Timestamp timestamp;
    private final DialogueMessageId messageId;
    private final MessageStatus messageStatus;
    private final List<Contact.ChannelType> allowedChannels = new ArrayList<Contact.ChannelType>();

    private boolean isReadStatus;


    public DialogueMessage(Contact contactParameter, String messageBodyParameter,
                           MessageStatus messageStatusParameter, Contact.ChannelType channelTypeParameter) {
        if (contactParameter == null) {
            throw new NullArgumentException(contactParameter);
        }
        if (messageBodyParameter == null) {
            throw new NullArgumentException(messageBodyParameter);
        }
        if (channelTypeParameter == null) {
            throw new NullArgumentException(channelTypeParameter);
        }

        this.contact = contactParameter;
        this.messageBody = newMessageBody(messageBodyParameter);
        this.timestamp = new Timestamp((new Date()).getTime());
        this.messageId = DialogueMessageId.getNewDialogueMessageId();
        this.isReadStatus = false;
        this.messageStatus = messageStatusParameter;
        this.allowedChannels.add(channelTypeParameter);

    }


    /**
     * Getter for the contact message
     * @return the contact whose message is
     */
    public Contact getContact() {
        //FIXME:Contact should be immutable
        return contact;
    }

    /**
     * Getter for the body message
     * @return the body message
     */
    public MessageBody getBody() {
        return messageBody;
    }

    /**
     * Getter for is read status of message
     * @return true if message has been read , false otherwise
     */
    public boolean getIsReadStatus() {
        return isReadStatus;
    }

    /**
     * Getter for the message time stamp
     * @return the time stamp of the message
     */
    public Timestamp getMessageTimeStamp() {
        return timestamp;
    }

    /**
     * Getter for the message status
     * @return the message status. incoming or outgoing
     */
    public MessageStatus getMessageStatus() {
        return messageStatus;
    }

    /**
     * Getter for the message id
     * @return the message id
     */
    public DialogueMessageId getMessageId() {
        return messageId;
    }

    /**
     * Sets a message as read
     */
    public void setMessageAsRead() {
        //FIXME: Decide if we need a listener somewhere or when enter in converstation all messages are read
        isReadStatus = false;
    }



    /**
     * Method that returns the allowed channels where we can send the message
     * @return the list of allowed channels
     */
    public  List<Contact.ChannelType> getAllowedChannels() {
        return new ArrayList<Contact.ChannelType>(allowedChannels);
    }

    /**
     * Factory method for constructing the corespondent type of message body
     * @param body the string we will put in the body
     * @return the created MessageBody
     */
    public abstract MessageBody newMessageBody(String body);

}
