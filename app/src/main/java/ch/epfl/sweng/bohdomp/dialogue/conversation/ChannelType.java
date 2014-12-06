package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.os.Parcel;
import android.os.Parcelable;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * enumeration for channel types
 */
public enum ChannelType implements Parcelable {
    SMS, MMS, ChannelType;

    @Override
    public String toString() {
        switch (this) {
            case SMS:
                return "SMS";
            case MMS:
                return "MMS";
            default :
                return this.name();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        Contract.throwIfArgNull(dest, "dest");

        dest.writeInt(ordinal());
    }

    public static final Creator<ChannelType> CREATOR = new Creator<ChannelType>() {
        @Override
        public ChannelType createFromParcel(Parcel source) {
            Contract.throwIfArgNull(source, "source");

            return ChannelType.values()[source.readInt()];
        }

        @Override
        public ChannelType[] newArray(int size) {
            return new ChannelType[size];
        }
    };
}