package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.net.Uri;
import android.os.Parcel;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Class representing the mBody of a data message
 */
public final class DataMessageBody implements MessageBody{

    //FIXME: Find exactly how uri works

    private final Uri mBody;

    public DataMessageBody(String body) {
        if (body == null) {
            throw new NullArgumentException("body");
        }

        this.mBody = Uri.parse(body);
    }

    @Override
    public String getMessageBody() {
        return mBody.getFragment();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mBody, 0);
    }

    private DataMessageBody(Parcel in) {
        this.mBody = in.readParcelable(Uri.class.getClassLoader());
    }

    public static final Creator<DataMessageBody> CREATOR = new Creator<DataMessageBody>() {
        public DataMessageBody createFromParcel(Parcel source) {
            return new DataMessageBody(source);
        }

        public DataMessageBody[] newArray(int size) {
            return new DataMessageBody[size];
        }
    };
}
