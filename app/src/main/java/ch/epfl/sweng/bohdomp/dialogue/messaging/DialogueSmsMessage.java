package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsMessage;

/**
 * Class representing a Dialogue SMS message.
 */
public class DialogueSmsMessage implements Parcelable {

    /**
     * Enumeration representing the state of a message
     */
    public static enum MessageType {
        INCOMING, OUTGOING
    }

    private final String mBody;
    private final String mPhoneNumber;
    private MessageType mMessageType;

    public DialogueSmsMessage(String phoneNumber, String body) {
        if (phoneNumber == null || body == null) {
            throw new IllegalArgumentException("Null arguments in DialogueSmsMessage");
        } else {
            this.mPhoneNumber = phoneNumber;
            this.mBody = body;
            this.mMessageType = MessageType.OUTGOING;
        }

    }

    public DialogueSmsMessage(SmsMessage smsMessage) {
        if (smsMessage == null) {
            throw new IllegalArgumentException("Null arguments in DialogueSmsMessage");
        } else {
            this.mPhoneNumber = smsMessage.getOriginatingAddress();
            this.mBody = smsMessage.getMessageBody();
            this.mMessageType = MessageType.INCOMING;
        }

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

    @Override
    public int describeContents() {
        return 0;
    }

    /* --- Parcel --- */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(this.mBody);
        parcel.writeString(this.mPhoneNumber);
        parcel.writeInt(this.mMessageType == null ? -1 : this.mMessageType.ordinal());
    }

    private DialogueSmsMessage(Parcel in) {
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
