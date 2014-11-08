package ch.epfl.sweng.bohdomp.dialogue.messaging;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;

import org.mockito.Mockito;


/**
 * Test class for dialogue text messages
 */
public class DialogueTextMessageTest extends MockTestCase {


    private final String text = "Hello world!";
    private final DialogueMessage.MessageStatus status = DialogueMessage.MessageStatus.INCOMING;
    private final Contact.ChannelType channelType = Contact.ChannelType.SMS;
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
        assertTrue(message.getContact() != null);
    }

    public void testGetMessageTimeStamp() {
        assertTrue(message.getMessageTimeStamp() != null);
        assertTrue(message.getMessageTimeStamp().getTime() > 0);

    }

    public void testGetMessageStatus() {
        assertTrue(message.getMessageStatus() == status);

    }


    public void testGetMessageId() {
        assertTrue(message.getMessageId() != null);
    }

    public void testSetMessageAsRead() {
        assertFalse(message.getIsReadStatus());
    }

    public void testGetAllowedChannels() {

        assertFalse(message.getIsDataMessage());
    }


}
