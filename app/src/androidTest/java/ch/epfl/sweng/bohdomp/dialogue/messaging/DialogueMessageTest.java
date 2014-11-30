package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.content.Context;

import org.mockito.Mockito;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageDirection.INCOMING;

/**
 * Created by BohDomp! on 26.11.14.
 */
public class DialogueMessageTest extends MockTestCase {

    private static final long MILLIS_IN_DAY = 86400000;
    private static final long MILLIS_IN_10MIN = 100000;
    private static final int NB_YEAR_DAY = 366;
    private static final int NB_MONTH_DAY = 31;
    private static final long MAGIC_MONDAY = 1415628854000L;
    private static final String PHONE_NUMBER = "0773207769";
    private static final String MESSAGE_BODY = "blabla";

    private Context mContext;
    private SystemTimeProvider mTimeProvider;

    private ContactFactory mContactFactory;
    private Contact.PhoneNumber.Tag mTag;
    private Contact mContact;
    private Contact.PhoneNumber mNumber;
    private Contact.ChannelType mChannel;
    private DialogueMessage mMessage;

    public void setUp() throws Exception {
        super.setUp();

        mContext = getInstrumentation().getTargetContext();
        mContactFactory = new ContactFactory(mContext);

        mContact = mContactFactory.contactFromNumber(PHONE_NUMBER);

        mTag = Contact.PhoneNumber.Tag.MOBILE;
        mNumber = new Contact.PhoneNumber(PHONE_NUMBER, mTag);
        mChannel = Contact.ChannelType.SMS;
        mMessage = new DialogueTextMessage(mContact, mChannel, mNumber, MESSAGE_BODY, INCOMING);
    }

    public void testPrettyTimeStampNullContext() {
        try {
            mMessage.prettyTimeStamp(null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // alles gut! :)
        }
    }

    public void testPrettyTimeStampNow() {
        String expectedDisplay = mContext.getString(R.string.now);

        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testPrettyTimeStampSameDay() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();

        DialogueMessage.setTimeProvider(mTimeProvider);
        mMessage = new DialogueTextMessage(mContact, mChannel, mNumber, MESSAGE_BODY, INCOMING);


        Mockito.doReturn(timeSinceMagicMonday(MILLIS_IN_10MIN)).when(mTimeProvider).currentTimeMillis();

        Timestamp lastActivity = mMessage.getTimeStamp();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        String expectedDisplay = onlyHoursAndMin.format(lastActivity);

        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityYesterday() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();

        DialogueMessage.setTimeProvider(mTimeProvider);
        mMessage = new DialogueTextMessage(mContact, mChannel, mNumber, MESSAGE_BODY, INCOMING);

        Mockito.doReturn(timeSinceMagicMonday(1)).when(mTimeProvider).currentTimeMillis();

        Timestamp lastActivity = mMessage.getTimeStamp();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        String expectedDisplay = mContext.getString(R.string.yesterday) + ": " + onlyHoursAndMin.format(lastActivity);

        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityTwoDaysAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();

        DialogueMessage.setTimeProvider(mTimeProvider);
        mMessage = new DialogueTextMessage(mContact, mChannel, mNumber, MESSAGE_BODY, INCOMING);

        Mockito.doReturn(timeSinceMagicMonday(2)).when(mTimeProvider).currentTimeMillis();

        Timestamp lastActivity = mMessage.getTimeStamp();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

        String expectedDisplay = mContext.getString(R.string.two_days_ago) + ": "
                + onlyHoursAndMin.format(lastActivity);

        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityOneYearAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();

        DialogueMessage.setTimeProvider(mTimeProvider);
        mMessage = new DialogueTextMessage(mContact, mChannel, mNumber, MESSAGE_BODY, INCOMING);

        Timestamp lastActivity = mMessage.getTimeStamp();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("dd/MM/yy: HH:mm", Locale.ENGLISH);
        String expectedDisplay = onlyHoursAndMin.format(lastActivity);

        Mockito.doReturn(timeSinceMagicMonday(NB_YEAR_DAY)).when(mTimeProvider).currentTimeMillis();

        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityOneMonthAgo() {
        mTimeProvider = Mockito.mock(SystemTimeProvider.class);
        Mockito.doReturn(timeSinceMagicMonday(0)).when(mTimeProvider).currentTimeMillis();

        DialogueMessage.setTimeProvider(mTimeProvider);
        mMessage = new DialogueTextMessage(mContact, mChannel, mNumber, MESSAGE_BODY, INCOMING);

        Timestamp lastActivity = mMessage.getTimeStamp();
        SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("dd.MM: HH:mm", Locale.ENGLISH);
        String expectedDisplay = onlyHoursAndMin.format(lastActivity);

        Mockito.doReturn(timeSinceMagicMonday(NB_MONTH_DAY)).when(mTimeProvider).currentTimeMillis();

        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    private long timeSinceMagicMonday(int nbDaysToAdd) {

        return MAGIC_MONDAY + nbDaysToAdd * MILLIS_IN_DAY;
    }

    private long timeSinceMagicMonday(long millisToAdd) {

        return MAGIC_MONDAY + millisToAdd;
    }
}
