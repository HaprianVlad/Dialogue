package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.content.Context;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageStatus;

/**
 * Class representing a Dialogue conversation. This class is mutable
 */
public class DialogueConversation implements Conversation {
    public static final String CONVERSATION_ID = "conversationID";
    private static final long MILLIS_IN_DAY = 86400000;

    /**
     * Describes all week day from SimpleDateFormat 'E'
     */
    private enum weekDays {
        Mon, Tue, Wed, Thu, Fri, Sat, Sun
    }

    private final ConversationId mId;
    private final List<Contact> mContacts;

    private final List<DialogueMessage> mMessages;
    private final List<ConversationListener> mListeners;
    private final SystemTimeProvider mTimeProvider;

    private Timestamp mLastActivityTime;

    private int mMessageCount;
    private boolean mHasUnread;

    /**
     * Constructor of the class
     * @param contacts - set of contacts we add to conversation
     * @param systemTimeProvider - will provide us system time
     */
    public DialogueConversation(List<Contact> contacts, SystemTimeProvider systemTimeProvider) {
        if (contacts == null) {
            throw new NullArgumentException("contacts == null!");
        }

        this.mId = IdManager.getInstance().newConversationId();
        this.mContacts = new ArrayList<Contact>(contacts);
        this.mMessages = new ArrayList<DialogueMessage>();
        this.mListeners = new ArrayList<ConversationListener>();
        this.mMessageCount = 0;
        this.mLastActivityTime = new Timestamp((new Date()).getTime());
        this.mHasUnread = false;
        this.mTimeProvider = systemTimeProvider;
    }


    @Override
    public ConversationId getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mContacts.get(0).getDisplayName();
    }


    @Override
    public List<Contact> getContacts() {
        return new ArrayList<Contact>(mContacts);
    }

    @Override
    public List<DialogueMessage> getMessages() {
        return new ArrayList<DialogueMessage>(mMessages);
    }


    @Override
    public Timestamp getLastActivityTime() {
        return mLastActivityTime;
    }

    @Override
    public String getLastConversationActivityString(Context context) {

        long currentTime = mTimeProvider.currentTimeMillis();
        long elapsedTime = currentTime - mLastActivityTime.getTime();
        long millisElapsedToday = currentTime % MILLIS_IN_DAY;

        if (elapsedTime <= millisElapsedToday) {
            SimpleDateFormat onlyHoursAndMin = new SimpleDateFormat("HH:mm", Locale.ENGLISH);

            return onlyHoursAndMin.format(mLastActivityTime);
        }

        if (elapsedTime <= (millisElapsedToday + MILLIS_IN_DAY)) {

            return context.getString(R.string.yesterday);
        }

        if (elapsedTime <= (millisElapsedToday + 2 * MILLIS_IN_DAY)) {

            return context.getString(R.string.two_days_ago);
        }

        SimpleDateFormat year = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        Date currentDate = new Date(currentTime);

        if (!year.format(currentDate).equals(year.format(mLastActivityTime))) {
            SimpleDateFormat onlyMonthYear = new SimpleDateFormat("MM/yy", Locale.ENGLISH);

            return onlyMonthYear.format(mLastActivityTime);
        }

        SimpleDateFormat week = new SimpleDateFormat("ww", Locale.ENGLISH);

        if (!week.format(currentDate).equals(week.format(mLastActivityTime))) {
            SimpleDateFormat onlyDayMonth = new SimpleDateFormat("dd.MM", Locale.ENGLISH);

            return onlyDayMonth.format(mLastActivityTime);
        }

        SimpleDateFormat dayOfTheWeek = new SimpleDateFormat("E", Locale.ENGLISH);

        int indexWeekDay = weekDays.valueOf(dayOfTheWeek.format(mLastActivityTime)).ordinal();

        return context.getResources().getStringArray(R.array.days_of_week)[indexWeekDay];
    }

    @Override
    public int getMessageCount() {
        return mMessageCount;
    }


    @Override
    public boolean hasUnread() {
        return mHasUnread;
    }


    @Override
    public void addContact(Contact contact) {
        if (contact == null) {
            throw new NullArgumentException("contact == null !");
        }

        mContacts.add(contact);
        notifyListeners();
    }

    @Override
    public void removeContact(Contact contact) {
        if (contact == null) {
            throw new NullArgumentException("contact == null !");
        }

        if (mContacts.contains(contact)) {
            mContacts.remove(contact);
            notifyListeners();
        }
    }

    @Override
    public void addMessage(DialogueMessage message) {
        if (message == null) {
            throw new NullArgumentException("message == null !");
        }

        if (message.getMessageStatus() == MessageStatus.INCOMING) {
            mHasUnread = true;
        }

        mMessages.add(message);
        mMessageCount = mMessageCount + 1;

        mLastActivityTime = new Timestamp(mTimeProvider.currentTimeMillis());

        notifyListeners();
    }

    @Override
    public void addListener(ConversationListener listener) {
        if (listener == null) {
            throw new NullArgumentException("listener == null !");
        }

        mListeners.add(listener);
    }

    @Override
    public void removeListener(ConversationListener listener) {
        if (listener == null) {
            throw new NullArgumentException("listener == null !");
        }

        if (mListeners.contains(listener)) {
            mListeners.remove(listener);
        }
    }

    @Override
    public void setAllMessagesAsRead() {
        mHasUnread = false;
        notifyListeners();
    }

    //Method that notifies listeners when a change in conversation occurs
    private void notifyListeners() {
        for (ConversationListener listener : mListeners) {
            listener.onConversationChanged(this.getId());
        }
    }
}
