package ch.epfl.sweng.bohdomp.dialogue.channels;


import android.content.Intent;
import android.test.ServiceTestCase;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;


/**
 * Class creating a Tester for a Dialogue Incoming Dispatcher
 */
public final class DialogueIncomingDispatcherTest extends ServiceTestCase<DialogueIncomingDispatcher> {

    private DialogueMessage message;
    private DialogueMessage messageOutgoing;
    private Conversation conversation;
    private Intent intent;

    public DialogueIncomingDispatcherTest() {
        super(DialogueIncomingDispatcher.class);

    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        ContactFactory contactFactory = new ContactFactory(getSystemContext());

        Contact contact = contactFactory.contactFromNumber("0762677108");
        conversation = DefaultDialogData.getInstance().createOrGetConversation(contact);

        String body = "Hello";
        message = new DialogueTextMessage(contact, body, DialogueMessage.MessageStatus.INCOMING);
        messageOutgoing = new DialogueTextMessage(contact, body, DialogueMessage.MessageStatus.OUTGOING);

        intent = new Intent();
        intent.setAction(DialogueIncomingDispatcher.ACTION_RECEIVE_MESSAGE);
        intent.putExtra(DialogueMessage.MESSAGE, message);

    }

    public void testReceiveNullContext() {
        try {
            DialogueIncomingDispatcher.receiveMessage(null, message);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //ok
        }
    }

    public void testReceiveNullMessage() {
        try {
            DialogueIncomingDispatcher.receiveMessage(mContext, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //ok
        }
    }

    public void testReceiveOutgoingMessage() {
        try {
            DialogueIncomingDispatcher.receiveMessage(mContext, messageOutgoing);
            fail("Exception should have been thrown");
        } catch (IllegalArgumentException e) {
            //ok
        }
    }

    public void testServiceStartedCorrectlyViaIntent() throws Exception {

        assertTrue(getService() == null);

        startService(intent);

        assertTrue(getService() != null);

    }

    public void testOnHandleIntent() throws Exception {
        setupService();

        assertTrue(getService() != null);

        getService().onHandleIntent(intent);

        List<DialogueMessage> messages = DefaultDialogData.getInstance().
                getConversation(conversation.getId()).getMessages();

        assertTrue(messages.size() > 0);

        DialogueMessage message1 = messages.get(messages.size() - 1);

        assertTrue(message1 != null);

        assertEquals(message.getId(), message1.getId());
        assertEquals(message.getContact(), message1.getContact());
        assertEquals(message.getBody(), message1.getBody());
        assertEquals(message.getIsDataMessage(), message1.getIsDataMessage());
        assertEquals(message.getIsReadStatus(), message1.getIsReadStatus());
        assertEquals(message.getStatus(), message1.getStatus());
        assertEquals(message.getTimeStamp(), message1.getTimeStamp());

    }

}
