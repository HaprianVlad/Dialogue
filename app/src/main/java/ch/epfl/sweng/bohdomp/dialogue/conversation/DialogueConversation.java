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
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;
import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageStatus;

/**
 * Class representing a Dialogue conversation. This class is mutable
 */
public final class DialogueConversation implements Conversation {
    public static final String CONVERSATION_ID = "conversationID";
    private static final long MILLIS_IN_DAY = 86400000;

    /**
     * Describes all week day from SimpleDateFormat 'E'
     */
    private enum WeekDays {
        Mon, Tue, Wed, Thu, Fri, Sat, Sun
    }

    private final ConversationId mId;
    private final SystemTimeProvider mTimeProvider;

    private final List<Contact> mContact;

    private final List<DialogueMessage> mMessages;
    private List<ConversationListener> mListeners;

    private Timestamp mLastActivityTime;

    private Contact.ChannelType mChannel;
    private Contact.PhoneNumber mPhoneNumber;

    private int mMessageCount;
    private boolean mHasUnread;

    /**
     * Constructor of the class
     * @param contacts - set of contacts we add to conversation
     * @param systemTimeProvider - will provide us system time
     */
    public DialogueConversation(List<Contact> contacts, SystemTimeProvider systemTimeProvider) {
        if (contacts == null) {
            throw new NullArgumentException("contacts");
        }

        if (contacts.size() == 0) {
            throw new IllegalArgumentException("Must have at least one contact");
        }

        if (contacts.contains(null)) {
            throw new IllegalArgumentException("There is a null contact");
        }

        if (systemTimeProvider == null) {
            throw new NullArgumentException("systemTimeProvider");
        }

        this.mId = IdManager.getInstance().newConversationId();
        this.mContact = new ArrayList<Contact>(contacts);
        this.mChannel = null;
        this.mPhoneNumber = null;
        this.mMessages = new ArrayList<DialogueMessage>();
        this.mListeners = new ArrayList<ConversationListener>();
        this.mMessageCount = 0;
        this.mTimeProvider = systemTimeProvider;
        this.mLastActivityTime = new Timestamp(mTimeProvider.currentTimeMillis());
        this.mHasUnread = false;
    }

    public DialogueConversation(Conversation conversation) {
        if (conversation == null) {
            throw new NullArgumentException("conversation == null!");
        }

        this.mId = conversation.getId();
        this.mContact = new ArrayList<Contact>(conversation.getContacts());
        this.mChannel = conversation.getChannel();
        this.mPhoneNumber = conversation.getPhoneNumber();
        this.mMessages = new ArrayList<DialogueMessage>(conversation.getMessages());
        this.mListeners = new ArrayList<ConversationListener>(conversation.getListeners());
        this.mMessageCount = conversation.getMessageCount();
        this.mTimeProvider = conversation.getSystemTimeProvider();
        this.mLastActivityTime =conversation.getLastActivityTime();
        this.mHasUnread = conversation.getHasUnread();
    }

    @Override
    public ConversationId getId() {
        return mId;
    }

    @Override
    public String getName() {
        return mContact.get(0).getDisplayName();
    }


    @Override
    public List<Contact> getContacts() {
        return new ArrayList<Contact>(mContact);
    }

    @Override
    public void setChannel(Contact.ChannelType channel) {
        Contract.throwIfArgNull(channel, "channel");
        this.mChannel = channel;
        notifyListeners();
    }

    @Override
    public Contact.ChannelType getChannel() {
        return mChannel;
    }

    @Override
    public void setPhoneNumber(Contact.PhoneNumber phone) {
        Contract.throwIfArgNull(phone, "phone number");
        this.mPhoneNumber = phone;
        notifyListeners();
    }

    @Override
    public Contact.PhoneNumber getPhoneNumber() {
        return mPhoneNumber;
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
        if (context == null) {
            throw new NullArgumentException("context");
        }

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

        int indexWeekDay = WeekDays.valueOf(dayOfTheWeek.format(mLastActivityTime)).ordinal();

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

        mContact.add(contact);
        notifyListeners();
    }

    @Override
    public void removeContact(Contact contact) {
        if (contact == null) {
            throw new NullArgumentException("contact == null !");
        }

        if (mContact.contains(contact)) {
            mContact.remove(contact);
            notifyListeners();
        }
    }

    @Override
    public void addMessage(DialogueMessage message) {
        if (message == null) {
            throw new NullArgumentException("message == null !");
        }

        if (message.getStatus() == MessageStatus.INCOMING) {
            mHasUnread = true;
        }

        mMessages.add(message);
        mMessageCount += 1;

        mLastActivityTime = new Timestamp(mTimeProvider.currentTimeMillis());

        notifyListeners();
    }

    @Override
    public void addListener(ConversationListener listener) {
        if (listener == null) {
            throw new NullArgumentException("listener == null !");
        }

        if (mListeners == null) {
            mListeners = new ArrayList<ConversationListener>();
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
    public List<ConversationListener> getListeners() {
        return new ArrayList<ConversationListener>(mListeners);
    }

    @Override
    public SystemTimeProvider getSystemTimeProvider() {
        return mTimeProvider;
    }

    @Override
    public boolean getHasUnread() {
        return mHasUnread;
    }

    @Override
    public void setAllMessagesAsRead() {
        mHasUnread = false;
        notifyListeners();
    }

    //Method that notifies listeners when a change in conversation occurs
    private void notifyListeners() {
        if (mListeners != null) {
            for (ConversationListener listener : mListeners) {
                listener.onConversationChanged(mId);
            }

        }

    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        if (dest == null) {
            throw new NullArgumentException("dest == null");
        }

        dest.writeParcelable(this.mId, flags);
        dest.writeList(mContact);
        //FIXME
        //dest.writeParcelable(mChannel, flags);
        dest.writeParcelable(mPhoneNumber, flags);
        dest.writeList(mMessages);
        dest.writeLong(this.mLastActivityTime.getTime());
        dest.writeInt(this.mMessageCount);
        dest.writeByte(mHasUnread ? (byte) 1 : (byte) 0);

    }

    @SuppressWarnings("unchecked") // we cannot solve this unchecked problem!
    private DialogueConversation(Parcel in, SystemTimeProvider timeProvider) {

        this.mTimeProvider = timeProvider;
        //FIXME
        this.mChannel = Contact.ChannelType.SMS;
        this.mId = in.readParcelable(ConversationId.class.getClassLoader());
        this.mContact = in.readArrayList(Contact.class.getClassLoader());
        this.mPhoneNumber = in.readParcelable(Contact.PhoneNumber.class.getClassLoader());
        this.mMessages =  in.readArrayList(DialogueMessage.class.getClassLoader());
        this.mLastActivityTime = new Timestamp(in.readLong());
        this.mMessageCount = in.readInt();
        this.mHasUnread = in.readByte() != 0;

    }

    public static final Creator<DialogueConversation> CREATOR = new Creator<DialogueConversation>() {
        public DialogueConversation createFromParcel(Parcel source) {
            if (source == null) {
                throw new NullArgumentException("source == null");
            }

            return new DialogueConversation(source, new SystemTimeProvider());
        }

        public DialogueConversation[] newArray(int size) {
            return new DialogueConversation[size];
        }
    };
}
