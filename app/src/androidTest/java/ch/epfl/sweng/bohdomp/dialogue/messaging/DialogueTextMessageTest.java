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

    private static final String TEXT = "Hello world!";
    private static final int ARRAY_SIZE = 6;

    private final DialogueMessage.MessageStatus mStatus = DialogueMessage.MessageStatus.INCOMING;
    private Contact mContact;
    private DialogueTextMessage mMessage;

    protected void setUp() throws Exception {
        super.setUp();
        this.mContact = Mockito.mock(Contact.class);
        this.mMessage = new DialogueTextMessage(mContact, null, null, TEXT, mStatus);
    }

    public void testParcelRoundTrip() throws InvalidNumberException {
        Context context = getInstrumentation().getTargetContext().getApplicationContext();

        ContactFactory contactFactory;
        contactFactory = new ContactFactory(context);

        Contact contact = contactFactory.contactFromNumber("+41 21 693 11 11");

        DialogueMessage message = new DialogueTextMessage(contact, null, null, TEXT, mStatus);

        Parcel parcel = Parcel.obtain();
        message.writeToParcel(parcel, 0);

        parcel.setDataPosition(0); // reset parcel for reading

        DialogueMessage messageFromParcel = DialogueTextMessage.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertNotNull(messageFromParcel);

        assertEquals(message.getContact(), messageFromParcel.getContact());
        assertEquals(message.getBody().getMessageBody(), messageFromParcel.getBody().getMessageBody());
        assertEquals(message.getStatus(), messageFromParcel.getStatus());
        assertEquals(message.getIsDataMessage(), messageFromParcel.getIsDataMessage());
        assertEquals(message.getIsReadStatus(), messageFromParcel.getIsReadStatus());
        assertEquals(message.getId(), messageFromParcel.getId());
        assertEquals(message.getTimeStamp(), messageFromParcel.getTimeStamp());
    }

    public void testNullContactArgument() {
        try {
            this.mMessage = new DialogueTextMessage(null, null, null, TEXT, mStatus);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //success
        }
    }

    public void testNullTextArgument() {
        try {
            this.mMessage = new DialogueTextMessage(mContact, null, null, null, mStatus);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
            //success
        }
    }

    public void testNullStatusArgument() {
        try {
            this.mMessage = new DialogueTextMessage(mContact, null, null, TEXT, null);
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
        assertTrue(mMessage.getTimeStamp() != null);
        assertTrue(mMessage.getTimeStamp().getTime() <= System.currentTimeMillis());

    }

    public void testGetMessageStatus() {
        assertTrue(mMessage.getStatus() != null);
        assertEquals(mStatus, mMessage.getStatus());

    }


    public void testGetMessageId() {
        DialogueMessageId afterId = IdManager.getInstance().newDialogueMessageId();

        assertTrue(mMessage.getId() != null);
        assertEquals(mMessage.getId().getLong() + 1, afterId.getLong());
    }

    public void testSetMessageAsRead() {
        assertFalse(mMessage.getIsReadStatus());
        mMessage.setAsRead();
        assertTrue(mMessage.getIsReadStatus());
    }

    public void testGetAllowedChannels() {
        assertFalse(mMessage.getIsDataMessage());
    }

    public void testNewArray() {
        DialogueTextMessage[] newArray = DialogueTextMessage.CREATOR.newArray(ARRAY_SIZE);

        assertEquals(ARRAY_SIZE, newArray.length);
    }

}
