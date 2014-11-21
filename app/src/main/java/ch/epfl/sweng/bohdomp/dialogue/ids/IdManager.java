package ch.epfl.sweng.bohdomp.dialogue.ids;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Manages ids.
 */
public final class IdManager {
    private long mPreviousDialogueMessageId;
    private long mPreviousConversationId;
    private static IdManager instance;

    private IdManager() {
        mPreviousConversationId = System.currentTimeMillis();
        mPreviousDialogueMessageId = mPreviousConversationId;
    }

    public static IdManager getInstance() {
        if (instance == null) {
            instance = new IdManager();
        }

        return instance;

    }

    public synchronized ConversationId newConversationId() {
        Contract.assertTrue(mPreviousConversationId >= 0, "mPreviousConversationId must be >= 0");

        mPreviousConversationId += 1;
        return  ConversationId.fromLong(mPreviousConversationId);
    }

    public synchronized DialogueMessageId newDialogueMessageId() {
        Contract.assertTrue(mPreviousDialogueMessageId >= 0, "mPreviousDialogueMessageId must be >= 0");

        mPreviousDialogueMessageId += 1;

        return DialogueMessageId.fromLong(mPreviousDialogueMessageId);
    }
}
