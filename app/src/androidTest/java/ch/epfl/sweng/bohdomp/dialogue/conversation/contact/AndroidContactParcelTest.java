package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;

/**
 * Tests the AndroidContact class.
 */
public class AndroidContactParcelTest extends AndroidTestCase {
    public void testParcelRoundTrip() throws InvalidNumberException {
        Contact contact = getContact();


        Parcel parcel = Parcel.obtain();
        contact.writeToParcel(parcel, 0);

        parcel.setDataPosition(0); // reset parcel for reading

        Contact contactFromParcel = contact.getParcelCreator().createFromParcel(parcel);

        parcel.recycle();

        assertEquals(contact, contactFromParcel);
    }

    public void testDescribeContents() throws InvalidNumberException {
        assertEquals(0, getContact().describeContents());
    }

    /* inspired by
     * http://stackoverflow.com/questions/9625308/android-find-a-contact-by-display-name
     */
    private static void removeContactByDisplayName(Context context, final String displayName) {
        ContentResolver resolver = context.getContentResolver();

        Cursor lookUpKeyCursor = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.PhoneLookup.LOOKUP_KEY},
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?",
                new String[]{displayName},
                null);

        if (lookUpKeyCursor.moveToFirst()) {
            String lookUpKey = lookUpKeyCursor.getString(
                    lookUpKeyCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));

            Uri deletionUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookUpKey);
            resolver.delete(deletionUri, null, null);
        }
    }

    private Contact getContact() throws InvalidNumberException {
        final Context context = getContext();

        final String phoneNumber = "+41 21 693 11 11";
        final String displayName = "dummy";

        final ContactFactory contactFactory = new ContactFactory(getContext());

        // contact not yet known
        Contact contact = contactFactory.contactFromNumber(phoneNumber);

        try {
            TestContactUtils.addContact(context, displayName, phoneNumber);

            // contact should be known now since it was just added
            contact = contactFactory.contactFromNumber(phoneNumber);
        } catch (RemoteException e) {
            fail();
        } catch (OperationApplicationException e) {
            fail();
        } finally {
            removeContactByDisplayName(context, displayName);
        }
        return contact;
    }
}