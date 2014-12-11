package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.app.Application;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.test.ApplicationTestCase;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.ContactLookupException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.FingerprintInsertionException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NoFingerprintException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 *  junit test class for ContactFactory class
 *
 */
public class ContactFactoryTest extends ApplicationTestCase<Application> {

    private ContactFactory mContactFactory;

    private static final String DISPLAY_NAME_1 = "Dummy 1";
    private static final String DISPLAY_NAME_2 = "Dummy 2";
    private static final String DISPLAY_NAME_3 = "Dummy 3";

    private static final String PHONE_1 = "012 345 67 89";
    private static final String PHONE_2 = "123 456 78 90";
    private static final String PHONE_3 = "234 567 89 01";

    private static final String[] ALL_NAMES = {DISPLAY_NAME_1, DISPLAY_NAME_2, DISPLAY_NAME_3};

    private static final String DUMMY_FINGERPRINT_0 =
            "FFFF FFFF FFFF FFFF FFFF"
            + " FFFF FFFF FFFF FFFF FFFF";

    private static final String DUMMY_FINGERPRINT_1 =
            "DEAD BEAF DEAD BEEF DEAD"
            + " BEEF DEAD BEEF DEAD BEEF";

    private static final String[] LOOKUPKEY_PROJECTION =
            new String[] {ContactsContract.Contacts.LOOKUP_KEY };

    public ContactFactoryTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context context = getContext();

        TestContactUtils.addContact(context, DISPLAY_NAME_1, PHONE_1);
        TestContactUtils.addContact(context, DISPLAY_NAME_2, PHONE_2);
        TestContactUtils.addContact(context, DISPLAY_NAME_3, PHONE_3);

        this.mContactFactory = new ContactFactory(context);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        for (final String name : ALL_NAMES) {
            TestContactUtils.removeContactByDisplayName(getContext(), name);
        }
    }

    public void testContactFromPhoneNumberWhenNumberIsNotKnownButValid()  {
        String validPhoneNumber = "+41 21 693 11 11"; //EPFL front desk .-P

        Contact contact = null;
        try {
            contact = mContactFactory.contactFromNumber(validPhoneNumber);
        } catch (InvalidNumberException e) {
            fail("Exception should not be thrown here!");
        }

        String expectedName = "unknown: " + validPhoneNumber;
        assertEquals(expectedName, contact.getDisplayName());

        Set<PhoneNumber> phoneNumbersInContact = contact.getPhoneNumbers();
        Set<String> phoneNumberStrings = new HashSet<String>();

        for (PhoneNumber num : phoneNumbersInContact) {
            phoneNumberStrings.add(num.getNumber());
        }

        // phoneNumbersInContact should contain exactly one phone number,
        // which is the one we passed in
        assertEquals(1, phoneNumbersInContact.size());
        assertTrue(phoneNumberStrings.contains(validPhoneNumber));

        // we assume that contact is reachable via SMS when there is a phone number
        Set<ChannelType> contactAvailableChannels = contact.availableChannels();
        assertEquals(1, contactAvailableChannels.size());
        assertTrue(contactAvailableChannels.contains(ChannelType.SMS));
    }

    public void testContactFromPhoneNumberWhenNumberIsNotKnownAndInvalid() {
        String invalidPhoneNumber = "not a phone number";

        try {
            Contact contact = mContactFactory.contactFromNumber(invalidPhoneNumber);
            fail("should have thrown InvalidNumberException");
        } catch (InvalidNumberException e) {

        }
    }

    public void testContactFromPhoneNumberWhenNumberIsKnownAndIdentical() {
        Contact contact = null;
        try {
            contact = mContactFactory.contactFromNumber(PHONE_1);
        } catch (InvalidNumberException e) {
            fail("Exception should not be thrown here!");
        }
        assertTrue(contact.getDisplayName().equals(DISPLAY_NAME_1));
    }

    public void testContactFromPhoneNumberWhenNumberIsKnownAndDifferentFormat() {
        Contact contact = null;
        try {
            contact = mContactFactory.contactFromNumber(PHONE_1.replaceAll("\\s", ""));
        } catch (InvalidNumberException e) {
            fail("Exception should not be thrown here!");
        }
        assertTrue(contact.getDisplayName().equals(DISPLAY_NAME_1));
    }

    public void testContactFromPhoneNumberReturnsEqualUnknownContactsFromEqualPhoneNumbers()
        throws InvalidNumberException {
        final String validUnknownPhoneNumber = "+41 21 693 11 11";

        final Contact firstContact = mContactFactory.contactFromNumber(validUnknownPhoneNumber);
        final Contact secondContact = mContactFactory.contactFromNumber(validUnknownPhoneNumber);

        contactEqualityCheck(firstContact, secondContact);
    }

    public void testContactFromPhoneNumberReturnsEqualUnknownContactsFromReformattedPhoneNumbers()
        throws InvalidNumberException {
        final String validUnknownPhoneNumber = "+41 21 693 11 11";
        final String reformattedValidUnknownPhoneNumber = "0216931111"; // no country prefix and no spaces

        final Contact firstContact = mContactFactory.contactFromNumber(validUnknownPhoneNumber);
        final Contact secondContact = mContactFactory.contactFromNumber(reformattedValidUnknownPhoneNumber);

        contactEqualityCheck(firstContact, secondContact);
    }

    public void testContactFromPhoneNumberReturnsEqualAndroidContactsFromEqualPhoneNumbers()
        throws InvalidNumberException {
        final Contact firstContact = mContactFactory.contactFromNumber(PHONE_1);
        final Contact secondContact = mContactFactory.contactFromNumber(PHONE_1);

        contactEqualityCheck(firstContact, secondContact);
    }

    public void testContactFromPhoneNumberReturnsEqualAndroidContactsFromReformattedPhoneNumbers()
        throws InvalidNumberException {
        final String reformattedPhone1 = PhoneNumberUtils.stripSeparators(PHONE_1);

        // sanity check that the test is actually useful
        assertFalse(reformattedPhone1.equals(PHONE_1));

        final Contact firstContact = mContactFactory.contactFromNumber(PHONE_1);
        final Contact secondContact = mContactFactory.contactFromNumber(reformattedPhone1);

        contactEqualityCheck(firstContact, secondContact);
    }

    public void testContactFromPhoneNumberUpdatedContactsAreEqual()
        throws InvalidNumberException {
        final String phoneNumber = "+41 21 693 11 11";
        final String displayName = "dummy 4";

        Context context = getContext();
        final ContactFactory contactFactory = new ContactFactory(context);

        // contact not yet known
        Contact firstContact = contactFactory.contactFromNumber(phoneNumber);

        try {
            TestContactUtils.addContact(context, displayName, phoneNumber);

            // contact should be known now since it was just added
            Contact secondContact = contactFactory.contactFromNumber(phoneNumber);

            // AndroidContact and UnknownContacts are not equals
            contactNonEqualityCheck(firstContact, secondContact);

            // updated UnknownContact is equal to AndroidContact
            contactEqualityCheck(firstContact.updateInfo(context), secondContact);
        } catch (RemoteException e) {
            fail();
        } catch (OperationApplicationException e) {
            fail();
        } finally {
            TestContactUtils.removeContactByDisplayName(context, displayName);
        }
    }

    public void testContactFromLookupKeyNullKey() throws ContactLookupException {
        try {
            mContactFactory.contactFromLookupKey(null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {
            // everything ok
        }
    }

    public void testContactFromLookupKeyEmptyKey() throws ContactLookupException {
        try {
            mContactFactory.contactFromLookupKey("");
            fail("expected to throw AssertionError");
        } catch (Throwable e) {
            // this hack is needed since checkstyle doesn't like catching AssertionError
            if (!(e instanceof AssertionError)) {
                fail("expected AssertionError but threw: " + e.getClass());
            }
        }
    }

    public void testContactFromLookupKeyInvalidKey() {
        try {
            mContactFactory.contactFromLookupKey("invalidKey");
            fail("expected to throw ContactLookupException");
        } catch (ContactLookupException e) {
            // everything ok
        }
    }

    public void testContactFromLookupKeyValidKey() throws InvalidNumberException, ContactLookupException {
        final Contact contactFromNumber = mContactFactory.contactFromNumber(PHONE_1);
        final String lookupKey = lookupKeyFromPhoneNumber(PHONE_1);
        final Contact contactFromLookupKey = mContactFactory.contactFromLookupKey(lookupKey);

        contactEqualityCheck(contactFromNumber, contactFromLookupKey);
    }

    public void testUnknownContactHasNoFingerPrint() throws InvalidNumberException {
        final Contact unknown = mContactFactory.contactFromNumber("+41 21 693 11 11");

        assertFalse(unknown.hasFingerprint());

        try {
            unknown.getFingerprint();
            fail("expected to throw NoFingerprintException");
        } catch (NoFingerprintException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionNullLookupKey() throws FingerprintInsertionException {
        try {
            mContactFactory.insertFingerprintForLookupKey(null, DUMMY_FINGERPRINT_0);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionNullFingerprint() throws FingerprintInsertionException {
        try {
            final String lookupKey = lookupKeyFromPhoneNumber(PHONE_1);
            mContactFactory.insertFingerprintForLookupKey(lookupKey, null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionInvalidFingerprint() throws FingerprintInsertionException {
        try {
            final String lookupKey = lookupKeyFromPhoneNumber(PHONE_1);
            mContactFactory.insertFingerprintForLookupKey(lookupKey, "invalid fingerprint");
            fail("expected to throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionInvalidLookupKey() {
        try {
            mContactFactory.insertFingerprintForLookupKey("invalid lookup key", DUMMY_FINGERPRINT_0);
            fail("expected to throw FingerprintInsertionException");
        } catch (FingerprintInsertionException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionSingleInsertion()
        throws FingerprintInsertionException, ContactLookupException, NoFingerprintException {

        final String lookupKey = lookupKeyFromPhoneNumber(PHONE_1);
        final Contact knownContact = mContactFactory.contactFromLookupKey(lookupKey);

        mContactFactory.insertFingerprintForLookupKey(lookupKey, DUMMY_FINGERPRINT_0);

        assertFalse(knownContact.hasFingerprint());
        try {
            knownContact.getFingerprint();
            fail("expected to throw NoFingerException");
        } catch (NoFingerprintException e) {
            // continue
        }

        final Contact updatedContact = knownContact.updateInfo(mContext);

        assertTrue(updatedContact.hasFingerprint());
        assertEquals(DUMMY_FINGERPRINT_0, updatedContact.getFingerprint());
    }

    public void testFingerprintInsertionDoubleInsertion()
        throws FingerprintInsertionException, ContactLookupException, NoFingerprintException {

        final String lookupKey = lookupKeyFromPhoneNumber(PHONE_1);
        final Contact originalContact = mContactFactory.contactFromLookupKey(lookupKey);

        mContactFactory.insertFingerprintForLookupKey(lookupKey, DUMMY_FINGERPRINT_0);
        mContactFactory.insertFingerprintForLookupKey(lookupKey, DUMMY_FINGERPRINT_1);

        final Contact updatedContact = originalContact.updateInfo(mContext);

        assertTrue(updatedContact.hasFingerprint());
        assertEquals(DUMMY_FINGERPRINT_1, updatedContact.getFingerprint());
    }

    public void testFingerprintInsertionNullPhoneNumber()
        throws FingerprintInsertionException {

        try {
            mContactFactory.insertFingerprintForPhoneNumber(null, DUMMY_FINGERPRINT_0);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionInvalidPhoneNumber() throws FingerprintInsertionException {

        try {
            mContactFactory.insertFingerprintForPhoneNumber("invalid phone number", DUMMY_FINGERPRINT_0);
            fail("expected to throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionValidPhoneNumberNullFingerprint()
        throws FingerprintInsertionException {

        try {
            mContactFactory.insertFingerprintForPhoneNumber(PHONE_1, null);
            fail("expected to throw NullArgumentException");
        } catch (NullArgumentException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionValidPhoneNumberInvalidFingerprint()
        throws FingerprintInsertionException {

        try {
            mContactFactory.insertFingerprintForPhoneNumber(PHONE_1, "invalid fingerprint");
            fail("expected to throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // everything ok
        }
    }

    public void testFingerprintInsertionValidPhoneNumberValidFingerprint()
        throws InvalidNumberException, FingerprintInsertionException, NoFingerprintException {

        final Contact contact = mContactFactory.contactFromNumber(PHONE_1);

        mContactFactory.insertFingerprintForPhoneNumber(PHONE_1, DUMMY_FINGERPRINT_0);

        assertFalse(contact.hasFingerprint());
        try {
            contact.getFingerprint();
            fail("expected to throw NoFingerprintException");
        } catch (NoFingerprintException e) {
            // continue
        }

        final Contact updated = contact.updateInfo(mContext);

        assertTrue(updated.hasFingerprint());
        assertEquals(DUMMY_FINGERPRINT_0, updated.getFingerprint());
    }

    private static void contactEqualityCheck(final Contact c1, final Contact c2) {
        assertTrue(c1.equals(c2));
        assertTrue(c2.equals(c1));
        assertTrue(c1.hashCode() == c2.hashCode());
    }

    private static void contactNonEqualityCheck(final Contact c1, final Contact c2) {
        assertFalse(c1.equals(c2));
        assertFalse(c2.equals(c1));
    }

    /*
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

}
