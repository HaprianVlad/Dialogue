package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;

import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * interface representing a Contact
 */
public interface Contact extends Parcelable {
    String getDisplayName();
    Set<PhoneNumber> getPhoneNumbers();
    Set<PhoneNumber> getPhoneNumbers(ChannelType channel);
    Set<ChannelType> availableChannels();
    Contact updateInfo(final Context context) throws InvalidNumberException;
    Creator<Contact> getParcelCreator();

    /**
     * enumeration for channel types
     */
    public static enum ChannelType implements Parcelable {
        SMS, MMS;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Contract.throwIfArgNull(dest, "destination parcel");
            dest.writeInt(ordinal());
        }

        public static final Creator<ChannelType> CREATOR = new Creator<ChannelType>() {
            @Override
            public ChannelType createFromParcel(Parcel source) {
                Contract.throwIfArgNull(source, "source parcel");
                return ChannelType.values()[source.readInt()];
            }

            @Override
            public ChannelType[] newArray(int size) {
                return new ChannelType[size];
            }
        };
    }

    /**
     * PhoneNumber class, wraps a phone number string with an associated tag
     */
    class PhoneNumber implements Parcelable {

        private final String mPhoneNumber;
        private final Tag mTag;

        public PhoneNumber(final String phoneNumber, final Tag tag) {
            Contract.throwIfArgNull(phoneNumber, "phone number");
            Contract.throwIfArgNull(tag, "tag");
            this.mPhoneNumber = phoneNumber;
            this.mTag = tag;
        }

        public String number() {
            return mPhoneNumber;
        }

        public Tag tag() {
            return mTag;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            PhoneNumber other = (PhoneNumber) o;

            return PhoneNumberUtils.compare(this.mPhoneNumber, other.mPhoneNumber) && this.mTag == other.mTag;
        }

        @Override
        public int hashCode() {
            return PhoneNumberUtils.toCallerIDMinMatch(this.mPhoneNumber).hashCode() ^ mTag.hashCode();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Contract.throwIfArgNull(dest, "dest parcel");
            dest.writeString(this.mPhoneNumber);
            dest.writeInt(this.mTag.ordinal());
        }

        private PhoneNumber(Parcel in) {
            this.mPhoneNumber = in.readString();
            this.mTag = Tag.values()[in.readInt()];
        }

        /**
         * subset of the officially supported phone number tags in android:
         * https://developer.android.com/reference/android/provider/ContactsContract.CommonDataKinds.Phone.html
         */
        public static enum Tag {
            MOBILE, HOME, WORK, WORK_MOBILE, OTHER;
        }

        public static final Creator<PhoneNumber> CREATOR = new Creator<PhoneNumber>() {
            @Override
            public PhoneNumber createFromParcel(Parcel source) {
                Contract.throwIfArgNull(source, "source parcel");
                return new PhoneNumber(source);
            }

            @Override
            public PhoneNumber[] newArray(int size) {
                return new PhoneNumber[size];
            }
        };
    }
}
