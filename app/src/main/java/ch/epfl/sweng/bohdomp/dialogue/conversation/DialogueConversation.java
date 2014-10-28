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
            this.conversationMsgCount = 0;
            this.conversationTimeStamp = new Timestamp((new Date()).getTime());
            this.conversationHasUnread = false;
        }
    }

    /**
     * Getter for converstation id
     * @return converstation id
     */
    @Override
    public ConversationId getConversationId() {
        return conversationId;
    }

    /**
     * Getter for the set of contacts
     * @return the set of contacts of the conversation
     */
    @Override
    public Set<Contact> getConversationContacts() {
        return new HashSet<Contact>(conversationContacts);
    }

    /**
     * Getter for the messages list of the conversation
     * @return list of messages
     */
    @Override
    public List<DialogueMessage> getConversationMessages() {
        return new ArrayList<DialogueMessage>(conversationMessages);
    }

    /**
     * Getter for the timeStamp of the conversation
     * @return the current timestamp
     */
    @Override
    public Timestamp getConversationTimeStamp() {
        return conversationTimeStamp;
    }

    /**
     * Getter for the number of messages in the conversation
     * @return number of messages in the conversation
     */
    @Override
    public int getConversationMsgCount() {
        return conversationMsgCount;
    }

    /**
     * Getter for the unread boolean
     * @return conversationHasUnread paramater
     */
    @Override
    public boolean getConversationHasUnread() {
        return conversationHasUnread;
    }

    /**
     * Adds a contact to the conversation
     * @param contact the contact we want to add
     */
    @Override
    public void addConversationContact(Contact contact) {
        conversationContacts.add(contact);
    }

    /**
     * Add a message to the conversation
     * @param message the message we want to add
     */
    @Override
    public void addMessage(DialogueMessage message) {
        conversationMessages.add(message);
    }

    @Override
    public void addListener(ConversationListener listener) {

    }

    @Override
    public void removeListener() {

    }


}
