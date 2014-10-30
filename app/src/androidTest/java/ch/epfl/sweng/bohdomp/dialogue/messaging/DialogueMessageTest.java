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


    public void  testGetContact() {

    }
    public void testGetBody() {

    }

    public void testGetIsReadStatus() {


    }

    public void testGetMessageTimeStamp() {

    }

    public void testGetMessageStatus() {


    }


    public void testGetMessageId() {

    }

    public void testSetMessageAsRead() {

    }




    public void testGetAllowedChannels() {

    }


}
