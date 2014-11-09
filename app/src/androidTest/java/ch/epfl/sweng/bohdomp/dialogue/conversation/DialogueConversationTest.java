package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.UnknownContact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageStatus;

/**
 * Created by BohDomp! on 08.11.14.
 */
public class DialogueConversationTest extends AndroidTestCase {
    private List<Contact> mContacts;
    private Contact mContact;
    private DialogueConversation mConversation;
    private List<DialogueMessage> mMessages;
    private boolean mHasBeenCalled;

    public void setUp() throws Exception {
        super.setUp();

        mContact = new UnknownContact("0773207769");
        mContacts = new ArrayList<Contact>();
        mContacts.add(mContact);
        
        mConversation = new DialogueConversation(mContacts);
        mMessages = new ArrayList<DialogueMessage>();
        mHasBeenCalled = false;
    }

    public void testConstructorContactsNotNull() {

        try {
            mConversation = new DialogueConversation(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testGetId() {
        ConversationId nextId = IdManager.getInstance().newConversationId();

        assertEquals(nextId.getLong(), mConversation.getId().getLong() + 1);
    }

    public void testGetConversationName() {

        assertEquals(mContact.getDisplayName(), mConversation.getName());
    }

    public void testGetConversationContacts() {

        assertEquals(mContacts, mConversation.getContacts());
    }

    public void testGetConversationMessagesNonInit() {

        assertEquals(new ArrayList<DialogueMessage>(), mConversation.getMessages());
    }

    public void testGetConversationTimeStamp() {
        long afterConversationLastActivity = System.currentTimeMillis();

        assertTrue(afterConversationLastActivity >= mConversation.getLastActivityTime().getTime());
    }

    public void testGetConversationMsgCountNonInit() {

        assertEquals(0, mConversation.getMessageCount());
    }

    public void testGetConversationHasUnreadNonInit() {

        assertEquals(false, mConversation.hasUnread());
    }

    public void testAddNullContact() {

        try {
            mConversation.addContact(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testAddContact() {
        Contact contact = new UnknownContact("0888431243");
        mConversation.addContact(contact);
        mContacts.add(contact);

        assertEquals(mContacts, mConversation.getContacts());
    }

    public void testRemoveNullContact() {

        try {
            mConversation.removeContact(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testAddAndRemoveContact() {
        Contact contact = new UnknownContact("0888431243");
        mConversation.addContact(contact);
        mConversation.removeContact(contact);

        assertEquals(mContacts, mConversation.getContacts());
    }

    public void testRemoveNonExistentContact() {
        Contact contact = new UnknownContact("0887341234");

        // this should do nothing and throw no runtime exception!
        mConversation.removeContact(contact);
    }

    public void testAddNullMessage() {

        try {
            mConversation.addMessage(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testAddMessage() {
        DialogueMessage message = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.OUTGOING);
        mConversation.addMessage(message);
        mMessages.add(message);

        assertTrue(mConversation.hasUnread());
        assertEquals(mMessages, mConversation.getMessages());
    }

    public void testSetAllMessagesAsRead() {
        DialogueMessage message = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.OUTGOING);
        mConversation.addMessage(message);

        assertTrue(mConversation.hasUnread());

        mConversation.setAllMessagesAsRead();

        assertFalse(mConversation.hasUnread());
    }

    public void testAddNullListener() {

        try {
            mConversation.addListener(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testAddListener() {
        ConversationListener listener = new ConversationListener() {
            @Override
            public void onConversationChanged(Conversation conversation) {
                mHasBeenCalled = true;
            }
        };

        DialogueMessage message1 = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.OUTGOING);
        mConversation.addMessage(message1);

        assertFalse(mHasBeenCalled);

        mConversation.addListener(listener);

        DialogueMessage message2 = new DialogueTextMessage(mContact, "Test message 2", MessageStatus.OUTGOING);
        mConversation.addMessage(message2);

        assertTrue(mHasBeenCalled);
    }

    public void testRemoveNullListener() {

        try {
            mConversation.removeListener(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testAddAndRemoveListener() {
        ConversationListener listener = new ConversationListener() {
            @Override
            public void onConversationChanged(Conversation conversation) {
                mHasBeenCalled = true;
            }
        };

        mConversation.addListener(listener);
        mConversation.removeListener(listener);

        DialogueMessage message1 = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.OUTGOING);
        mConversation.addMessage(message1);

        assertFalse(mHasBeenCalled);

    }

    public void testRemoveNonAddedListener() {
        ConversationListener listener = new ConversationListener() {
            @Override
            public void onConversationChanged(Conversation conversation) {
                mHasBeenCalled = true;
            }
        };

        // this should do nothing and throw no runtime exception!
        mConversation.removeListener(listener);
    }
}


