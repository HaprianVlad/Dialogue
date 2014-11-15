package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.os.Parcel;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;

/**
 * Tests the UnknownContact class
 */
public class UnknownContactTest extends AndroidTestCase {
    public void testParcelRoundTrip() throws InvalidNumberException {
        ContactFactory contactFactory = new ContactFactory(getContext());
        Contact contact = contactFactory.contactFromNumber("+41 21 693 11 11");

        /*
         The factory should return an UnknownContact but to
         be sure we add this assert.
         */
        assertEquals(contact.getClass(), UnknownContact.class);


        Parcel parcel = Parcel.obtain();
        contact.writeToParcel(parcel, 0);

        parcel.setDataPosition(0); // reset parcel for reading

        Contact contactFromParcel = UnknownContact.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertEquals(contact, contactFromParcel);
    }
}