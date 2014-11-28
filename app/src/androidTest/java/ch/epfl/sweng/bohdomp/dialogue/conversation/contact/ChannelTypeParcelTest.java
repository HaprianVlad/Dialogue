package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.os.Parcel;
import android.test.AndroidTestCase;

/**
 * test implementation of Parcelable interface for Contact.ChannelType
 */
public class ChannelTypeParcelTest extends AndroidTestCase {

    private static Contact.ChannelType[] mChannelTypes;

    @Override
    public void setUp() {
        mChannelTypes = Contact.ChannelType.values();
    }

    public void testDescribeContents() {
        for (Contact.ChannelType chan : mChannelTypes) {
            assertTrue(chan.describeContents() == 0);
        }
    }

    public void testParcelPackAndUnpack() {
        for (Contact.ChannelType chan : mChannelTypes) {

            Parcel parcel = Parcel.obtain();
            chan.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            Contact.ChannelType unpacked = Contact.ChannelType.CREATOR.createFromParcel(parcel);

            parcel.recycle();

            assertTrue(unpacked == chan);
        }
    }
}
