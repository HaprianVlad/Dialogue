package ch.epfl.sweng.bohdomp.dialogue.ids;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;

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
        if (BuildConfig.DEBUG && mPreviousConversationId < 0) {
            throw new AssertionError("negative mPreviousConversationId");
        }
        mPreviousConversationId += 1;
        return  ConversationId.fromLong(mPreviousConversationId);
    }

    public synchronized DialogueMessageId newDialogueMessageId() {
        if (BuildConfig.DEBUG && mPreviousDialogueMessageId < 0) {
            throw new AssertionError("negative mPreviousDialogueMessageId");
        }

        mPreviousDialogueMessageId += 1;

        return DialogueMessageId.fromLong(mPreviousDialogueMessageId);
    }
}
