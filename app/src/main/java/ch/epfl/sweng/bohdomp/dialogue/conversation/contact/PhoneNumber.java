package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.os.Parcel;
import android.os.Parcelable;
import android.telephony.PhoneNumberUtils;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * PhoneNumber class, wraps a phone number string with an associated tag
 */
public class PhoneNumber implements Parcelable {

    private final String mPhoneNumber;
    private final Tag mTag;

    public PhoneNumber(final String phoneNumber, final Tag tag) {
        Contract.throwIfArgNull(phoneNumber, "phoneNumber");
        Contract.throwIfArgNull(tag, "tag");

        this.mPhoneNumber = phoneNumber;
        this.mTag = tag;
    }

    public String getNumber() {
        return mPhoneNumber;
    }

    public Tag getTag() {
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
        Contract.throwIfArgNull(dest, "dest");

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

        @Override
        public String toString() {
            switch (this) {
                case MOBILE:
                    return "mobile";
                case HOME:
                    return "home";
                case WORK_MOBILE:
                    return "mobile work";
                case OTHER:
                    return "other";
                default :
                    return this.name();
            }
        }


    }

    public static final Creator<PhoneNumber> CREATOR = new Creator<PhoneNumber>() {
        @Override
        public PhoneNumber createFromParcel(Parcel source) {
            Contract.throwIfArgNull(source, "source");

            return new PhoneNumber(source);
        }

        @Override
        public PhoneNumber[] newArray(int size) {
            return new PhoneNumber[size];
        }
    };
}