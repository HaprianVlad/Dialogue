package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.os.Parcel;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;

/**
 * test implementation of Parcelable interface for Contact.ChannelType
 */
public class ChannelTypeParcelTest extends AndroidTestCase {

    private static ChannelType[] mChannelTypes;

    @Override
    public void setUp() {
        mChannelTypes = ChannelType.values();
    }

    public void testDescribeContents() {
        for (ChannelType chan : mChannelTypes) {
            assertTrue(chan.describeContents() == 0);
        }
    }

    public void testParcelPackAndUnpack() {
        for (ChannelType chan : mChannelTypes) {

            Parcel parcel = Parcel.obtain();
            chan.writeToParcel(parcel, 0);
            parcel.setDataPosition(0);

            ChannelType unpacked = ChannelType.CREATOR.createFromParcel(parcel);

            parcel.recycle();

            assertTrue(unpacked == chan);
        }
    }
}
