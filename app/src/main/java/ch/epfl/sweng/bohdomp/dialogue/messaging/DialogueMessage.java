package ch.epfl.sweng.bohdomp.dialogue.messaging;


import android.content.Intent;
import android.os.Parcel;
import android.os.Parcelable;

import java.sql.Timestamp;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.DialogueMessageId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

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
    private final Contact.ChannelType mChannel;
    private final Contact.PhoneNumber mPhoneNumber;
    private final MessageBody mBody;
    private final long mTimestamp;
    private final DialogueMessageId mId;
    private final MessageStatus mStatus;

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

    DialogueMessage(Contact contactParameter, Contact.ChannelType channel, Contact.PhoneNumber phoneNumber,
                    String messageBodyParameter, MessageStatus messageStatusParameter, boolean isDataMessageParameter) {

        Contract.throwIfArgNull(contactParameter, "contact");
        Contract.throwIfArgNull(messageBodyParameter, "message body");
        Contract.throwIfArgNull(messageStatusParameter, "message status");

        this.mContact = contactParameter;
        this.mChannel = channel;
        this.mPhoneNumber = phoneNumber;
        this.mBody = newBody(messageBodyParameter);
        this.mTimestamp = System.currentTimeMillis();
        this.mId = IdManager.getInstance().newDialogueMessageId();
        this.mIsReadStatus = false;
        this.mStatus = messageStatusParameter;
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

    public Contact.ChannelType getChannel() {
        return mChannel;
    }

    public Contact.PhoneNumber getPhoneNumber(){
        return mPhoneNumber;
    }

    /**
     * Getter for the body message
     * @return the body message
     */
    public MessageBody getBody() {
        return mBody;
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
    public Timestamp getTimeStamp() {
        return new Timestamp(mTimestamp);
    }

    /**
     * Getter for the message status
     * @return the message status. incoming or outgoing
     */
    public MessageStatus getStatus() {
        return mStatus;
    }

    /**
     * Getter for the message id
     * @return the message id
     */
    public DialogueMessageId getId() {
        return mId;
    }

    /**
     * Sets a message as read
     */
    public void setAsRead() {
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
    public abstract MessageBody newBody(String body);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mContact, 0);
        dest.writeParcelable(this.mChannel, 0);
        dest.writeParcelable(this.mPhoneNumber, 0);
        dest.writeParcelable(this.mBody, 0);
        dest.writeLong(this.mTimestamp);
        dest.writeParcelable(this.mId, 0);
        dest.writeInt(this.mStatus == null ? -1 : this.mStatus.ordinal());
        dest.writeByte(mIsReadStatus ? (byte) 1 : (byte) 0);
        dest.writeByte(mIsDataMessage ? (byte) 1 : (byte) 0);
    }

    DialogueMessage(Parcel in) {
        this.mContact = in.readParcelable(Contact.class.getClassLoader());
        this.mChannel = in.readParcelable(Contact.ChannelType.class.getClassLoader());
        this.mPhoneNumber = in.readParcelable(Contact.PhoneNumber.class.getClassLoader());
        this.mBody = in.readParcelable(MessageBody.class.getClassLoader());
        this.mTimestamp = in.readLong();
        this.mId = in.readParcelable(DialogueMessageId.class.getClassLoader());
        int tmpMessageStatus = in.readInt();
        this.mStatus = tmpMessageStatus == -1 ? null : MessageStatus.values()[tmpMessageStatus];
        this.mIsReadStatus = in.readByte() != 0;
        this.mIsDataMessage = in.readByte() != 0;
    }
}
