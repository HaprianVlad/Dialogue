package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsMessage;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Class representing a Dialogue SMS message.
 */
public class DialogueSmsMessage implements Parcelable {

    final static long ID = 0;

    /**
     * Enumeration representing the origin of a message
     */
    public static enum MessageType {
        INCOMING, OUTGOING
    }

    private final long mId;

    private final String mBody;
    private final String mPhoneNumber;
    private MessageType mMessageType;

    public DialogueSmsMessage(String phoneNumber, String body) {
        if (phoneNumber == null) {
            throw new NullArgumentException("Phone number");
        } else if (body == null) {
            throw new NullArgumentException("Body");
        } else {
            this.mId = ID;
            this.mPhoneNumber = phoneNumber;
            this.mBody = body;
            this.mMessageType = MessageType.OUTGOING;
        }
    }

    public DialogueSmsMessage(SmsMessage smsMessage) {
        if (smsMessage == null) {
            throw new IllegalArgumentException("Null arguments in DialogueSmsMessage");
        } else {
            this.mId = ID;
            this.mPhoneNumber = smsMessage.getOriginatingAddress();
            this.mBody = smsMessage.getMessageBody();
            this.mMessageType = MessageType.INCOMING;
        }

    }

    public long getId() {
        return mId;
    }

    public String getBody() {
        return mBody;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public MessageType getMessageType() {
        return mMessageType;
    }

    public void setMessageType(MessageType type) {
        mMessageType = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    /* --- Parcel --- */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeLong(this.mId);
        parcel.writeString(this.mBody);
        parcel.writeString(this.mPhoneNumber);
        parcel.writeInt(this.mMessageType == null ? -1 : this.mMessageType.ordinal());
    }

    private DialogueSmsMessage(Parcel in) {
        this.mId = in.readLong();
        this.mBody = in.readString();
        this.mPhoneNumber = in.readString();
        int tmpMMessageType = in.readInt();
        this.mMessageType = tmpMMessageType == -1 ? null : MessageType.values()[tmpMMessageType];
    }

    public static final Creator<DialogueSmsMessage> CREATOR = new Creator<DialogueSmsMessage>() {
        public DialogueSmsMessage createFromParcel(Parcel source) {
            return new DialogueSmsMessage(source);
        }

        public DialogueSmsMessage[] newArray(int size) {
            return new DialogueSmsMessage[size];
        }
    };
}
