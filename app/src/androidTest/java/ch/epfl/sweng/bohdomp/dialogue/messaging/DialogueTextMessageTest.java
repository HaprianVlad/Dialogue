package ch.epfl.sweng.bohdomp.dialogue.messaging;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.DialogueMessageId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;

import org.mockito.Mockito;


/**
 * Test class for dialogue text messages
 */
public class DialogueTextMessageTest extends MockTestCase {


    private final String text = "Hello world!";
    private final DialogueMessage.MessageStatus status = DialogueMessage.MessageStatus.INCOMING;
    private Contact contact;
    private DialogueTextMessage message;

    protected void setUp() throws Exception {
        super.setUp();
        this.contact = Mockito.mock(Contact.class);
        this.message = new DialogueTextMessage(contact, text, status);

    }

    public void testNullContactArgument() {
        try {
            this.message = new DialogueTextMessage(null, text, status);
            fail("Exception should have been trown");
        } catch (NullArgumentException e) {
           //success
        }
    }

    public void testNullTextArgument() {
        try {
            this.message = new DialogueTextMessage(contact, null, status);
            fail("Exception should have been trown");
        } catch (NullArgumentException e) {
            //success
        }
    }

    public void testNullStatusArgument() {
        try {
            this.message = new DialogueTextMessage(contact, text, null);
            fail("Exception should have been trown");
        } catch (NullArgumentException e) {
            //success
        }
    }

    public void  testGetContact() {
        //Contact is immutable
        assertTrue(message.getContact() != null);
        assertEquals(contact, message.getContact());
    }

    public void testGetMessageTimeStamp() {
        assertTrue(message.getMessageTimeStamp() != null);
        assertTrue(message.getMessageTimeStamp().getTime() <= System.currentTimeMillis());

    }

    public void testGetMessageStatus() {
        assertTrue(message.getMessageStatus() != null);
        assertEquals(status, message.getMessageStatus());

    }


    public void testGetMessageId() {
        DialogueMessageId afterId = IdManager.getInstance().newDialogueMessageId();

        assertTrue(message.getMessageId() != null);
        assertEquals(message.getMessageId().getLong()+1, afterId.getLong());
    }

    public void testSetMessageAsRead() {
        assertFalse(message.getIsReadStatus());
        message.setMessageAsRead();
        assertTrue(message.getIsReadStatus());
    }

    public void testGetAllowedChannels() {
        assertFalse(message.getIsDataMessage());
    }


}
