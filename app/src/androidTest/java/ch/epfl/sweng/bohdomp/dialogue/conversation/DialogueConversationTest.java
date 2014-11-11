package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.content.Context;

import org.mockito.Mockito;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageStatus;

/**
 * Created by BohDomp! on 08.11.14.
 */
public class DialogueConversationTest extends MockTestCase {

    private static final long MILLIS_IN_DAY = 86400000;

    private Context mContext;
    private SystemTimeProvider mTimeProvider;
    private ContactFactory mContactFactory;
    private List<Contact> mContacts;
    private Contact mContact;
    private DialogueConversation mConversation;
    private List<DialogueMessage> mMessages;
    private boolean mHasBeenCalled;

    public void setUp() throws Exception {
        super.setUp();

        mContext = getInstrumentation().getTargetContext();
        mTimeProvider = new SystemTimeProvider();
        mContactFactory = new ContactFactory(mContext);

        mContact = mContactFactory.contactFromNumber("0773207769");
        mContacts = new ArrayList<Contact>();
        mContacts.add(mContact);

        mConversation = new DialogueConversation(mContacts, mTimeProvider);
        mMessages = new ArrayList<DialogueMessage>();
        mHasBeenCalled = false;
    }

    public void testConstructorContactsNotNull() {

        try {
            mConversation = new DialogueConversation(null, mTimeProvider);
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
        mContact = mContactFactory.contactFromNumber("0888431243");
        mConversation.addContact(mContact);
        mContacts.add(mContact);

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

    public void testRemoveContact() {
        mConversation.removeContact(mContact);
        mContacts.remove(mContact);

        assertEquals(mContacts, mConversation.getContacts());
    }

    public void testRemoveNonAddedContact() {
        mContact = mContactFactory.contactFromNumber("0887341234");

        // this should do nothing and throw no runtime exception!
        mConversation.removeContact(mContact);
    }

    public void testAddNullMessage() {

        try {
            mConversation.addMessage(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testAddOutgoingMessage() {
        DialogueMessage message = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.OUTGOING);
        mConversation.addMessage(message);
        mMessages.add(message);

        assertEquals(mMessages, mConversation.getMessages());
    }

    public void testAddIncomingMessage() {
        DialogueMessage message = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.INCOMING);
        mConversation.addMessage(message);
        mMessages.add(message);

        assertTrue(mConversation.hasUnread());
        assertEquals(mMessages, mConversation.getMessages());
    }

    public void testSetAllMessagesAsRead() {
        DialogueMessage message = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.INCOMING);
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
            public void onConversationChanged(ConversationId conversation) {
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
            public void onConversationChanged(ConversationId conversation) {
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
            public void onConversationChanged(ConversationId conversation) {
                mHasBeenCalled = true;
            }
        };

        // this should do nothing and throw no runtime exception!
        mConversation.removeListener(listener);
    }

    public void testLastActivitySameDay() {
        Timestamp lastActivity = mConversation.getLastActivityTime();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        String expectedDisplay = onlyHoursAndMin.format(lastActivity);

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityYesterday() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        Mockito.doReturn(mockedCurrentTimeMills(MILLIS_IN_DAY)).when(mTimeProvider).currentTimeMillis();

        String expectedDisplay = mContext.getString(R.string.yesterday);

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityTwoDaysAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        Mockito.doReturn(mockedCurrentTimeMills(2*MILLIS_IN_DAY)).when(mTimeProvider).currentTimeMillis();

        String expectedDisplay = mContext.getString(R.string.two_days_ago);

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityOneYearAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        Timestamp lastActivity = mConversation.getLastActivityTime();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        String expectedDisplay = onlyHoursAndMin.format(lastActivity);

        Mockito.doReturn(mockedCurrentTimeMills(366*MILLIS_IN_DAY)).when(mTimeProvider).currentTimeMillis();

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityOneMonthAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        Timestamp lastActivity = mConversation.getLastActivityTime();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        String expectedDisplay = onlyHoursAndMin.format(lastActivity);

        Mockito.doReturn(mockedCurrentTimeMills(366*MILLIS_IN_DAY)).when(mTimeProvider).currentTimeMillis();

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    private long mockedCurrentTimeMills(long bias) {
        return System.currentTimeMillis() + bias;
    }
}


