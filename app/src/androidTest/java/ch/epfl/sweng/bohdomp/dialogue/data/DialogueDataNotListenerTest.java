package ch.epfl.sweng.bohdomp.dialogue.data;

import android.os.Bundle;
import android.test.InstrumentationTestCase;

import org.mockito.Mockito;

import java.util.ArrayList;

import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;

/**
 * Created by BohDomp! on 18.11.14.
 */
public class DialogueDataNotListenerTest extends InstrumentationTestCase {

    private static final String CONVERSATION_ID = "CONVERSATION_ID";
    private static final String CONVERSATION = "CONVERSATION";
    private static final int TWELVE = 12;

    private DialogueData mDialogueData;

    public void setUp() {
        mDialogueData = DefaultDialogData.getInstance();
        hackMockito();
    }

    private void hackMockito() {
        System.setProperty("dexmaker.dexcache", getInstrumentation().getTargetContext().getCacheDir().getPath());
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
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testAddNullListener() {
        try {
            mDialogueData.addListener(null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
            // all good :)
        }
    }

    public void testRemoveNullListener() {
        try {
            mDialogueData.removeListener(null);
            fail("NullArgumentException expected");
        } catch (NullArgumentException e) {
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
        } catch (NullArgumentException e) {
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

    // TODO public void testRestoreFromBundle()

    public void testDeleteAll() {
        Contact contact1 = Mockito.mock(Contact.class);
        Contact contact2 = Mockito.mock(Contact.class);

        mDialogueData.createOrGetConversation(contact1);
        mDialogueData.createOrGetConversation(contact2);

        assertFalse(0 == mDialogueData.getConversations().size());

        mDialogueData.removeAllConversations();

        assertTrue(0 == mDialogueData.getConversations().size());
    }

    public void testUpdateConversationNull() {
        try {
            mDialogueData.updateConversation(null);
            fail();
        } catch (NullArgumentException e) {
            //OK
        }
    }

    public void testUpdateConversation() {
        Contact contact1 = Mockito.mock(Contact.class);

        Conversation conversationBefore = mDialogueData.createOrGetConversation(contact1);
        assertNull(conversationBefore.getChannel());

        conversationBefore.setChannel(ChannelType.SMS);

        mDialogueData.updateConversation(conversationBefore);

        Conversation conversationAfter = mDialogueData.createOrGetConversation(contact1);
        assertNotNull(conversationAfter.getChannel());
        assertEquals(ChannelType.SMS, conversationAfter.getChannel());

        mDialogueData.removeConversation(conversationBefore.getId());
        mDialogueData.removeConversation(conversationAfter.getId());

    }
}
