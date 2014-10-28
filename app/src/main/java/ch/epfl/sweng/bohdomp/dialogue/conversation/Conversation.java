package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

/**
 *
 */
public interface Conversation {

    ConversationId getConversationId();

    Set<Contact> getConversationContacts();

    List<DialogueMessage> getConversationMessages();

    Timestamp getConversationTimeStamp();

    int getConversationMsgCount();

    boolean getConversationHasUnread();

    void addConversationContact(Contact contact);

    void addMessage(DialogueMessage message);

    void addListener(ConversationListener listener);

    void removeListener();


    /**
     * Represents a DialogueConversation id. This class is immutable
     */
    public static final class ConversationId{

        private static long lastId = 0;

        public static ConversationId getNewConversationId() {
            lastId += 1;
            return  new ConversationId(lastId);
        }

        private final long id;

        private ConversationId(long idParameter) {
            this.id = idParameter;
        }

        public long getId() {
            return id;
        }
    }
}
