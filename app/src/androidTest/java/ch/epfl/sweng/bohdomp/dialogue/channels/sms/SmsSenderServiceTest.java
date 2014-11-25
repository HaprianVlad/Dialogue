package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.test.ServiceTestCase;
import android.util.Log;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;

/**
 * Class creating a Tester for a Sms Sender Service
 */
public final class SmsSenderServiceTest extends ServiceTestCase<SmsSenderService> {

    private final static String phoneNumber = "888888";

    private DialogueMessage message;
    private Intent intent;
    private Intent badIntent;
    private boolean sent;

    public SmsSenderServiceTest() {
        super(SmsSenderService.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        ContactFactory contactFactory = new ContactFactory(getSystemContext());

        Contact contact = contactFactory.contactFromNumber(phoneNumber);

        String body = "Hello";
        message = new DialogueTextMessage(contact, body, DialogueMessage.MessageStatus.OUTGOING);


        intent = new Intent();
        intent.setAction(SmsSenderService.ACTION_SEND_SMS);
        intent.putExtra(DialogueMessage.MESSAGE, message);

        badIntent = new Intent();
        badIntent.setAction("");
        badIntent.putExtra(DialogueMessage.MESSAGE, message);

        sent = false;
    }

    public void testServiceStartedCorrectlyViaIntent() throws Exception {
        assertTrue(getService() == null);

        startService(intent);

        assertTrue(getService() != null);
    }

    public void testOnHandleIntent() throws Exception {
        setupService();
        createSmsCursor();

        assertTrue(getService() != null);


       // int initialNumber = getSmsMessageCount();

        getService().onHandleIntent(intent);

        assertTrue(sent);
        //int afterNumber = getSmsMessageCount();

        //assertEquals(initialNumber, afterNumber-1);
        //assertEquals(message.getBody().getMessageBody(), readLastSms());
    }


    public void testOnHandleBadIntent() throws Exception {
        setupService();

        assertTrue(getService() != null);

       // int initialNumber = getSmsMessageCount();

        getService().onHandleIntent(badIntent);

       // int afterNumber = getSmsMessageCount();

        assertFalse(sent);
        //assertEquals(initialNumber, afterNumber);
    }


    private String readLastSms() {
        Cursor c = createSmsCursor();

        return c.getString(c.getColumnIndex("body"));
    }

    private int getSmsMessageCount() {
        Cursor c = createSmsCursor();

        return  c.getCount();
    }

    private Cursor createSmsCursor() {
        Uri sentURI = Uri.parse("content://sms/sent");

        String[] reqCols = new String[] { "_id", phoneNumber, "body" };

        String selection = "address='888888'";

        ContentResolver cr = getContext().getContentResolver();

        cr.notifyChange(sentURI, new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                Log.d("asas","chanfed");
                sent = true;
            }
        });

        return cr.query(sentURI, reqCols, selection, null, null);
    }
}
