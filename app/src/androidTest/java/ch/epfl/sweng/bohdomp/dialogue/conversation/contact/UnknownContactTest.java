package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.os.Parcel;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;

/**
 * Tests the UnknownContact class
 */
public class UnknownContactTest extends AndroidTestCase {

    private static final int ARRAY_SIZE = 7;

    private ContactFactory mContactFactory;
    private UnknownContact mContact;

    public void setUp() throws InvalidNumberException {
        mContactFactory = new ContactFactory(getContext());
        mContact = (UnknownContact) mContactFactory.contactFromNumber("+41 21 693 11 11");
    }

    public void testParcelRoundTrip() {


        /*
         The factory should return an UnknownContact but to
         be sure we add this assert.
         */
        assertEquals(mContact.getClass(), UnknownContact.class);


        Parcel parcel = Parcel.obtain();
        mContact.writeToParcel(parcel, 0);

        parcel.setDataPosition(0); // reset parcel for reading

        Contact contactFromParcel = UnknownContact.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertEquals(mContact, contactFromParcel);
    }

    public void testEqualsNull() {
        assertFalse(mContact.equals(null));
    }

    public void testDescribeContent() {
        assertEquals(0, mContact.describeContents());
    }

    public void testNewArray() {
        UnknownContact[] newArray = UnknownContact.CREATOR.newArray(ARRAY_SIZE);

        assertEquals(ARRAY_SIZE, newArray.length);
    }
}