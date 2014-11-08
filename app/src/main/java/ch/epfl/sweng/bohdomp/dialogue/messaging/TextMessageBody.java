package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.SmsMessage;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;


/**
 *  Class representing the body of a text message
 */
public class TextMessageBody implements MessageBody{

    public final static int MAX_MESSAGE_SIZE = SmsMessage.MAX_USER_DATA_BYTES;
    private final String body;

    public TextMessageBody(String bodyParameter) {
        if (bodyParameter == null) {
            throw new NullArgumentException("bodyParameter");
        }
        if (bodyParameter.getBytes().length > MAX_MESSAGE_SIZE) {
            throw new IllegalArgumentException("Too big message body!");
        }

        this.body = bodyParameter;

    }

    @Override
    public String getMessageBody() {
        return body;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.body);
    }

    private TextMessageBody(Parcel in) {
        this.body = in.readString();
    }

    public static final Parcelable.Creator<TextMessageBody> CREATOR = new Parcelable.Creator<TextMessageBody>() {
        public TextMessageBody createFromParcel(Parcel source) {
            return new TextMessageBody(source);
        }

        public TextMessageBody[] newArray(int size) {
            return new TextMessageBody[size];
        }
    };
}
