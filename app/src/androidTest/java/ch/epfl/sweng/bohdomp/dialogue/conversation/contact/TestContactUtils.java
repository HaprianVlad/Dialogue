package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;

import java.util.ArrayList;

/**
 * Utilities for testing contacts
 */
public class TestContactUtils {

    public static void addContact(Context context, final String displayName, final String phoneNumber)
        throws RemoteException, OperationApplicationException {

        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        // add new raw contact
        operations.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, displayName)
                .build());

        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, 0)
                .withValue(ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phoneNumber)
                .withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
                .build());

        context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
    }

    /* inspired by
     * http://stackoverflow.com/questions/9625308/android-find-a-contact-by-display-name
     */
    public static void removeContactByDisplayName(Context context, final String displayName) {
        ContentResolver resolver = context.getContentResolver();

        Cursor lookUpKeyCursor = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                new String[] {ContactsContract.PhoneLookup.LOOKUP_KEY},
                ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME + " = ?",
                new String[] {displayName},
                null);

        if (lookUpKeyCursor.moveToFirst()) {
            String lookUpKey = lookUpKeyCursor.getString(
                    lookUpKeyCursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));

            Uri deletionUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookUpKey);
            resolver.delete(deletionUri, null, null);
        }
    }
}
