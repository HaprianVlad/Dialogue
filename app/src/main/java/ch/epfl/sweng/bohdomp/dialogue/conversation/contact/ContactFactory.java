package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.List;

/**
 * factory creating different contacts
 */
public class ContactFactory {

    private final Context mContext;

    private static final String[] CONTACT_LIST_PROJECTION =
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
    public Contact contactFromNumber(final String phoneNumber) throws IllegalArgumentException {
        //TODO lookup in database if there's a contact matching this phone number
        // and create AndroidContact if possible
        if (!verifyPhoneNumber(phoneNumber)) {
            throw new IllegalArgumentException(phoneNumber + " is not a valid phone number");
        }
        return new UnknownContact(phoneNumber);
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
                CONTACT_LIST_PROJECTION,
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
