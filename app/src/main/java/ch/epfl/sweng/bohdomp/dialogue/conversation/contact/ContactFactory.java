package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * factory creating different contacts
 */
public class ContactFactory {

    private final Context mContext;

    private static final String[] LOOKUPKEY_PROJECTION =
            new String[] {ContactsContract.Contacts.LOOKUP_KEY };

    /**
     * @param context context of the application used to look up contact information
     * @throws IllegalArgumentException
     */
    public ContactFactory(final Context context) throws IllegalArgumentException {
        if (context == null) {
            throw new IllegalArgumentException(
                    "context passed to ContactFactory constructor must not be null!");
        }
        this.mContext = context;
    }

    /**
     * create contact from given phone number
     * tries to fill in missing information in case the number is associated with a known
     * contact
     *
     * @param phoneNumber
     * @return a Contact for this number
     */
    public Contact contactFromNumber(final String phoneNumber) {

        if (phoneNumber == null) {
            throw new NullArgumentException("phoneNumber");
        }
        if (!verifyPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException(phoneNumber + " is not a valid phone number");
        }

        final String lookupKey = lookupKeyFromPhoneNumber(phoneNumber);

        if (lookupKey == null) {
            return new UnknownContact(phoneNumber);
        } else {
            return new AndroidContact(lookupKey, mContext);
        }
    }

    /**
     * @return list of all contacts currently on the phone
     */
    public List<Contact> knownContacts() {
        Cursor cursor = contactListCursor();
        return contactListFromCursor(cursor);
    }

    private Cursor contactListCursor() {
        return mContext.getContentResolver().query(
                ContactsContract.Contacts.CONTENT_URI,
                LOOKUPKEY_PROJECTION,
                null,
                null,
                null);
    }

    private List<Contact> contactListFromCursor(Cursor cursor) {
        List<Contact> result = new ArrayList<Contact>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            result.add(new AndroidContact(
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY)),
                    mContext));
        }
        cursor.close();
        return result;
    }

    /**
     * inspired by
     * http://stackoverflow.com/questions/5553867/get-contact-by-phone-number-on-android
     */
    private String lookupKeyFromPhoneNumber(final String phoneNumber) {
        Uri uri = Uri.withAppendedPath(
                ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                Uri.encode(phoneNumber));
        Cursor lookupKeyCursor = mContext.getContentResolver().query(
                uri,
                LOOKUPKEY_PROJECTION,
                null,
                null,
                null);

        final String result;
        if (lookupKeyCursor.moveToFirst()) {
            result = lookupKeyCursor.getString(
                    lookupKeyCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
        } else {
            result = null;
        }

        lookupKeyCursor.close();

        return result;
    }

    /**
     * phone number verification copied from:
     * http://stackoverflow.com/questions/5958665/validation-for-a-cell-number-in-android
     * @param phoneNumber to test
     * @return if phoneNumber is a valid
     */
    private boolean verifyPhoneNumber(final CharSequence phoneNumber) {
        if (phoneNumber == null || TextUtils.isEmpty(phoneNumber)) {
            return false;
        } else {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
    }
}
