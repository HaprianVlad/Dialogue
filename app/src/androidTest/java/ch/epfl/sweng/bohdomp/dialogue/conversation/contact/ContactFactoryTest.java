package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.app.Application;
import android.test.ApplicationTestCase;

import java.util.Set;

/**
 *  junit test class for ContactFactory class
 */
public class ContactFactoryTest extends ApplicationTestCase<Application> {

    private ContactFactory mContactFactory;

    public ContactFactoryTest() {
        super(Application.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        createApplication();
        this.mContactFactory = new ContactFactory(getContext());
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

    public void testKnownContacts() throws Exception {
        //TODO use mocking to try unit testing reading the contact list
        assertTrue(true);
    }
}