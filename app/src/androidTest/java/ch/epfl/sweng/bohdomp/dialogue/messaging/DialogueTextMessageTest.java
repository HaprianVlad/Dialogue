package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.content.Context;
import android.os.Parcel;

import org.mockito.Mockito;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.DialogueMessageId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;


/**
 * Test class for dialogue text messages
 */
public class DialogueTextMessageTest extends MockTestCase {
    private final String mText = "Hello world!";
    private final DialogueMessage.MessageStatus mStatus = DialogueMessage.MessageStatus.INCOMING;
    private Contact mContact;
    private DialogueTextMessage mMessage;

    protected void setUp() throws Exception {
        super.setUp();
        this.mContact = Mockito.mock(Contact.class);
        this.mMessage = new DialogueTextMessage(mContact, mText, mStatus);
    }

    public void testParcelRoundTrip() throws InvalidNumberException {
        Context context = getInstrumentation().getTargetContext().getApplicationContext();

        ContactFactory contactFactory;
        contactFactory = new ContactFactory(context);

        Contact contact = contactFactory.contactFromNumber("+41 21 693 11 11");

        DialogueMessage message = new DialogueTextMessage(contact, mText, mStatus);

        Parcel parcel = Parcel.obtain();
        message.writeToParcel(parcel, 0);

        parcel.setDataPosition(0); // reset parcel for reading

        DialogueMessage messageFromParcel = DialogueTextMessage.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertNotNull(messageFromParcel);

        assertEquals(message.getContact(), messageFromParcel.getContact());
        assertEquals(message.getBody().getMessageBody(), messageFromParcel.getBody().getMessageBody());
        assertEquals(message.getMessageStatus(), messageFromParcel.getMessageStatus());
        assertEquals(message.getIsDataMessage(), messageFromParcel.getIsDataMessage());
        assertEquals(message.getIsReadStatus(), messageFromParcel.getIsReadStatus());
        assertEquals(message.getMessageId(), messageFromParcel.getMessageId());
        assertEquals(message.getMessageTimeStamp(), messageFromParcel.getMessageTimeStamp());
    }

    public void testNullContactArgument() {
        try {
            this.mMessage = new DialogueTextMessage(null, mText, mStatus);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //success
        }
    }

    public void testNullTextArgument() {
        try {
            this.mMessage = new DialogueTextMessage(mContact, null, mStatus);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //success
        }
    }

    public void testNullStatusArgument() {
        try {
            this.mMessage = new DialogueTextMessage(mContact, mText, null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //success
        }
    }

    public void testGetContact() {
        //Contact is immutable
        assertTrue(mMessage.getContact() != null);
        assertEquals(mContact, mMessage.getContact());
    }

    public void testGetMessageTimeStamp() {
        assertTrue(mMessage.getMessageTimeStamp() != null);
        assertTrue(mMessage.getMessageTimeStamp().getTime() <= System.currentTimeMillis());

    }

    public void testGetMessageStatus() {
        assertTrue(mMessage.getMessageStatus() != null);
        assertEquals(mStatus, mMessage.getMessageStatus());

    }


    public void testGetMessageId() {
        DialogueMessageId afterId = IdManager.getInstance().newDialogueMessageId();

        assertTrue(mMessage.getMessageId() != null);
        assertEquals(mMessage.getMessageId().getLong() + 1, afterId.getLong());
    }

    public void testSetMessageAsRead() {
        assertFalse(mMessage.getIsReadStatus());
        mMessage.setMessageAsRead();
        assertTrue(mMessage.getIsReadStatus());
    }

    public void testGetAllowedChannels() {
        assertFalse(mMessage.getIsDataMessage());
    }


}
