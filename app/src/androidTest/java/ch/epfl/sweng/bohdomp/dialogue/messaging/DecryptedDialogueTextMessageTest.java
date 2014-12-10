package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.os.Parcel;
import android.test.AndroidTestCase;

import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.PhoneNumber;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;

/**
 * Tests the DecryptedDialogueTextMessage class.
 */
public class DecryptedDialogueTextMessageTest extends AndroidTestCase {
    public void testParcelRoundTrip() throws InvalidNumberException {
        Contact contact = new ContactFactory(getContext()).contactFromNumber("1234");

        PhoneNumber phoneNumber = (PhoneNumber) contact.getPhoneNumbers(ChannelType.SMS).toArray()[0];

        DialogueMessage message = new DialogueTextMessage(contact, ChannelType.SMS, phoneNumber, "test",
                DialogueMessage.MessageDirection.OUTGOING);

        Parcel parcel = Parcel.obtain();
        message.writeToParcel(parcel, 0);

        parcel.setDataPosition(0); // reset parcel for reading

        DialogueMessage messageFromParcel = DecryptedDialogueTextMessage.CREATOR.createFromParcel(parcel);

        parcel.recycle();

        assertEquals(message.getPlainTextBody().getMessageBody(),
                messageFromParcel.getPlainTextBody().getMessageBody());

        assertEquals(message.getBody().getMessageBody(), messageFromParcel.getBody().getMessageBody());
    }

    public void testIsEncrypted() throws InvalidNumberException {
        Contact contact = new ContactFactory(getContext()).contactFromNumber("1234");

        PhoneNumber phoneNumber = (PhoneNumber) contact.getPhoneNumbers(ChannelType.SMS).toArray()[0];

        DialogueMessage message = new DecryptedDialogueTextMessage(contact, ChannelType.SMS, phoneNumber, "test",
                DialogueMessage.MessageDirection.OUTGOING);

        assertTrue(message.isEncrypted());
    }
}