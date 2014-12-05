package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.content.Intent;
import android.test.ServiceTestCase;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;

/**
 * Class creating a Tester for a Sms Sender Service
 */
public final class SmsSenderServiceTest extends ServiceTestCase<SmsSenderService> {
    private final static String PHONE_NUMBER = "000000000000000000";

    private final static String BODY = "Hello";
    private Intent mIntent;


    public SmsSenderServiceTest() {
        super(SmsSenderService.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        ContactFactory contactFactory = new ContactFactory(getSystemContext());
        Contact contact = contactFactory.contactFromNumber(PHONE_NUMBER);

        Contact.PhoneNumber number = contact.getPhoneNumbers().iterator().next();
        Contact.ChannelType channel = Contact.ChannelType.SMS;

        DialogueMessage message = new DialogueTextMessage(contact, channel, number,
                BODY, DialogueMessage.MessageDirection.OUTGOING);

        mIntent = new Intent();
        mIntent.setAction(SmsSenderService.ACTION_SEND_SMS);
        mIntent.putExtra(DialogueMessage.MESSAGE, message);

    }

    public void testServiceStartedCorrectlyViaIntent() throws Exception {
        assertNull(getService());

        startService(mIntent);

        assertNotNull(getService());
    }

    public void test() throws Exception {
        setupService();

        assertNotNull(getService());
    }

}
