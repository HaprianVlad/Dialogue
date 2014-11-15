package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.app.Application;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.telephony.PhoneNumberUtils;
import android.test.ApplicationTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;

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

    public ContactFactoryTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        Context context = getContext();

        addContact(context, DISPLAY_NAME_1, PHONE_1);
        addContact(context, DISPLAY_NAME_2, PHONE_2);
        addContact(context, DISPLAY_NAME_3, PHONE_3);

        this.mContactFactory = new ContactFactory(context);
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
        for (final String name : ALL_NAMES) {
            removeContactByDisplayName(getContext(), name);
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

        Set<String> phoneNumbersInContact = contact.getPhoneNumbers();
        // phoneNumbersInContact should contain exactly one phone number,
        // which is the one we passed in
        assertEquals(1, phoneNumbersInContact.size());
        assertTrue(phoneNumbersInContact.contains(validPhoneNumber));

        // we assume that contact is reachable via SMS when there is a phone number
        Set<Contact.ChannelType> contactAvailableChannels = contact.availableChannels();
        assertEquals(1, contactAvailableChannels.size());
        assertTrue(contactAvailableChannels.contains(Contact.ChannelType.SMS));
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

    public void testKnownContactsContainsAddedContacts() throws Exception {
        List<Contact> contacts = mContactFactory.knownContacts();
        Set<String> displayNames = new HashSet<String>();
        for (final Contact c : contacts) {
            displayNames.add(c.getDisplayName());
        }

        for (final String name : ALL_NAMES) {
            assertTrue(displayNames.contains(name));
        }
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
            addContact(context, displayName, phoneNumber);

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
            removeContactByDisplayName(context, displayName);
        }
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

    private static void addContact(Context context, final String displayName, final String phoneNumber)
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
    private static void removeContactByDisplayName(Context context, final String displayName) {
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
