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

    private  final static Comparator<DialogueConversation> TIME_STAMPS_COMPARATOR;

    static {
        TIME_STAMPS_COMPARATOR = new Comparator<DialogueConversation>() {
            public int compare(DialogueConversation c1, DialogueConversation c2) {
                return c2.getConversationTimeStamp().
                        compareTo(c1.getConversationTimeStamp()); // lowest timestamp comes before
            }
        };
    }

    private final static DialogueData DIALOGUE_DATA = new DialogueData();

    private  LongSparseArray<DialogueConversation> mConversations = new LongSparseArray<DialogueConversation>();

    private DialogueData() {
        assert DIALOGUE_DATA != null: "Dialog data has already been instanciated";
    }

    public static DialogueData getInstance() {
        return DIALOGUE_DATA;
    }

    /**
     *
     * @return List of all conversations ordered by their most recent activity
     */
    public List<DialogueConversation> getConversations() {
        if (mConversations == null) {
            return null;
        }

        List<DialogueConversation> dialogueConversations = new ArrayList<DialogueConversation>(mConversations.size());

        for (int i = 0; i < mConversations.size(); i++) {
            dialogueConversations.add(mConversations.valueAt(i));
        }

        Collections.sort(dialogueConversations, TIME_STAMPS_COMPARATOR);
        return dialogueConversations;
    }

    /**
     * Getter for a conversation
     * @param conversationId of the conversation we are looking for.
     * @return DialogueConversation associated to the given id.
     */
    public DialogueConversation getConversation(long conversationId) {
        return mConversations.get(conversationId);
    }

    /**
     * Adds a dialogueConversation to the data
     * @param dialogueConversation a new dialogueConversation
     */
    public void addConversation(DialogueConversation dialogueConversation) {
        long conversationId = dialogueConversation.getId();

        if (mConversations.indexOfKey(conversationId) >= 0) {
            mConversations.put(conversationId, dialogueConversation);
        }
    }

    //FIXME: here comes message dispatching logique
}