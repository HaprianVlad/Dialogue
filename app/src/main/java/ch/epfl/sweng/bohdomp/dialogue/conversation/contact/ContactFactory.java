package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.content.ContentProviderOperation;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.util.Patterns;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.FingerprintUtils;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.ContactLookupException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.FingerprintInsertionException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NoFingerprintException;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * factory creating different contacts
 */
public class ContactFactory {

    private final Context mContext;

    private static final String FINGERPRINT_MIMETYPE = "vnd.android.cursor.item/dialogue_fingerprint";

    private static final String[] LOOKUPKEY_PROJECTION =
            new String[] {ContactsContract.Contacts.LOOKUP_KEY };

    /**
     * @param context context of the application used to look up contact information
     * @throws IllegalArgumentException
     */
    public ContactFactory(final Context context) {
        Contract.throwIfArgNull(context, "context");

        this.mContext = context;
    }

    /**
     *
     * @param phoneNumber phone number of the contact for which we want to add a fingerprint
     * @param fingerprint the fingerprint to insert in the contact database
     * @throws FingerprintInsertionException when contact associated with phoneNumber is not known
     *                                       or insertion in database failed
     */
    public void insertFingerprintForPhoneNumber(final String phoneNumber, final String fingerprint)
        throws FingerprintInsertionException {

        Contract.throwIfArgNull(phoneNumber, "phoneNumber");
        Contract.throwIfArg(!verifyPhoneNumber(phoneNumber), "invalid phone number");
        Contract.throwIfArgNull(fingerprint, "fingerprint");
        Contract.throwIfArg(!FingerprintUtils.isValidFingerPrint(fingerprint), "invalid fingerprint");

        final String lookupKey = lookupKeyFromPhoneNumber(phoneNumber);

        if (lookupKey == null) {
            throw new FingerprintInsertionException("contact with phone number: " + phoneNumber + "not known");
        }

        insertFingerprintForLookupKey(lookupKey, fingerprint);
    }

    /**
     *
     * @param lookupKey android specific lookupKey of contact for which we want to add a fingerprint
     * @param fingerprint the fingerprint to insert in the contact database
     * @throws FingerprintInsertionException on insertion failure
     */
    public void insertFingerprintForLookupKey(final String lookupKey, final String fingerprint)
        throws FingerprintInsertionException {

        Contract.throwIfArgNull(lookupKey, "lookupKey");
        Contract.throwIfArgNull(fingerprint, "fingerprint");
        Contract.throwIfArg(!FingerprintUtils.isValidFingerPrint(fingerprint), "fingerprint invalid");

        final String contactId;
        try {
            contactId = AndroidContact.contactIdFromLookupKey(lookupKey, mContext);
        } catch (ContactLookupException e) {
            throw new FingerprintInsertionException(
                    "could not find contact associated with given lookup key: " + lookupKey,
                    e);
        }

        final Set<String> rawContactIds = rawContactIdsFromId(contactId, mContext);

        for (final String rawId : rawContactIds) {
            fingerprintUpdateOrInsert(rawId, fingerprint, mContext);
        }
    }

    /**
     *
     * @param lookupKey opaque value for contact look ups
     *                  see:
     *                  http://developer.android.com/reference/android/provider/ContactsContract.Contacts.html
     * @return a Contact for this lookup key
     * @throws ContactLookupException when given lookupKey is invalid
     */
    public Contact contactFromLookupKey(final String lookupKey) throws ContactLookupException {
        Contract.throwIfArgNull(lookupKey, "lookupKey");

        return new AndroidContact(lookupKey, mContext);
    }

    /**
     * create contact from given phone number
     * tries to fill in missing information in case the number is associated with a known
     * contact
     *
     * @param phoneNumber the phonenumber we want to create a contact from.
     * @return a Contact for this number
     * @throws InvalidNumberException when given phone number is not valid
     */
    public Contact contactFromNumber(final String phoneNumber)
        throws InvalidNumberException {

        Contract.throwIfArgNull(phoneNumber, "phone number");

        if (!verifyPhoneNumber(phoneNumber)) {
            throw new InvalidNumberException(" is not a valid phone number");
        }

        final String lookupKey = lookupKeyFromPhoneNumber(phoneNumber);

        if (lookupKey == null) {
            return new UnknownContact(phoneNumber);
        } else {
            try {
                return new AndroidContact(lookupKey, mContext);
            } catch (ContactLookupException e) {
                return new UnknownContact(phoneNumber);
            }
        }
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

    private static void fingerprintUpdateOrInsert(final String rawId, final String fingerprint, final Context context)
        throws FingerprintInsertionException {

        ArrayList<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();

        operations.add(ContentProviderOperation.newDelete(ContactsContract.Data.CONTENT_URI)
            .withSelection(
                    ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                    new String[] {rawId, FINGERPRINT_MIMETYPE})
            .build());

        operations.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
            .withValue(ContactsContract.Data.RAW_CONTACT_ID, rawId)
            .withValue(ContactsContract.Data.MIMETYPE, FINGERPRINT_MIMETYPE)
            .withValue(ContactsContract.Data.DATA1, fingerprint)
            .build());

        try {
            context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, operations);
        } catch (RemoteException e) {
            throw new FingerprintInsertionException(e);
        } catch (OperationApplicationException e) {
            throw new FingerprintInsertionException(e);
        }
    }

    /*
     * inspired by:
     * http://stackoverflow.com/questions/9012263/get-rawcontactid-of-specific-contact-based-off-phonelookup
     */
    private static Set<String> rawContactIdsFromId(final String id, final Context context) {
        Contract.assertNotNull(id, "id");
        Contract.assertNotNull(context, "context");

        HashSet<String> result = new HashSet<String>();

        Cursor rawIdCursor = context.getContentResolver().query(
                ContactsContract.RawContacts.CONTENT_URI,
                new String[] {ContactsContract.RawContacts._ID},
                ContactsContract.RawContacts.CONTACT_ID + " = ?",
                new String[] {id},
                null);

        if (rawIdCursor == null || !rawIdCursor.moveToFirst()) {
            return result;
        }

        do {
            result.add(rawIdCursor.getString(rawIdCursor.getColumnIndex(ContactsContract.RawContacts._ID)));
        } while (rawIdCursor.moveToNext());

        rawIdCursor.close();

        return result;
    }

    /**
     * phone number verification copied from:
     * http://stackoverflow.com/questions/5958665/validation-for-a-cell-number-in-android
     * @param phoneNumber to test
     * @return if phoneNumber is a valid
     */
    private static boolean verifyPhoneNumber(final CharSequence phoneNumber) {
        if (phoneNumber == null || TextUtils.isEmpty(phoneNumber)) {
            return false;
        } else {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
    }

    /**
     * dialogue representation of an android contact
     */
    private static class AndroidContact implements Contact {
        private final String mLookupKey;
        private final String mDisplayName;
        private final String mFingerprint;
        private final Map<ChannelType, Set<PhoneNumber>> mPhoneNumberMap;
        private final Set<PhoneNumber> mPhoneNumbers;

        private static final String[] NAME_PROJECTION = new String[]{
            ContactsContract.Contacts.DISPLAY_NAME};

        private static final String[] ID_PROJECTION = new String[]{
            ContactsContract.Contacts._ID};

        AndroidContact(final String lookupKey, final Context context) throws ContactLookupException {
            Contract.throwIfArgNull(lookupKey, "lookupKey");
            Contract.throwIfArgNull(context, "context");

            this.mLookupKey = lookupKey;
            this.mDisplayName = displayNameFromLookupKey(lookupKey, context);
            this.mFingerprint = fingerprintFromLookupKey(lookupKey, context);
            this.mPhoneNumbers = phoneNumbersFromLookupKey(lookupKey, context);
            this.mPhoneNumberMap = channelMapFromPhoneNumberSet(mPhoneNumbers);
        }

        @Override
        public String getDisplayName() {
            return mDisplayName;
        }

        @Override
        public Set<PhoneNumber> getPhoneNumbers() {
            return mPhoneNumbers;
        }

        @Override
        public Set<PhoneNumber> getPhoneNumbers(final ChannelType channel) {
            Contract.throwIfArgNull(channel, "channel");

            if (mPhoneNumberMap.containsKey(channel)) {
                return mPhoneNumberMap.get(channel);
            } else {
                return new HashSet<PhoneNumber>();
            }
        }

        @Override
        public Set<ChannelType> availableChannels() {
            return mPhoneNumberMap.keySet();
        }

        @Override
        public boolean hasFingerprint() {
            return this.mFingerprint != null;
        }

        @Override
        public String getFingerprint() throws NoFingerprintException {
            if (this.mFingerprint != null) {
                return this.mFingerprint;
            } else {
                throw new NoFingerprintException("contact has no fingerprint");
            }
        }

        @Override
        public Contact updateInfo(final Context context) {
            Contract.throwIfArgNull(context, "context");

            // since database look-ups are done in constructor we
            // try to recreate this contact from its look-up-key
            try {
                return new AndroidContact(this.mLookupKey, context);
            } catch (ContactLookupException e) {
                // contact has been deleted in the mean-time
                return this;
            }
        }

        @Override
        public Creator<Contact> getParcelCreator() {
            return CREATOR;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            return this.mLookupKey.equals(((AndroidContact) o).mLookupKey);
        }

        @Override
        public int hashCode() {
            return mLookupKey.hashCode();
        }


        private static Map<ChannelType, Set<PhoneNumber>> channelMapFromPhoneNumberSet(
            final Set<PhoneNumber> phoneNumbers) {

            Map<ChannelType, Set<PhoneNumber>> result = new HashMap<ChannelType, Set<PhoneNumber>>();

            for (ChannelType chan : ChannelType.values()) {
                result.put(chan, phoneNumberSubsetByChannel(chan, phoneNumbers));
            }

            return result;
        }


        private static Set<PhoneNumber> phoneNumberSubsetByChannel(
            final ChannelType channel,
            final Set<PhoneNumber> numbers) {

            switch (channel) {
                case SMS:
                    return smsSubset(numbers);
                // TODO figure out how to know if we can send mms
                default:
                    return new HashSet<PhoneNumber>();
            }
        }

        private static Set<PhoneNumber> smsSubset(final Set<PhoneNumber> phoneNumbers) {
            final Set<PhoneNumber> result = new HashSet<PhoneNumber>();

            for (PhoneNumber number : phoneNumbers) {
                if (PhoneNumberUtils.isWellFormedSmsAddress(number.getNumber())) {
                    result.add(number);
                }
            }

            return result;
        }

        private static String fingerprintFromLookupKey(final String lookupKey, final Context context)
            throws ContactLookupException {

            Contract.assertNotNull(lookupKey, "lookupKey");
            Contract.assertFalse(lookupKey.isEmpty(), "lookupKey mustn't be empty string");
            Contract.assertNotNull(context, "context");

            final String contactId = contactIdFromLookupKey(lookupKey, context);

            if (contactId == null) {
                throw new ContactLookupException("got an invalid id");
            }

            final Set<String> rawIds = rawContactIdsFromId(contactId, context);

            Contract.assertTrue(rawIds.iterator().hasNext(), "contact must have raw ids associated!");

            final String rawId = rawIds.iterator().next();

            Cursor fingerprintCursor = context.getContentResolver().query(
                    ContactsContract.Data.CONTENT_URI,
                    new String[] {ContactsContract.Data.DATA1},
                    ContactsContract.Data.RAW_CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?",
                    new String[] {rawId, FINGERPRINT_MIMETYPE},
                    null);

            if (fingerprintCursor == null) {
                return null;
            }

            final String result = fingerprintCursor.moveToFirst()
                ? fingerprintCursor.getString(fingerprintCursor.getColumnIndex(ContactsContract.Data.DATA1))
                : null;

            fingerprintCursor.close();

            return result;
        }

        /**
         * sample code taken from this question:
         * http://stackoverflow.com/questions/9554743/how-to-obtain-lookup-key-in-android-contacts-api
         * @param lookupKey android specific lookupKey for this contact
         * @param context application context, will use its ContentResolver to lookup displayName
         * @return display name of contact associated with lookupKey
         */
        private static String displayNameFromLookupKey(final String lookupKey, final Context context)
            throws ContactLookupException {

            Contract.assertNotNull(lookupKey, "lookupKey");
            Contract.assertTrue(!lookupKey.isEmpty(), "lookupKey is empty string");
            Contract.assertNotNull(context, "context");

            Uri lookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);

            Cursor cursor = context.getContentResolver().query(
                    lookupUri,
                    NAME_PROJECTION,
                    null,
                    null,
                    null);

            if (cursor == null) {
                throw new ContactLookupException("no lookup cursor");
            }

            if (!cursor.moveToFirst()) {
                throw new ContactLookupException("no data in cursor");
            }

            final String result =
                    cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            cursor.close();

            return result;
        }

        private static String contactIdFromLookupKey(final String lookupKey, final Context context)
            throws ContactLookupException {

            Contract.assertNotNull(lookupKey, "lookupKey");
            Contract.assertTrue(!lookupKey.isEmpty(), "lookupKey is empty string");
            Contract.assertNotNull(context, "context");

            Uri lookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);

            Cursor cursor = context.getContentResolver().query(
                    lookupUri,
                    ID_PROJECTION,
                    null,
                    null,
                    null);

            if (cursor == null) {
                throw new ContactLookupException("no lookup cursor");
            }

            if (!cursor.moveToFirst()) {
                throw new ContactLookupException("no data in cursor");
            }

            final String result = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

            cursor.close();

            return result;
        }

        /**
         * inspired by
         * http://stackoverflow.com/questions/2356084/read-all-contacts-phone-numbers-in-android
         *
         * @param lookupKey android specific lookupKey for this contact
         * @param context application context, will use its ContentResolver to lookup displayName
         * @return all phone numbers associated to contact with specific lookupKey
         */
        private static Set<PhoneNumber> phoneNumbersFromLookupKey(final String lookupKey, final Context context)
            throws ContactLookupException {

            Contract.assertNotNull(lookupKey, "lookupKey");
            Contract.assertTrue(!lookupKey.isEmpty(), "lookupKey is empty string");
            Contract.assertNotNull(context, "context");

            final HashSet<PhoneNumber> result = new HashSet<PhoneNumber>();

            final String id = contactIdFromLookupKey(lookupKey, context);

            if (id == null) {
                throw new ContactLookupException("got invalid id");
            }

            Cursor phoneCursor = context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{id},
                null);

            for (phoneCursor.moveToFirst(); !phoneCursor.isAfterLast(); phoneCursor.moveToNext()) {
                final String phoneNumber = phoneCursor.getString(
                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                final int androidPhoneType = phoneCursor.getInt(
                    phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                result.add(new PhoneNumber(phoneNumber, tagFromAndroidPhoneType(androidPhoneType)));
            }

            phoneCursor.close();

            return result;
        }

        private static PhoneNumber.Tag tagFromAndroidPhoneType(final int androidPhoneType) {
            switch (androidPhoneType) {
                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                    return PhoneNumber.Tag.MOBILE;
                case ContactsContract.CommonDataKinds.Phone.TYPE_HOME:
                    return PhoneNumber.Tag.HOME;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK:
                    return PhoneNumber.Tag.WORK;
                case ContactsContract.CommonDataKinds.Phone.TYPE_WORK_MOBILE:
                    return PhoneNumber.Tag.WORK_MOBILE;
                default:
                    return PhoneNumber.Tag.OTHER;
            }
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Contract.throwIfArgNull(dest, "dest");

            dest.writeString(this.mLookupKey);
            dest.writeString(this.mDisplayName);

            if (this.mFingerprint != null) {
                dest.writeString(this.mFingerprint);
            } else {
                dest.writeString("");
            }

            List<PhoneNumber> phoneNumbers = new ArrayList<PhoneNumber>(this.mPhoneNumbers);
            dest.writeList(phoneNumbers);
        }

        @SuppressWarnings("unchecked")
        private AndroidContact(Parcel in) {
            this.mLookupKey = in.readString();
            this.mDisplayName = in.readString();

            final String maybeFingerprint = in.readString();

            if (maybeFingerprint.isEmpty()) {
                this.mFingerprint = null;
            } else {
                this.mFingerprint = maybeFingerprint;
            }

            this.mPhoneNumbers = new HashSet<PhoneNumber>(in.readArrayList(getClass().getClassLoader()));
            this.mPhoneNumberMap = channelMapFromPhoneNumberSet(this.mPhoneNumbers);
        }

        public static final Creator<Contact> CREATOR = new Creator<Contact>() {
            public AndroidContact createFromParcel(Parcel source) {
                Contract.throwIfArgNull(source, "source");

                return new AndroidContact(source);
            }

            public AndroidContact[] newArray(int size) {
                return new AndroidContact[size];
            }
        };
    }

    /**
     * class representing a contact for which no entry was found in the contact database
     */
    private static class UnknownContact implements Contact {

        private final String mPhoneNumber;

        public UnknownContact(final String phoneNumber) {
            Contract.throwIfArgNull(phoneNumber, "phone number string");

            this.mPhoneNumber = phoneNumber;
        }

        @Override
        public String getDisplayName() {
            return "unknown: " + mPhoneNumber;
        }

        @Override
        public Set<PhoneNumber> getPhoneNumbers() {
            Set<PhoneNumber> result = new HashSet<PhoneNumber>();
            result.add(new PhoneNumber(mPhoneNumber, PhoneNumber.Tag.OTHER));
            return result;
        }

        @Override
        public Set<PhoneNumber> getPhoneNumbers(ChannelType channel) {
            Contract.throwIfArgNull(channel, "channel");

            switch (channel) {
                case SMS:
                    return getPhoneNumbers();
                default:
                    return new HashSet<PhoneNumber>();
            }
        }

        @Override
        public Set<ChannelType> availableChannels() {
            Set<ChannelType> result = new HashSet<ChannelType>();
            result.add(ChannelType.SMS);
            return result;
        }

        @Override
        public boolean hasFingerprint() {
            return false;
        }

        @Override
        public String getFingerprint() throws  NoFingerprintException {
            throw new NoFingerprintException("unknown contact has no fingerprint");
        }

        @Override
        public Contact updateInfo(final Context context) {
            Contract.throwIfArgNull(context, "context");

            try {
                return new ContactFactory(context).contactFromNumber(mPhoneNumber);
            } catch (InvalidNumberException e) {
                return this;
            }
        }

        @Override
        public Creator<Contact> getParcelCreator() {
            return CREATOR;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (o == null || this.getClass() != o.getClass()) {
                return false;
            }

            /*
             * from the android api at:
             * http://developer.android.com/reference/android/telephony/PhoneNumberUtils.html
             *
             * public static boolean compare (String a, String b)
             * Compare phone numbers a and b, return true if they're identical enough for caller ID purposes.
             */
            return PhoneNumberUtils.compare(this.mPhoneNumber, ((UnknownContact) o).mPhoneNumber);
        }

        @Override
        public int hashCode() {
            return PhoneNumberUtils.toCallerIDMinMatch(this.mPhoneNumber).hashCode();
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            Contract.throwIfArgNull(dest, "dest");

            dest.writeString(this.mPhoneNumber);
        }

        private UnknownContact(Parcel in) {
            this.mPhoneNumber = in.readString();
        }

        public static final Creator<Contact> CREATOR = new Creator<Contact>() {
            public UnknownContact createFromParcel(Parcel source) {
                Contract.throwIfArgNull(source, "source");
                return new UnknownContact(source);
            }

            public UnknownContact[] newArray(int size) {
                return new UnknownContact[size];
            }
        };
    }
}
