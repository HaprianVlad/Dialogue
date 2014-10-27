package ch.epfl.sweng.bohdomp.dialogue.conversation;

import android.util.LongSparseArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Represents all conversation in the application
 * This class should be directly connected to the GUI
 * @author BohDomp!
 *
 */
public final class DialogueData {

    private  final static Comparator<Conversation> TIME_STAMPS_COMPARATOR;

    static {
        TIME_STAMPS_COMPARATOR = new Comparator<Conversation>() {
            public int compare(Conversation c1, Conversation c2) {
                return c2.getConversationTimeStamp().
                        compareTo(c1.getConversationTimeStamp()); // lowest timestamp comes before
            }
        };
    }

    private final static DialogueData DIALOGUE_DATA = new DialogueData();

    private  LongSparseArray<Conversation> mConversations = new LongSparseArray<Conversation>();

    private DialogueData() {
        if (DIALOGUE_DATA != null) {
            throw new IllegalStateException("Dialogue Data has been already instanciated !");
        }
    }

    public static DialogueData getInstance() {
        return DIALOGUE_DATA;
    }

    /**
     *
     * @return List of all conversations ordered by their most recent activity
     */
    public List<Conversation> getConversations() {
        if (mConversations == null) {
            return null;
        }
        List<Conversation> conversations = new ArrayList<Conversation>(mConversations.size());
        for (int i = 0; i < mConversations.size(); i++) {
            conversations.add(mConversations.valueAt(i));
        }
        Collections.sort(conversations, TIME_STAMPS_COMPARATOR);
        return conversations;
    }

    /**
     * Getter for a conversation
     * @param conversationId of the conversation we are looking for.
     * @return Conversation associated to the given id.
     */
    public Conversation getConversation(Conversation.ConversationId conversationId) {
        return mConversations.get(conversationId.getId());
    }

    /**
     * Adds a conversation to the data
     * @param conversation a new conversation
     */
    public void addConversation(Conversation conversation) {
        long conversationId = conversation.getConversationId().getId();
        if (mConversations.indexOfKey(conversationId) >= 0) {
            mConversations.put(conversationId, conversation);
        }
    }

    //FIXME: here comes message dispatching logique
}