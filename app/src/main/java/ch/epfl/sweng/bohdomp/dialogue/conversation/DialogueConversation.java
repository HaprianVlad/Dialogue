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

    private final ConversationId conversationId;
    private final List<Contact> conversationContacts;

    private final List<DialogueMessage> conversationMessages;
    private final Timestamp conversationTimeStamp;

    private final List<ConversationListener> conversationListeners;

    private int conversationMsgCount;
    private boolean conversationHasUnread;

    /**
     * Constructor of the class
     * @param contacts - set of contacts we add to conversation
     */
    public DialogueConversation(List<Contact> contacts) {
        if (contacts == null) {
            throw new NullArgumentException("contacts == null!");
        }

        this.conversationId = IdManager.getInstance().newConversationId();
        this.conversationContacts = new ArrayList<Contact>(contacts);
        this.conversationMessages = new ArrayList<DialogueMessage>();
        this.conversationListeners = new ArrayList<ConversationListener>();
        this.conversationMsgCount = 0;
        this.conversationTimeStamp = new Timestamp((new Date()).getTime());
        this.conversationHasUnread = false;
    }


    @Override
    public ConversationId getId() {
        return conversationId;
    }

    @Override
    public String getConversationName() {
        return conversationContacts.get(0).getDisplayName();
    }


    @Override
    public List<Contact> getConversationContacts() {
        return new ArrayList<Contact>(conversationContacts);
    }

    @Override
    public List<DialogueMessage> getConversationMessages() {
        return new ArrayList<DialogueMessage>(conversationMessages);
    }


    @Override
    public Timestamp getConversationTimeStamp() {
        return conversationTimeStamp;
    }


    @Override
    public int getConversationMsgCount() {
        return conversationMsgCount;
    }


    @Override
    public boolean getConversationHasUnread() {
        return conversationHasUnread;
    }


    @Override
    public void addConversationContact(Contact contact) {
        if (contact == null) {
            throw new NullArgumentException("contact == null !");
        }

        conversationContacts.add(contact);
        notifyListeners();
    }

    @Override
    public void removeConversationContact(Contact contact) {
        if (contact == null) {
            throw new NullArgumentException("contact == null !");
        }

        if (conversationContacts.contains(contact)) {
            conversationContacts.remove(contact);
            notifyListeners();
        }
    }

    @Override
    public void addMessage(DialogueMessage message) {
        if (message == null) {
            throw new NullArgumentException("message == null !");
        }

        conversationHasUnread = true;
        conversationMessages.add(message);
        notifyListeners();
    }

    @Override
    public void addListener(ConversationListener listener) {
        if (listener == null) {
            throw new NullArgumentException("listener == null !");
        }

        conversationListeners.add(listener);
    }

    @Override
    public void removeListener(ConversationListener listener) {
        if (listener == null) {
            throw new NullArgumentException("listener == null !");
        }

        if (conversationListeners.contains(listener)) {
            conversationListeners.remove(listener);
        }
    }

    @Override
    public void setAllMessagesAsRead() {
        conversationHasUnread = false;
        notifyListeners();
    }

    //Method that notifies listeners when a change in conversation occurs
    private void notifyListeners() {
        for (ConversationListener listener : conversationListeners) {
            listener.onConversationChanged(this);
        }
    }
}
