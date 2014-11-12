package ch.epfl.sweng.bohdomp.dialogue.messaging;


import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.DialogueMessageId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;

/**
 * Abstract class representing an message. This class is mutable.
 */

public abstract class DialogueMessage implements Parcelable {



    /**
     * Enumeration representing the state of a message
     */
    public static enum MessageStatus {
        INCOMING, OUTGOING
    }

    public static final String MESSAGE = "MESSAGE";

    private final Contact contact;
    private final MessageBody messageBody;
    private final long timestamp;
    private final DialogueMessageId messageId;
    private final MessageStatus messageStatus;

    private boolean isReadStatus;
    private boolean isDataMessage;


    /**
     * Extracts a DialogueMessage from an intent.
     * @param intent containing the message.
     * @return the message contained in the intent.
     */
    public static DialogueMessage extractMessage(Intent intent) {
        if (intent == null) {
            throw new NullArgumentException("intent");
        }

        return (DialogueMessage) intent.getExtras().getParcelable(MESSAGE);
    }

    DialogueMessage(Contact contactParameter, String messageBodyParameter,
                           MessageStatus messageStatusParameter, boolean isDataMessageParameter) {
        if (contactParameter == null) {
            throw new NullArgumentException("contactParameter");
        }
        if (messageBodyParameter == null) {
            throw new NullArgumentException("messageBodyParameter");
        }
        if (messageStatusParameter == null) {
            throw new NullArgumentException("messageStatusParameter");
        }

        this.contact = contactParameter;
        this.messageBody = newMessageBody(messageBodyParameter);
        this.timestamp = System.currentTimeMillis();
        this.messageId = IdManager.getInstance().newDialogueMessageId();
        this.isReadStatus = false;
        this.messageStatus = messageStatusParameter;
        this.isDataMessage = isDataMessageParameter;
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
        return new Timestamp(timestamp);
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
        isReadStatus = true;
    }



    /**
     * Method that returns the allowed channels where we can send the message
     * @return the list of allowed channels
     */
    public  boolean getIsDataMessage() {
        return isDataMessage;
    }

    /**
     * Factory method for constructing the corespondent type of message body
     * @param body the string we will put in the body
     * @return the created MessageBody
     */
    public abstract MessageBody newMessageBody(String body);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.contact, 0);
        dest.writeParcelable(this.messageBody, 0);
        dest.writeLong(this.timestamp);
        dest.writeParcelable(this.messageId, 0);
        dest.writeInt(this.messageStatus == null ? -1 : this.messageStatus.ordinal());
        dest.writeByte(isReadStatus ? (byte) 1 : (byte) 0);
        dest.writeByte(isDataMessage ? (byte) 1 : (byte) 0);
    }

    DialogueMessage(Parcel in) {
        this.contact = in.readParcelable(Contact.class.getClassLoader());
        this.messageBody = in.readParcelable(MessageBody.class.getClassLoader());
        this.timestamp = in.readLong();
        this.messageId = in.readParcelable(DialogueMessageId.class.getClassLoader());
        int tmpMessageStatus = in.readInt();
        this.messageStatus = tmpMessageStatus == -1 ? null : MessageStatus.values()[tmpMessageStatus];
        this.isReadStatus = in.readByte() != 0;
        this.isDataMessage = in.readByte() != 0;
    }

}
