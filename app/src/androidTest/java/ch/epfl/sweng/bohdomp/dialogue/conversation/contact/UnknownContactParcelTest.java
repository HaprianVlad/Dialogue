package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.os.Parcel;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;

/**
 * Tests the UnknownContact class
 */
public class UnknownContactParcelTest extends AndroidTestCase {
    public void testParcelRoundTrip() throws InvalidNumberException {
        ContactFactory contactFactory = new ContactFactory(getContext());
        Contact contact = contactFactory.contactFromNumber("+41 21 693 11 11");


        Parcel parcel = Parcel.obtain();
        contact.writeToParcel(parcel, 0);

        parcel.setDataPosition(0); // reset parcel for reading

        Contact contactFromParcel = contact.getParcelCreator().createFromParcel(parcel);

        parcel.recycle();

        assertEquals(contact, contactFromParcel);
    }
}