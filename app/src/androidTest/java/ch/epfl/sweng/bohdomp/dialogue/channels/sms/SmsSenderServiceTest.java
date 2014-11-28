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
    private Intent intent;

    public SmsSenderServiceTest() {
        super(SmsSenderService.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        ContactFactory contactFactory = new ContactFactory(getSystemContext());
        String phoneNumber = "0762677108";
        Contact contact = contactFactory.contactFromNumber(phoneNumber);

        String body = "Hello";

        DialogueMessage message = new DialogueTextMessage(contact, body, DialogueMessage.MessageStatus.OUTGOING);

        intent = new Intent();
        intent.setAction(SmsSenderService.ACTION_SEND_SMS);
        intent.putExtra(DialogueMessage.MESSAGE, message);
    }

    public void testServiceStartedCorrectlyViaIntent() throws Exception {
        assertTrue(getService() == null);

        startService(intent);

        assertTrue(getService() != null);
    }

}
