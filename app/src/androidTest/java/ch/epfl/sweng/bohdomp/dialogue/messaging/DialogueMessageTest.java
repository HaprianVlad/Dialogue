package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.content.Context;

import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.PhoneNumber;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageDirection.INCOMING;

/**
 * Created by BohDomp! on 26.11.14.
 */
public class DialogueMessageTest extends MockTestCase {

    private static final DateTime MESSAGE_CREATION_TIME = new DateTime(2014, 9, 22, 9, 42, 42, 42);
    private static final int THIRTY = 30;

    private static final String PHONE_NUMBER = "0773207769";
    private static final String MESSAGE_BODY = "blabla";

    private Context mContext;

    private ContactFactory mContactFactory;
    private PhoneNumber.Tag mTag;
    private Contact mContact;
    private PhoneNumber mNumber;
    private ChannelType mChannel;
    private DialogueMessage mMessage;

    public void setUp() throws Exception {
        super.setUp();

        mContext = getInstrumentation().getTargetContext();
        mContactFactory = new ContactFactory(mContext);

        mContact = mContactFactory.contactFromNumber(PHONE_NUMBER);

        mTag = PhoneNumber.Tag.MOBILE;
        mNumber = new PhoneNumber(PHONE_NUMBER, mTag);
        mChannel = ChannelType.SMS;

        DateTimeUtils.setCurrentMillisFixed(MESSAGE_CREATION_TIME.getMillis());
        mMessage = new DialogueTextMessage(mContact, mChannel, mNumber, MESSAGE_BODY, INCOMING);
    }

    public void tearDown() throws Exception {
        super.tearDown();

        DateTimeUtils.setCurrentMillisSystem();
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

    public void testPrettyTimeStamp30SAgo() {
        setNow(MESSAGE_CREATION_TIME.plusSeconds(THIRTY));

        String expectedDisplay = mContext.getString(R.string.now);
        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testPrettyTimeStamp30MinAgo() {
        setNow(MESSAGE_CREATION_TIME.plusMinutes(THIRTY));

        String expectedDisplay = "09:42";
        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityYesterday() {
        setNow(MESSAGE_CREATION_TIME.plusDays(1));

        String expectedDisplay = mContext.getString(R.string.yesterday) + ": 09:42";
        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityTwoDaysAgo() {
        setNow(MESSAGE_CREATION_TIME.plusDays(2));

        String expectedDisplay = mContext.getString(R.string.two_days_ago) + ": 09:42";
        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityAMonthAndADayAgo() {
        setNow(MESSAGE_CREATION_TIME.plusMonths(1).plusDays(1));

        String expectedDisplay = "22.09: 09:42";
        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    public void testLastActivityAYearAndADayAgo() {
        setNow(MESSAGE_CREATION_TIME.plusYears(1).plusDays(1));

        String expectedDisplay = "22/09/14: 09:42";
        String toDisplay = mMessage.prettyTimeStamp(mContext);

        assertEquals(expectedDisplay, toDisplay);
    }

    private void setNow(DateTime now) {
        DateTimeUtils.setCurrentMillisFixed(now.getMillis());
    }
}
