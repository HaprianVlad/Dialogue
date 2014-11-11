package ch.epfl.sweng.bohdomp.dialogue.conversation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;

/**
 * Default implementation of DialogData
 */
public final class DefaultDialogData implements DialogueData {
    private  final static Comparator<Conversation> TIME_STAMPS_COMPARATOR;

    static {
        TIME_STAMPS_COMPARATOR = new Comparator<Conversation>() {
            public int compare(Conversation c1, Conversation c2) {
                return c2.getLastActivityTime().
                        compareTo(c1.getLastActivityTime()); // lowest timestamp comes before
            }
        };
    }

    private final static DialogueData OBJECT = new DefaultDialogData();

    private HashMap<ConversationId, Conversation> mConversations =
            new HashMap<ConversationId, Conversation>();

    /**
     * Gets the singleton instance
     * @return gets the singleton instance
     */
    public static DialogueData getInstance() {
        return OBJECT;
    }

    /*
        See DialogData.getConversations
     */
    public List<Conversation> getConversations() {
        if (mConversations == null) {
            return null;
        }

        List<Conversation> dialogueConversations = new ArrayList<Conversation>(mConversations.values());

        Collections.sort(dialogueConversations, TIME_STAMPS_COMPARATOR);
        return dialogueConversations;
    }

    /*
    See DialogData.getConversation
    */
    public Conversation getConversation(ConversationId conversationId) {
        if (conversationId == null) {
            throw new NullArgumentException("conversationId");
        }

        return mConversations.get(conversationId);
    }

    /*
    See DialogData.getConversation
    */
    public Conversation createOrGetConversation(Contact contact) {
        Collection<Conversation> conversations = mConversations.values();

        // Try finding it first.
        for (Conversation conversation : conversations) {
            if (conversation.getContacts().contains(contact)) {
                return conversation;
            }
        }

        // Default case, conversation not found.
        List<Contact> contacts = new ArrayList<Contact>();
        contacts.add(contact);
        Conversation conversation = new DialogueConversation(contacts, new SystemTimeProvider());

        mConversations.put(conversation.getId(), conversation);
        return conversation;
    }

    /*
    See DialogData.getConversation
    */
    public void removeConversation(ConversationId id) {
        if (mConversations.containsKey(id)) {
            mConversations.remove(id);
        }
    }
}
