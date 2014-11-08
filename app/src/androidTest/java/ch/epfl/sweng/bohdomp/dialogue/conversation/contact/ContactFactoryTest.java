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
import android.test.ApplicationTestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *  junit test class for ContactFactory class
 *
 *  /!\ don't run on your actual device will delete all contacts
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
    private static final Set<String> NAME_SET = new HashSet<String>(Arrays.asList(ALL_NAMES));

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
        removeContacts(getContext());
    }

    public void testContactFromPhoneNumberWhenNumberIsNotKnownButValid() {
        String validPhoneNumber = "+41 21 693 11 11"; //EPFL front desk .-P

        Contact contact = mContactFactory.contactFromNumber(validPhoneNumber);

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
            fail("should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertTrue(true);
        }
    }

    public void testContactFromPhoneNumberWhenNumberIsKnownAndIdentical() {
        Contact contact = mContactFactory.contactFromNumber(PHONE_1);
        assertTrue(contact.getDisplayName().equals(DISPLAY_NAME_1));
    }

    public void testContactFromPhoneNumberWhenNumberIsKnownAndDifferentFormat() {
        Contact contact = mContactFactory.contactFromNumber(PHONE_1.replaceAll("\\s", ""));
        assertTrue(contact.getDisplayName().equals(DISPLAY_NAME_1));
    }

    public void testKnownContacts() throws Exception {
        List<Contact> contacts = mContactFactory.knownContacts();
        assertTrue(contacts.size() == NAME_SET.size());
        for (Contact c : contacts) {
            assertTrue(NAME_SET.contains(c.getDisplayName()));
        }
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

    /**
     * code taken from
     * http://stackoverflow.com/
     *  questions/6966523/how-to-delete-all-contacts-in-contact-list-on-android-mobile-programatically
     * @param context
     */
    private static void removeContacts(Context context) {
        ContentResolver resolver = context.getContentResolver();

        Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String lookupKey = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
            Uri deletionUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI, lookupKey);
            resolver.delete(deletionUri, null, null);
        }
    }
}
