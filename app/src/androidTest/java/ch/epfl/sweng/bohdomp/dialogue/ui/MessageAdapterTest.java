package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.ui.messages.MessagesAdapter;

/**
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.messages.MessagesAdapter}
 */
public class MessageAdapterTest extends AndroidTestCase {
    private MessagesAdapter mAdapter;

    private String mBodyText;
    private final long mID = 123L;

    public MessageAdapterTest() {
        super();
    }

    protected void setUp() throws Exception {
        super.setUp();

        mBodyText = "Hello";

        List<DialogueMessage> list = new ArrayList<DialogueMessage>();

        mAdapter = new MessagesAdapter(getContext(), list);
    }

    public void testNullContext() {
        List<DialogueMessage> msgSet = new ArrayList<DialogueMessage>();

        try {
            new MessagesAdapter(null, msgSet);
            Assert.fail("Null context argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testNullItems() {
        try {
            new MessagesAdapter(getContext(), null);
            Assert.fail("Null list argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testItemsContainNull() {
        try {
            List<DialogueMessage> msgSet = new ArrayList<DialogueMessage>();
            msgSet.add(null);

            new MessagesAdapter(getContext(), msgSet);
            Assert.fail("Null list argument not throwing");
        } catch (IllegalArgumentException e) {
            // Everything works fine
        }
    }

    public void testParentNull() {
        try {
            mAdapter.getView(0, null, null);
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }
}
