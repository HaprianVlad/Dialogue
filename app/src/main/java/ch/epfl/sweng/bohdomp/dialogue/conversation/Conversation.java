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
public class Conversation {

    /**
     * Represents a Conversation id. This class is immutable
     */
    public static final class ConversationId{
        private static long topId = 0;

        public static ConversationId getNewConversationId() {
            topId += 1;
            return  new ConversationId(topId);
        }

        private final long id;

        private ConversationId(long idParameter) {
            this.id = idParameter;
        }

        public long getId() {
            return id;
        }
    }
    private final ConversationId conversationId;
    private final Set<Contact> conversationContacts;

    private final List<DialogueMessage> conversationMessages;
    private final Timestamp conversationTimeStamp;

    private int conversationMsgCount;
    private boolean conversationHasUnread;

    public Conversation(Set<Contact> contacts) {
        if (contacts == null) {
            throw new IllegalArgumentException();
        } else {
            this.conversationId = ConversationId.getNewConversationId();
            //Contacts has to be implemeted as imutable
            this.conversationContacts = new HashSet<Contact>(contacts);
            this.conversationMessages = new ArrayList<DialogueMessage>();
            this.conversationMsgCount = 0;
            this.conversationTimeStamp = new Timestamp((new Date()).getTime());
            this.conversationHasUnread = false;
        }
    }


    public ConversationId getConversationId() {
        return conversationId;
    }

    public Set<Contact> getConversationContacts() {
        return new HashSet<Contact>(conversationContacts);
    }

    public List<DialogueMessage> getConversationMessages() {
        return new ArrayList<DialogueMessage>(conversationMessages);
    }

    public Timestamp getConversationTimeStamp() {
        return conversationTimeStamp;
    }

    public int getConversationMsgCount() {
        return conversationMsgCount;
    }

    public boolean getConversationHasUnread() {
        return conversationHasUnread;
    }

    public void addConverstationContact(Contact contact) {
        conversationContacts.add(contact);
    }

    public void addMessage(DialogueMessage message) {
        conversationMessages.add(message);
    }


}
