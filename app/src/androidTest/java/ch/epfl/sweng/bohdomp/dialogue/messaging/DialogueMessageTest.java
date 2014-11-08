package ch.epfl.sweng.bohdomp.dialogue.messaging;



import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;

import org.mockito.Mockito;



/**
 * Test class for dialogue messages
 */
public class DialogueMessageTest extends MockTestCase {

    private Contact contact;

    protected void setUp() throws Exception {
        super.setUp();
        this.contact = Mockito.mock(Contact.class);

    }


    /*
    public void  testGetContact() {
        assertTrue(contact!=null);
    }

    public void testGetMessageTimeStamp() {
        assertTrue(true);
    }

    public void testGetMessageStatus() {
        assertTrue(true);

    }


    public void testGetMessageId() {
       assertTrue(true);
    }

    public void testSetMessageAsRead() {
        assertTrue(true);
    }

    public void testGetAllowedChannels() {
        assertTrue(true);
    }
*/

}
