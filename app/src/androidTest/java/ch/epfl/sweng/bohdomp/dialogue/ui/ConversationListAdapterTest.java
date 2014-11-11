package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ui.contactList.ConversationListAdapter;

/**
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.contactList.ConversationListAdapter}
 */
public class ConversationListAdapterTest extends AndroidTestCase {

    public ConversationListAdapterTest() {
        super();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testNullContext() {
        List<Conversation> dialogueConversations = new ArrayList<Conversation>();

        try {
            ConversationListAdapter adapter = new ConversationListAdapter(null, dialogueConversations);
            Assert.fail("Null context argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testNullItems() {
        try {
            ConversationListAdapter adapter = new ConversationListAdapter(getContext(), null);
            Assert.fail("Null list argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testItemsContainNull() {
        try {
            List<Conversation> dialogueConversations = new ArrayList<Conversation>();
            dialogueConversations.add(null);

            ConversationListAdapter adapter = new ConversationListAdapter(getContext(), dialogueConversations);
            Assert.fail("Null list argument not throwing");
        } catch (IllegalArgumentException e) {
            // Everything works fine
        }
    }
}
