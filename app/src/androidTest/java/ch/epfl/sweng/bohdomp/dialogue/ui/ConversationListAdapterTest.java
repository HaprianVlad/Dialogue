package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.test.AndroidTestCase;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ui.contactList.ConversationListAdapter;

/**
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.contactList.ConversationListAdapter}
 */
public class ConversationListAdapterTest extends AndroidTestCase {


    private ConversationListAdapter mAdapter;
    private List<Conversation> mConversations;

    public ConversationListAdapterTest() {
        super();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        mConversations  = new ArrayList<Conversation>();
        mAdapter = new ConversationListAdapter(getContext(), mConversations);
    }

    public void testNullContext() {

        try {
            mAdapter = new ConversationListAdapter(null, mConversations);
        }

            fail("Null context argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testNullItems() {
        try {
            mAdapter = new ConversationListAdapter(getContext(), null);

            fail("Null list argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testItemsContainNull() {
        try {
            mConversations.add(null);
            mAdapter =  new ConversationListAdapter(getContext(), mConversations);

            ConversationListAdapter adapter = new ConversationListAdapter(getContext(), mConversations);

            fail("Null list argument not throwing");
        } catch (IllegalArgumentException e) {
            // Everything works fine
        }
    }
}
