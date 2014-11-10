package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 * Class representing a Dialogue conversation. This class is mutable
 */
public class DialogueConversation implements Conversation {
    public static final String CONVERSATION_ID = "conversationID";

    private final ConversationId mId;
    private final List<Contact> mContacts;

    private final List<DialogueMessage> mMessages;
    private final Timestamp mLastActivityTime;

    private final List<ConversationListener> mListeners;

    private int mMessageCount;
    private boolean mHasUnread;

    /**
     * Constructor of the class
     * @param contacts - set of contacts we add to conversation
     */
    public DialogueConversation(List<Contact> contacts) {
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

        mHasUnread = true;
        mMessages.add(message);
        mMessageCount = mMessageCount + 1;

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
