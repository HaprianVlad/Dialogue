package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.content.Intent;
import android.telephony.SmsManager;
import android.test.ServiceTestCase;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;

/**
 * Class creating a Tester for a Sms Sender Service
 */
public final class SmsSenderServiceTest extends ServiceTestCase<SmsSenderService> {
    private Intent mIntent;
    private final String phoneNumber = "0762677108";
    private final String body = "Hello";
    private SmsManager mSmsManager;

    public SmsSenderServiceTest() {
        super(SmsSenderService.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        ContactFactory contactFactory = new ContactFactory(getSystemContext());
        Contact contact = contactFactory.contactFromNumber(phoneNumber);

        DialogueMessage message = new DialogueTextMessage(contact, body, DialogueMessage.MessageStatus.OUTGOING);

        mIntent = new Intent();
        mIntent.setAction(SmsSenderService.ACTION_SEND_SMS);
        mIntent.putExtra(DialogueMessage.MESSAGE, message);

        //mSmsManager = Mockito.mock(SmsManager.class);
    }

    public void testServiceStartedCorrectlyViaIntent() throws Exception {
        assertTrue(getService() == null);

        startService(mIntent);

        assertTrue(getService() != null);
    }

    public void test() throws Exception {
        setupService();

        assertTrue(getService() != null);

        /*getService().onHandleIntent(mIntent);
         //Can not mock Sms Manager
        PendingIntent pendingIntent = Mockito.mock(PendingIntent.class);
        Mockito.verify(mSmsManager).sendTextMessage(Mockito.eq(phoneNumber),
                Mockito.anyString(), Mockito.eq(body), pendingIntent, pendingIntent);*/
    }

}
