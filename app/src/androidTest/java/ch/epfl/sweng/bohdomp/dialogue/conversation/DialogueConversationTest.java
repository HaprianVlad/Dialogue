package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.content.Context;
import android.content.Intent;
import android.os.Parcel;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.PhoneNumber;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageDirection;
/**
 * Created by BohDomp! on 08.11.14.
 */
public class DialogueConversationTest extends MockTestCase {
    private static final int MONDAY_INDEX = 0;
    private static final int THREE = 3;
    private static final int MAGIC_POSITIVE_INT = 42;
    private static final DateTime LATEST_ACTIVITY = new DateTime(2014, 9, 22, 9, 42, 42, 42);

    private Context mContext;
    private ContactFactory mContactFactory;
    private List<Contact> mContacts;
    private Contact mContact;
    private PhoneNumber mNumber;
    private ChannelType mChannel;
    private DialogueConversation mConversation;
    private List<DialogueMessage> mMessages;
    private boolean mHasBeenCalled;

    public void setUp() throws Exception {
        super.setUp();

        DateTimeUtils.setCurrentMillisFixed(LATEST_ACTIVITY.getMillis());

        mContext = getInstrumentation().getTargetContext();
        mContactFactory = new ContactFactory(mContext);

        mContact = mContactFactory.contactFromNumber("0773207769");
        mContacts = new ArrayList<Contact>();
        mContacts.add(mContact);


        mNumber = mContact.getPhoneNumbers().iterator().next();
        mChannel = ChannelType.SMS;

        mConversation = new DialogueConversation(mContacts);
        mMessages = new ArrayList<DialogueMessage>();

        mHasBeenCalled = false;
    }

    public void tearDown() throws Exception {
        super.tearDown();

        DateTimeUtils.setCurrentMillisSystem();
    }

    public void testConstructorContactsNotNull() {

        try {
            mConversation = new DialogueConversation(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testConstructorNoContacts() {
        try {
            mConversation = new DialogueConversation(new ArrayList<Contact>());
            fail("Should detect an empty contact list");
        } catch (IllegalArgumentException e) {
            // OK
        }
    }

    public void testConstructorNullContact() {
        List<Contact> contactsWithNull = new ArrayList<Contact>();
        contactsWithNull.add(null);

        try {
            mConversation = new DialogueConversation(contactsWithNull);
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
        DateTimeUtils.setCurrentMillisSystem();

        long afterConversationLastActivity = System.currentTimeMillis();
        assertTrue(afterConversationLastActivity >= mConversation.getLastActivityTime().getMillis());
    }

    public void testGetConversationMsgCountNonInit() {

        assertEquals(0, mConversation.getMessageCount());
    }

    public void testGetConversationHasUnreadNonInit() {

        assertEquals(false, mConversation.hasUnread());
    }

    public void testSetMessageStatusNullMessageValidStatus() {
        try {
            mConversation.setMessageStatus(null, DialogueMessage.MessageStatus.SENT);
            fail("Should throw NullArgumentException");
        } catch (NullArgumentException e) {
            // OK
        }
    }

    public void testSetMessageStatusValidMessageNullStatus() {
        DialogueMessage message = new DialogueTextMessage(mContact, null, null, "body", MessageDirection.OUTGOING);

        mConversation.addMessage(message);

        try {
            mConversation.setMessageStatus(message, null);
            fail("Should throw NullArgumentException");
        } catch (NullArgumentException e) {
            // OK
        }
    }

    public void testSetMessageStatusValidMessageValidStatus() {
        DialogueMessage message1 = new DialogueTextMessage(mContact, null, null, "body1", MessageDirection.OUTGOING);
        DialogueMessage message2 = new DialogueTextMessage(mContact, null, null, "body2", MessageDirection.OUTGOING);

        mConversation.addMessage(message1);
        mConversation.addMessage(message2);

        Intent intent = new Intent();
        intent.putExtra(DialogueMessage.MESSAGE, message2);
        DialogueMessage message2FromIntent = DialogueTextMessage.extractMessage(intent);

        try {
            mConversation.setMessageStatus(message2FromIntent, DialogueMessage.MessageStatus.SENT);
        } catch (NullArgumentException e) {
            fail("Should not throw NullArgumentException");
        }

        assertEquals(DialogueMessage.MessageStatus.SENT, mConversation.getMessages().get(1).getStatus());
        assertEquals(MessageDirection.OUTGOING, mConversation.getMessages().get(0).getDirection());
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

    public void testSetChannelNull() {
        try {
            mConversation.setChannel(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            //Ok
        }
    }

    public void testSetPhoneNumberNull() {
        try {
            mConversation.setPhoneNumber(null);
            fail("No NullArgumentException thrown");
        } catch (NullArgumentException e) {
            //Ok
        }
    }

    public void testPhoneNumber() {
        PhoneNumber number = mContact.getPhoneNumbers().iterator().next();
        mConversation.setPhoneNumber(number);

        assertEquals(number, mConversation.getPhoneNumber());
    }

    public void testChannel() {
        ChannelType channel = ChannelType.SMS;
        mConversation.setChannel(channel);

        assertEquals(channel, mConversation.getChannel());
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
        DialogueMessage message = new DialogueTextMessage(mContact, mChannel, mNumber,
                "Test message 1", MessageDirection.OUTGOING);

        mConversation.addMessage(message);
        mMessages.add(message);

        assertEquals(mMessages.size(), mConversation.getMessageCount());
        assertEquals(mMessages, mConversation.getMessages());
    }

    public void testAddIncomingMessage() {
        DialogueMessage message = new DialogueTextMessage(mContact, null, null,
                "Test message 1", MessageDirection.INCOMING);

        mConversation.addMessage(message);
        mMessages.add(message);

        assertTrue(mConversation.hasUnread());
        assertEquals(mMessages, mConversation.getMessages());
    }

    public void testSetAllMessagesAsRead() {
        DialogueMessage message = new DialogueTextMessage(mContact, null, null,
                "Test message 1", MessageDirection.INCOMING);

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

        DialogueMessage message1 = new DialogueTextMessage(mContact, mChannel, mNumber,
                "Test message 1", MessageDirection.OUTGOING);

        mConversation.addMessage(message1);

        assertFalse(mHasBeenCalled);

        mConversation.addListener(listener);

        DialogueMessage message2 = new DialogueTextMessage(mContact, mChannel , mNumber,
                "Test message 2", MessageDirection.OUTGOING);

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

        DialogueMessage message1 = new DialogueTextMessage(mContact, mChannel, mNumber,
                "Test message 1", MessageDirection.OUTGOING);

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
        String expectedDisplay = "09:42";
        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityYesterday() {
        setNow(LATEST_ACTIVITY.plusDays(1));

        String expectedDisplay = mContext.getString(R.string.yesterday);
        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityTwoDaysAgo() {
        setNow(LATEST_ACTIVITY.plusDays(2));

        String expectedDisplay = mContext.getString(R.string.two_days_ago);
        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityAMonthAndADayAgo() {
        setNow(LATEST_ACTIVITY.plusMonths(1).plusDays(1));

        String expectedDisplay = "22.09";
        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityAYearAndADayAgo() {
        setNow(LATEST_ACTIVITY.plusYears(1).plusDays(1));

        String expectedDisplay = "09/14";
        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }



    public void testLastActivityEarlierThisWeek() throws ParseException {
        setNow(LATEST_ACTIVITY.plusDays(THREE));

        String expectedDisplay =
                mContext.getResources().getStringArray(R.array.days_of_week)[MONDAY_INDEX];
        String toDisplay = mConversation.getLastConversationActivityString(mContext);

        assertEquals(expectedDisplay, toDisplay);
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
        mContacts = new ArrayList<Contact>();
        mContacts.add(mContact);

        mConversation = new DialogueConversation(mContacts);

        DialogueMessage message = new DialogueTextMessage(mContact, mChannel, mNumber,
                "Test message 1", MessageDirection.OUTGOING);

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
        assertEquals(mConversation.getLastActivityTime().getMillis(),
                conversationFromParcel.getLastActivityTime().getMillis());

        mMessages = mConversation.getMessages();
        List<DialogueMessage> parcelMessages = conversationFromParcel.getMessages();

        for (int i = 0; i < mMessages.size(); i++) {
            assertEquals("Message list does not contain the same message",
                    mMessages.get(i).getId(), parcelMessages.get(i).getId());
        }

        assertEquals(mConversation.hasUnread(), conversationFromParcel.hasUnread());
    }

    public void testParcelabilityWithUnread() {
        mContacts = new ArrayList<Contact>();
        mContacts.add(mContact);

        mConversation = new DialogueConversation(mContacts);

        DialogueMessage message = new DialogueTextMessage(mContact, null, null,
                "Test message 1", MessageDirection.INCOMING);

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
        DialogueConversation[] foundNewArray;
        foundNewArray = DialogueConversation.CREATOR.newArray(MAGIC_POSITIVE_INT);

        assertEquals(MAGIC_POSITIVE_INT, foundNewArray.length);
    }

    private void setNow(DateTime now) {
        DateTimeUtils.setCurrentMillisFixed(now.getMillis());
    }
}


