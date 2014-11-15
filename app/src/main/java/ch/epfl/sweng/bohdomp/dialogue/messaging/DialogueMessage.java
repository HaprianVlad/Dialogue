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

    private final Contact mContact;
    private final MessageBody mMessageBody;
    private final long mTimestamp;
    private final DialogueMessageId mMessageId;
    private final MessageStatus mMessageStatus;

    private boolean mIsReadStatus;
    private boolean mIsDataMessage;

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

        this.mContact = contactParameter;
        this.mMessageBody = newMessageBody(messageBodyParameter);
        this.mTimestamp = System.currentTimeMillis();
        this.mMessageId = IdManager.getInstance().newDialogueMessageId();
        this.mIsReadStatus = false;
        this.mMessageStatus = messageStatusParameter;
        this.mIsDataMessage = isDataMessageParameter;
    }


    /**
     * Getter for the mContact message
     * @return the mContact whose message is
     */
    public Contact getContact() {
        //FIXME:Contact should be immutable
        return mContact;
    }

    /**
     * Getter for the body message
     * @return the body message
     */
    public MessageBody getBody() {
        return mMessageBody;
    }

    /**
     * Getter for is read status of message
     * @return true if message has been read , false otherwise
     */
    public boolean getIsReadStatus() {
        return mIsReadStatus;
    }

    /**
     * Getter for the message time stamp
     * @return the time stamp of the message
     */
    public Timestamp getMessageTimeStamp() {
        return new Timestamp(mTimestamp);
    }

    /**
     * Getter for the message status
     * @return the message status. incoming or outgoing
     */
    public MessageStatus getMessageStatus() {
        return mMessageStatus;
    }

    /**
     * Getter for the message id
     * @return the message id
     */
    public DialogueMessageId getMessageId() {
        return mMessageId;
    }

    /**
     * Sets a message as read
     */
    public void setMessageAsRead() {
        //FIXME: Decide if we need a listener somewhere or when enter in converstation all messages are read
        mIsReadStatus = true;
    }

    /**
     * Method that returns the allowed channels where we can send the message
     * @return the list of allowed channels
     */
    public  boolean getIsDataMessage() {
        return mIsDataMessage;
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
        dest.writeParcelable(this.mContact, 0);
        dest.writeParcelable(this.mMessageBody, 0);
        dest.writeLong(this.mTimestamp);
        dest.writeParcelable(this.mMessageId, 0);
        dest.writeInt(this.mMessageStatus == null ? -1 : this.mMessageStatus.ordinal());
        dest.writeByte(mIsReadStatus ? (byte) 1 : (byte) 0);
        dest.writeByte(mIsDataMessage ? (byte) 1 : (byte) 0);
    }

    DialogueMessage(Parcel in) {
        this.mContact = in.readParcelable(Contact.class.getClassLoader());
        this.mMessageBody = in.readParcelable(MessageBody.class.getClassLoader());
        this.mTimestamp = in.readLong();
        this.mMessageId = in.readParcelable(DialogueMessageId.class.getClassLoader());
        int tmpMessageStatus = in.readInt();
        this.mMessageStatus = tmpMessageStatus == -1 ? null : MessageStatus.values()[tmpMessageStatus];
        this.mIsReadStatus = in.readByte() != 0;
        this.mIsDataMessage = in.readByte() != 0;
    }
}
