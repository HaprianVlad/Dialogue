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

    /**
     * Constructor of the class
     * @param contacts - set of contacts we add to conversation
     */
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

    /**
     * Getter for converstation id
     * @return converstation id
     */
    public ConversationId getConversationId() {
        return conversationId;
    }

    /**
     * Getter for the set of contacts
     * @return the set of contacts of the conversation
     */
    public Set<Contact> getConversationContacts() {
        return new HashSet<Contact>(conversationContacts);
    }

    /**
     * Getter for the messages list of the conversation
     * @return list of messages
     */
    public List<DialogueMessage> getConversationMessages() {
        return new ArrayList<DialogueMessage>(conversationMessages);
    }

    /**
     * Getter for the timeStamp of the conversation
     * @return the current timestamp
     */
    public Timestamp getConversationTimeStamp() {
        return conversationTimeStamp;
    }

    /**
     * Getter for the number of messages in the conversation
     * @return number of messages in the conversation
     */
    public int getConversationMsgCount() {
        return conversationMsgCount;
    }

    /**
     * Getter for the unread boolean
     * @return conversationHasUnread paramater
     */
    public boolean getConversationHasUnread() {
        return conversationHasUnread;
    }

    /**
     * Adds a contact to the conversation
     * @param contact the contact we want to add
     */
    public void addConverstationContact(Contact contact) {
        conversationContacts.add(contact);
    }

    /**
     * Add a message to the conversation
     * @param message the message we want to add
     */
    public void addMessage(DialogueMessage message) {
        conversationMessages.add(message);
    }


}
