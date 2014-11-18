package ch.epfl.sweng.bohdomp.dialogue.conversation.contact;

import android.os.Bundle;
import android.test.AndroidTestCase;

import java.util.ArrayList;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueDataListener;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;

/**
 * Created by BohDomp! on 18.11.14.
 */
public class DialogueDataNotListenerTest extends AndroidTestCase {

    private static final String CONVERSATION_ID = "CONVERSATION_ID";
    private static final String CONVERSATION = "CONVERSATION";
    private static final int TWELVE = 12;

    private DialogueData mDialogueData;

    public void setUp() {
        mDialogueData = DefaultDialogData.getInstance();
    }

    public void testGetNullConversation() {
        try {
            mDialogueData.getConversation(null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testAddNullMessageToConversation() {
        try {
            mDialogueData.addMessageToConversation(null);
            fail("NullArgumentException expected");
        } catch (IllegalArgumentException e) {
            // all good :)
        }
    }

    public void testAddNullListener() {
        try {
            mDialogueData.addListener(null);
            fail("NullArgumentException expected");
        } catch (IllegalArgumentException e) {
            // all good :)
        }
    }

    public void testRemoveNullListener() {
        try {
            mDialogueData.removeListener(null);
            fail("NullArgumentException expected");
        } catch (IllegalArgumentException e) {
            // all good :)
        }
    }

    public void testRemoveNonAddedListener() {
        mDialogueData.removeListener(new DialogueDataListener() {
            @Override
            public void onDialogueDataChanged() {
                // we don't care :)
            }
        });

        // nothing should happen :)
    }

    public void testRestoreFromNullBundle() {
        try {
            mDialogueData.restoreFromBundle(null);
            fail("NullArgumentException expected");
        } catch (IllegalArgumentException e) {
            // all good :)
        }
    }

    public void testRestoreFromBundleNullConversationId() {
        Bundle bundle = new Bundle();

        bundle.putParcelableArrayList(CONVERSATION_ID, null);

        mDialogueData.restoreFromBundle(bundle);

        // nothing should happen
    }

    public void testRestoreFromBundleTooManyConversationId() {
        Bundle bundle = new Bundle();

        ArrayList<Conversation> conversations = new ArrayList<Conversation>(TWELVE);
        ArrayList<ConversationId> conversationIds = new ArrayList<ConversationId>(TWELVE + 1);

        bundle.putParcelableArrayList(CONVERSATION_ID, conversationIds);
        bundle.putParcelableArrayList(CONVERSATION, conversations);

        mDialogueData.restoreFromBundle(bundle);

        // nothing should happen
    }

    public void testRestoreFromBundle() {
        Bundle bundle = new Bundle();

        ArrayList<Conversation> conversations = new ArrayList<Conversation>();
        ArrayList<ConversationId> conversationIds = new ArrayList<ConversationId>();

        for (int i = 0; i <= TWELVE; i++) {
            Conversation conversation =
                new DialogueConversation(new ArrayList<Contact>(), new SystemTimeProvider());
            conversations.add(conversation);
            conversationIds.add(conversation.getId());
        }

        bundle.putParcelableArrayList(CONVERSATION_ID, conversationIds);
        bundle.putParcelableArrayList(CONVERSATION, conversations);

        mDialogueData.restoreFromBundle(bundle);

        for (int i = 0; i < conversations.size(); i++) {
            assertNotNull(mDialogueData.getConversation(conversations.get(i).getId()));
        }

        // nothing should happen
    }
}
