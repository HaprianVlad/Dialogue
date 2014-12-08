package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.os.Parcel;
import android.test.AndroidTestCase;

/**
 * test parcelable implementation for Contact.PhoneNumber
 */
public class PhoneNumberParcelTest extends AndroidTestCase {

    private PhoneNumber mPhoneNumber;

    @Override
    public void setUp() {
        mPhoneNumber = new PhoneNumber("0123456789", PhoneNumber.Tag.MOBILE);
    }

    public void testDescribeContents() {
        assertTrue(mPhoneNumber.describeContents() == 0);
    }

    public void testParcelPackAndUnpack() {
        Parcel parcel = Parcel.obtain();

        mPhoneNumber.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        PhoneNumber unpacked = PhoneNumber.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertEquals(mPhoneNumber, unpacked);
    }
}
