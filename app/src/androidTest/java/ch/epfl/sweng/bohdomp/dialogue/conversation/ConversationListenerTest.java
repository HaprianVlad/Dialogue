package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;



/**
 * Class testing a conversation listener
 */
public final class ConversationListenerTest extends MockTestCase {


    private ContactFactory mContactFactory;
    private DialogueConversation mConversation;
    private List<DialogueMessage> mMessages;
    private boolean mHasBeenCalled;


    public void setUp() throws Exception {
        super.setUp();

        Context mContext = getInstrumentation().getTargetContext();
        mContactFactory = new ContactFactory(mContext);

        Contact mContact = mContactFactory.contactFromNumber("0762677108");
        List<Contact> mContacts = new ArrayList<Contact>();
        mContacts.add(mContact);

        mConversation = new DialogueConversation(mContacts);
        mMessages = new ArrayList<DialogueMessage>();

        String body = "Hello";

        mMessages.add(new DialogueTextMessage(mContact, null, null, body, DialogueMessage.MessageDirection.INCOMING));

        mHasBeenCalled = false;
    }


    public void testConversationListenerCalledWhenContactAdded() throws InvalidNumberException {
        mHasBeenCalled = false;

        ConversationListener listener = new ConversationListener() {
            @Override
            public void onConversationChanged(ConversationId id) {
                assertEquals(id, mConversation.getId());
                mHasBeenCalled = true;
            }
        };

        mConversation.addListener(listener);
        mConversation.addContact(mContactFactory.contactFromNumber("0040749475877"));
        assertTrue(mHasBeenCalled);

        mHasBeenCalled = false;

        mConversation.removeListener(listener);
        mConversation.addContact(mContactFactory.contactFromNumber("0040755785477"));
        assertFalse(mHasBeenCalled);

    }

    public void testConversationListenerCalledWhenContactRemoved() throws InvalidNumberException {
        mHasBeenCalled = false;

        ConversationListener listener = new ConversationListener() {
            @Override
            public void onConversationChanged(ConversationId id) {
                assertEquals(id, mConversation.getId());
                mHasBeenCalled = true;
            }
        };

        Contact contact1 = mContactFactory.contactFromNumber("0040749475877");
        Contact contact2 = mContactFactory.contactFromNumber("0040755785477");

        mConversation.addContact(contact1);
        mConversation.addContact(contact2);

        mConversation.addListener(listener);
        mConversation.removeContact(contact1);
        assertTrue(mHasBeenCalled);

        mHasBeenCalled = false;

        mConversation.removeListener(listener);
        mConversation.removeContact(contact2);
        assertFalse(mHasBeenCalled);


    }

    public void testConversationListenerCalledWhenMessageAdded() {
        mHasBeenCalled = false;

        ConversationListener listener = new ConversationListener() {
            @Override
            public void onConversationChanged(ConversationId id) {
                assertEquals(id, mConversation.getId());
                mHasBeenCalled = true;
            }
        };

        mConversation.addListener(listener);
        mConversation.addMessage(mMessages.get(0));
        assertTrue(mHasBeenCalled);

        mHasBeenCalled = false;

        mConversation.removeListener(listener);
        mConversation.addMessage(mMessages.get(0));
        assertFalse(mHasBeenCalled);

    }


    public void testConversationListenerCalledWhenSetAsRead() {
        mHasBeenCalled = false;

        ConversationListener listener = new ConversationListener() {
            @Override
            public void onConversationChanged(ConversationId id) {
                assertEquals(id, mConversation.getId());
                mHasBeenCalled = true;
            }
        };

        mConversation.addListener(listener);
        mConversation.setAllMessagesAsRead();
        assertTrue(mHasBeenCalled);

        mHasBeenCalled = false;

        mConversation.removeListener(listener);
        mConversation.setAllMessagesAsRead();
        assertFalse(mHasBeenCalled);

    }

}
