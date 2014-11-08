package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.HashSet;
import java.util.Set;

/**
 * dialogue representation of an android contact
 */
public class AndroidContact implements Contact {
    private final String mLookupKey;
    private final String mDisplayName;
    private final Set<String> mPhoneNumbers;
    private final Set<ChannelType> mAvailableChannels;

    private static final String[] PROJECTION = new String[]{
        ContactsContract.Contacts.DISPLAY_NAME};

    AndroidContact(final String lookupKey, final Context context) {
        this.mLookupKey = lookupKey;
        this.mDisplayName = displayNameFromLookupKey(lookupKey, context);

        //TODO lookup phone numbers associated with contact
        this.mPhoneNumbers = new HashSet<String>();

        //TODO determine what which channels this contact can use
        this.mAvailableChannels = new HashSet<ChannelType>();
    }

    @Override
    public String getDisplayName() {
        return mDisplayName;
    }

    @Override
    public Set<String> getPhoneNumbers() {
        return mPhoneNumbers;
    }

    @Override
    public Set<ChannelType> availableChannels() {
        return mAvailableChannels;
    }

    @Override
    public Contact updateInfo(final Context context) {
        // since database look-ups are done in constructor we
        // try to recreate this contact from its look-up-key
        return new AndroidContact(this.mLookupKey, context);
    }

    /*
    TODO would probably be nice to be able to compare to any Contact
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }

        return this.mLookupKey == ((AndroidContact) o).mLookupKey;
    }

    @Override
    public int hashCode() {
        return mLookupKey.hashCode();
    }
    */

    /**
     * sample code taken from this question:
     * http://stackoverflow.com/questions/9554743/how-to-obtain-lookup-key-in-android-contacts-api
     * @param lookupKey android specific lookupKey for this contact
     * @param context application context, will use its ContentResolver to lookup displayName
     * @return display name of contact associated with lookupKey
     */
    private static String displayNameFromLookupKey(final String lookupKey, final Context context) {

        Uri lookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);

        Cursor cursor = context.getContentResolver().query(lookupUri, PROJECTION, null, null, null);

        final String result;
        if (cursor.moveToFirst()) {
            result = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        } else {
            result = "";
        }

        cursor.close();

        return result;
    }
}
