package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.content.Context;
import android.os.Parcel;


import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.AndroidContact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageStatus;

/**
 * Class representing a Dialogue conversation. This class is mutable
 */
public final class DialogueConversation implements Conversation, android.os.Parcelable {
    public static final String CONVERSATION_ID = "conversationID";
    private static final long MILLIS_IN_DAY = 86400000;

    /**
     * Describes all week day from SimpleDateFormat 'E'
     */
    private enum weekDays {
        Mon, Tue, Wed, Thu, Fri, Sat, Sun
    }

    private  ConversationId mId;
    private  List<Contact> mContacts;

    private  List<DialogueMessage> mMessages;
    private  List<ConversationListener> mListeners;
    private  SystemTimeProvider mTimeProvider;

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
        this.mTimeProvider = systemTimeProvider;
        this.mLastActivityTime = new Timestamp(mTimeProvider.currentTimeMillis());
        this.mHasUnread = false;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.mId, flags);
        dest.writeTypedList(mContacts);
        dest.writeTypedList(mMessages);
        dest.writeLong(this.mLastActivityTime.getTime());
        dest.writeInt(this.mMessageCount);
        dest.writeByte(mHasUnread ? (byte) 1 : (byte) 0);

    }

    private DialogueConversation(Parcel in) {
        this.mId = in.readParcelable(ConversationId.class.getClassLoader());
        in.readTypedList(mContacts, AndroidContact.CREATOR);
        in.readTypedList(mMessages, DialogueTextMessage.CREATOR);
        this.mLastActivityTime = new Timestamp(in.readLong());
        this.mMessageCount = in.readInt();
        this.mHasUnread = in.readByte() != 0;

    }

    public static final Creator<DialogueConversation> CREATOR = new Creator<DialogueConversation>() {
        public DialogueConversation createFromParcel(Parcel source) {
            return new DialogueConversation(source);
        }

        public DialogueConversation[] newArray(int size) {
            return new DialogueConversation[size];
        }
    };
}
