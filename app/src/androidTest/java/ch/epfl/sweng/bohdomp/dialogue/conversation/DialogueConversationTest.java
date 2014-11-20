package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.content.Context;
import android.os.Parcel;

import org.mockito.Mockito;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
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
    private static final int NB_YEAR_DAY = 366;
    private static final int NB_MONTH_DAY = 31;
    private static final int FOUR = 4;
    private static final long MAGIC_MONDAY = 1415628854000L;

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

    public void testConstructorNoContacts() {
        try {
            mConversation = new DialogueConversation(new ArrayList<Contact>(), mTimeProvider);
            fail("Should detect an empty contact list");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    public void testConstructorNullContact() {
        List<Contact> contactsWithNull = new ArrayList<Contact>();
        contactsWithNull.add(null);

        try {
            mConversation = new DialogueConversation(contactsWithNull, mTimeProvider);
            fail("Should detect a null contact");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    public void testConstructorNullSystemTimeProvider() {
        try {
            mConversation = new DialogueConversation(mContacts, null);
            fail("Should detect a null contact");
        } catch (IllegalArgumentException e) {
            // OK
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

    public void testAddContact() throws InvalidNumberException {

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

    public void testRemoveNonAddedContact() throws InvalidNumberException {

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

        assertEquals(mMessages.size(), mConversation.getMessageCount());
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

    public void testLastActivityNullContext() {
        try {
            mConversation.getLastConversationActivityString(null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // alles gut! :)
        }
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
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();
        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        Mockito.doReturn(timeSinceMagicMonday(1)).when(mTimeProvider).currentTimeMillis();

        String expectedDisplay = mContext.getString(R.string.yesterday);

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityTwoDaysAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();
        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        Mockito.doReturn(timeSinceMagicMonday(2)).when(mTimeProvider).currentTimeMillis();

        String expectedDisplay = mContext.getString(R.string.two_days_ago);

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityOneYearAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();
        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        Timestamp lastActivity = mConversation.getLastActivityTime();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("MM/yy", Locale.ENGLISH);
        String expectedDisplay = onlyHoursAndMin.format(lastActivity);

        Mockito.doReturn(timeSinceMagicMonday(NB_YEAR_DAY)).when(mTimeProvider).currentTimeMillis();

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityOneMonthAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();
        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        Timestamp lastActivity = mConversation.getLastActivityTime();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("dd.MM", Locale.ENGLISH);
        String expectedDisplay = onlyHoursAndMin.format(lastActivity);

        Mockito.doReturn(timeSinceMagicMonday(NB_MONTH_DAY)).when(mTimeProvider).currentTimeMillis();

        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityEarlierThisWeek() throws ParseException {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(1)).when(mTimeProvider).currentTimeMillis();

        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        String expectedDisplay = mContext.getResources().getStringArray(R.array.days_of_week)[1];

        Mockito.doReturn(timeSinceMagicMonday(FOUR)).when(mTimeProvider).currentTimeMillis();
        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    private long timeSinceMagicMonday(int nbDaysToAdd) {

        return MAGIC_MONDAY + nbDaysToAdd * MILLIS_IN_DAY;
    }

    public void testWriteToNullParcel() {
        try {
            mConversation.writeToParcel(null, -1);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testCreateFromNullParcel() {
        try {
            DialogueConversation.CREATOR.createFromParcel(null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testParcelability() {

        mTimeProvider = new SystemTimeProvider();

        mContacts = new ArrayList<Contact>();
        mContacts.add(mContact);

        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        DialogueMessage message = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.OUTGOING);
        mConversation.addMessage(message);

        Parcel parcel = Parcel.obtain();

        mConversation.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Conversation conversationFromParcel;

        conversationFromParcel = DialogueConversation.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertNotNull(conversationFromParcel);
        assertFalse("Conversations are not equals",
                mConversation == conversationFromParcel);
        assertEquals("Message count are not equals",
                mConversation.getMessageCount(), conversationFromParcel.getMessageCount());
        assertEquals("Id are not equals",
                mConversation.getId(), conversationFromParcel.getId());
        assertEquals("Contact list does not contain the same things",
                mConversation.getContacts().hashCode(), conversationFromParcel.getContacts().hashCode());
        assertEquals(mConversation.getLastActivityTime().getTime(),
                conversationFromParcel.getLastActivityTime().getTime());

        mMessages = mConversation.getMessages();
        List<DialogueMessage> parcelMessages = conversationFromParcel.getMessages();

        for (int i = 0; i < mMessages.size(); i++) {
            assertEquals("Message list does not contain the same message",
                    mMessages.get(0).getId(), parcelMessages.get(0).getId());
        }

        assertEquals(mConversation.hasUnread(), conversationFromParcel.hasUnread());
    }

    public void testParcelabilityWithUnread() {

        mTimeProvider = new SystemTimeProvider();

        mContacts = new ArrayList<Contact>();
        mContacts.add(mContact);

        mConversation = new DialogueConversation(mContacts, mTimeProvider);

        DialogueMessage message = new DialogueTextMessage(mContact, "Test message 1", MessageStatus.INCOMING);
        mConversation.addMessage(message);

        Parcel parcel = Parcel.obtain();
        mConversation.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        Conversation conversationFromParcel;

        conversationFromParcel = DialogueConversation.CREATOR.createFromParcel(parcel);
        parcel.recycle();

        assertEquals(mConversation.hasUnread(), conversationFromParcel.hasUnread());
    }

    public void testDescribeContents() {

        assertEquals(0, mConversation.describeContents());
    }

    public void testNewArray() {
        DialogueConversation[] expectedNewArray = new DialogueConversation[FOUR];

        DialogueConversation[] foundNewArray;
        foundNewArray = DialogueConversation.CREATOR.newArray(FOUR);

        assertEquals(expectedNewArray.length, foundNewArray.length);
    }
}


