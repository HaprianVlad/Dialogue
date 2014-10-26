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
public class DialogueData {
	private static LongSparseArray<Conversation> mConversations = new LongSparseArray<Conversation>();
    private static final Comparator<Conversation> TIME_STAMPS_COMPARATOR;

    static {
        TIME_STAMPS_COMPARATOR = new Comparator<Conversation>() {
            public int compare(Conversation c1, Conversation c2) {
                return c2.getTimeStamp().compareTo(c1.getTimeStamp()); // lowest timestamp comes before
            }
        };
    }

    /**
     *
     * @return List of all conversations ordered by their most recent activity
     */
    public static List<Conversation> getConversations() {
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
     *
     * @param conversationId of the conversation we are looking for.
     * @return Conversation associated to the given id.
     */
    public static Conversation getConversation(long conversationId) {
        return mConversations.get(conversationId);
    }

    /**
     *
     * @param conversation a new conversation
     */
    public static void addConversation(Conversation conversation) {
        long conversationId = conversation.getId();
        if (mConversations.indexOfKey(conversationId) >= 0) {
            mConversations.put(conversationId, conversation);
        }
    }
}