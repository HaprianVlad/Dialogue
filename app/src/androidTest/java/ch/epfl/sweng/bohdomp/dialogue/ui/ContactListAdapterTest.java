package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ui.contactList.ContactListAdapter;

/**
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.contactList.ContactListAdapter}
 */
public class ContactListAdapterTest  extends AndroidTestCase {

    public ContactListAdapterTest() {
        super();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    public void testNullContext() {
        List<Conversation> dialogueConversations = new ArrayList<Conversation>();

        try {
            ContactListAdapter adapter = new ContactListAdapter(null, dialogueConversations);
            Assert.fail("Null context argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testNullItems() {
        try {
            ContactListAdapter adapter = new ContactListAdapter(getContext(), null);
            Assert.fail("Null list argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testItemsContainNull() {
        try {
            List<Conversation> dialogueConversations = new ArrayList<Conversation>();
            dialogueConversations.add(null);

            ContactListAdapter adapter = new ContactListAdapter(getContext(), dialogueConversations);
            Assert.fail("Null list argument not throwing");
        } catch (IllegalArgumentException e) {
            // Everything works fine
        }
    }
}
