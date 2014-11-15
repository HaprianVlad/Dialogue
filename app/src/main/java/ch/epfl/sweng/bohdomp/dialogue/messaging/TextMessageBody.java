package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsMessage;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;


/**
 *  Class representing the mBody of a text message
 */
public final class TextMessageBody implements MessageBody{
    public final static int MAX_MESSAGE_SIZE = SmsMessage.MAX_USER_DATA_BYTES;
    private final String mBody;

    public TextMessageBody(String body) {
        if (body == null) {
            throw new NullArgumentException("body");
        }
        if (body.getBytes().length > MAX_MESSAGE_SIZE) {
            throw new IllegalArgumentException("Too big message body!");
        }

        this.mBody = body;
    }

    @Override
    public String getMessageBody() {
        return mBody;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBody);
    }

    public static final Parcelable.Creator<TextMessageBody> CREATOR = new Parcelable.Creator<TextMessageBody>() {
        public TextMessageBody createFromParcel(Parcel source) {
            return new TextMessageBody(source);
        }

        public TextMessageBody[] newArray(int size) {
            return new TextMessageBody[size];
        }
    };

    private TextMessageBody(Parcel in) {
        this.mBody = in.readString();
    }
}
