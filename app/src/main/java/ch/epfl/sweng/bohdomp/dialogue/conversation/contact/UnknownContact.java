package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;
import android.os.Parcel;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;

/**
 * class representing a contact for which no entry was found in the contact database
 */
class UnknownContact implements Contact {

    private final String mPhoneNumber;

    public UnknownContact(final String phoneNumber) {
        this.mPhoneNumber = phoneNumber;
    }

    @Override
    public String getDisplayName() {
        return "unknown: " + mPhoneNumber;
    }

    @Override
    public Set<String> getPhoneNumbers() {
        Set<String> result = new HashSet<String>();
        result.add(mPhoneNumber);
        return result;
    }

    @Override
    public Set<ChannelType> availableChannels() {
        Set<ChannelType> result = new HashSet<ChannelType>();
        result.add(ChannelType.SMS);
        return result;
    }

    @Override
    public Contact updateInfo(final Context context) throws InvalidNumberException {
        return new ContactFactory(context).contactFromNumber(mPhoneNumber);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPhoneNumber);
    }

    private UnknownContact(Parcel in) {
        this.mPhoneNumber = in.readString();
    }

    public static final Creator<UnknownContact> CREATOR = new Creator<UnknownContact>() {
        public UnknownContact createFromParcel(Parcel source) {
            return new UnknownContact(source);
        }

        public UnknownContact[] newArray(int size) {
            return new UnknownContact[size];
        }
    };
}
