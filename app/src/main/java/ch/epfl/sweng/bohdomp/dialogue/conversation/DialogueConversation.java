package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 * Class representing a Dialogue conversation. This class is mutable
 */
public class DialogueConversation implements Conversation {

    private final ConversationId conversationId;
    private final Set<Contact> conversationContacts;

    private final List<DialogueMessage> conversationMessages;
    private final Timestamp conversationTimeStamp;

    private final List<ConversationListener> conversationListeners;

    private int conversationMsgCount;
    private boolean conversationHasUnread;

    /**
     * Constructor of the class
     * @param contacts - set of contacts we add to conversation
     */
    public DialogueConversation(Set<Contact> contacts) {
        if (contacts == null) {
            throw new IllegalArgumentException();
        } else {
            this.conversationId = ConversationId.getNewConversationId();
            //Contacts has to be implemeted as imutable
            this.conversationContacts = new HashSet<Contact>(contacts);
            this.conversationMessages = new ArrayList<DialogueMessage>();
            this.conversationListeners = new ArrayList<ConversationListener>();
            this.conversationMsgCount = 0;
            this.conversationTimeStamp = new Timestamp((new Date()).getTime());
            this.conversationHasUnread = false;
        }
    }


    @Override
    public ConversationId getConversationId() {
        return conversationId;
    }


    @Override
    public Set<Contact> getConversationContacts() {
        return new HashSet<Contact>(conversationContacts);
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
        conversationContacts.add(contact);
        notifyListeners();
    }

    @Override
    public void addMessage(DialogueMessage message) {
        conversationHasUnread = true;
        conversationMessages.add(message);
        notifyListeners();
    }

    @Override
    public void addListener(ConversationListener listener) {
        conversationListeners.add(listener);
    }

    @Override
    public void removeListener(ConversationListener listener) {
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
