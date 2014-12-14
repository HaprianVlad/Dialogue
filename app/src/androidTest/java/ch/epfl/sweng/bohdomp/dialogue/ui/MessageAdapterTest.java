package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.MessagesAdapter;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageDirection;

/**
 * @author swengTeam 2013 BohDomp
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.conversation.MessagesAdapter}
 */
public class MessageAdapterTest extends MockTestCase {
    private static final String CONTACT_NUMBER = "9876";
    private static final String MSG_BODY = "HELLO";

    private MessagesAdapter mAdapter;
    private Context mContext;

    private Contact mContact;
    private DialogueMessage mMessageOutGoing;
    private DialogueMessage mMessageIncomming;


    private List<DialogueMessage> mList;


    public MessageAdapterTest() {
        super();
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext();

        mContact = new ContactFactory(mContext).contactFromNumber(CONTACT_NUMBER);

        mMessageOutGoing = new DialogueTextMessage(mContact, null, null, MSG_BODY,
                MessageDirection.OUTGOING);
        mMessageIncomming = new DialogueTextMessage(mContact, ChannelType.SMS,
                mContact.getPhoneNumbers().iterator().next(), MSG_BODY, MessageDirection.INCOMING);
        mMessageIncomming.setStatus(DialogueMessage.MessageStatus.IN_TRANSIT);

        mList = new ArrayList<DialogueMessage>();
        mList.add(mMessageOutGoing);
        mList.add(mMessageIncomming);

        mAdapter = new MessagesAdapter(mContext, mList);
    }

    public void testUpdateDataNull() {
        try {
            mAdapter.updateData(null);
            fail("Null context argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testUpdateDataContainsNull() {
        try {
            List<DialogueMessage> msgList = new ArrayList<DialogueMessage>();
            msgList.add(null);
            mAdapter.updateData(msgList);
            fail("Null context argument not throwing");
        } catch (IllegalArgumentException e) {
            // Everything works fine
        }
    }

    public void testUpdateData() {
        List<DialogueMessage> msgList= new ArrayList<DialogueMessage>();
        msgList.add(mMessageOutGoing);
        msgList.add(mMessageIncomming);

        mAdapter.updateData(msgList);

        assertEquals(msgList.size(), mAdapter.getCount());
        assertEquals(msgList.get(0), mAdapter.getItem(0));
        assertEquals(msgList.get(1), mAdapter.getItem(1));
    }

    public void testNullContext() {
        List<DialogueMessage> msgSet = new ArrayList<DialogueMessage>();

        try {
            new MessagesAdapter(null, msgSet);
            fail("Null context argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testNullItems() {
        try {
            new MessagesAdapter(mContext, null);
            fail("Null list argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testItemsContainNull() {
        try {
            List<DialogueMessage> msgSet = new ArrayList<DialogueMessage>();
            msgSet.add(null);

            new MessagesAdapter(mContext, msgSet);
            fail("Null list argument not throwing");
        } catch (IllegalArgumentException e) {
            // Everything works fine
        }
    }

    public void testGetViewParentNull() {
        try {
            mAdapter.getView(0, null, null);
            fail("Parent null");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testGetViewInvalidPosition() {
        try {
            mAdapter.getItem(-1);
            ViewGroup parent = new LinearLayout(mContext);
            View viewInit = mAdapter.getView(-1, null, parent);
            fail("Invalid Position");
        } catch (IndexOutOfBoundsException e) {
            // Everything works fine
        }
    }

    public void testGetCount() {
        assertEquals("Count", mList.size(), mAdapter.getCount());
    }

    public void testGetItemInvalidPosition() {
        try {
            mAdapter.getItem(-1);
            fail("Invalid Position");
        } catch (IndexOutOfBoundsException e) {
            // Everything works fine
        }
    }

    public void testGetItem() {
        final int position = 0;
        assertEquals("Item", mList.get(position), mAdapter.getItem(position));
    }

    public void testGetItemIdInvalidPosition() {
        try {
            mAdapter.getItemId(-1);
            fail("Invalid Position");
        } catch (IndexOutOfBoundsException e) {
            // Everything works fine
        }
    }

    public void testGetItemId() {
        final int position = 0;
        assertEquals("Item", mList.get(position).getId().getLong(),
                mAdapter.getItemId(position));

        getInstrumentation().waitForIdleSync();
    }

    public void testGetView() {
        ViewGroup parent = new LinearLayout(mContext);
        assertTrue(mAdapter.getCount() >= 2);
        View viewInitOne = mAdapter.getView(0, null, parent);
        View viewInitTwo = mAdapter.getView(1, null, parent);

        assertNotNull("View", viewInitOne);
        assertNotNull("View", viewInitTwo);

        TextView bodyOne = (TextView) viewInitOne.findViewById(R.id.body);
        TextView timeStampOne = (TextView) viewInitOne.findViewById(R.id.header);

        TextView bodyTwo = (TextView) viewInitTwo.findViewById(R.id.body);
        TextView timeStampTwo = (TextView) viewInitTwo.findViewById(R.id.header);

        assertNotNull("Body", bodyOne);
        assertNotNull("Timestamp", timeStampOne);
        assertNotNull("Body", bodyTwo);
        assertNotNull("Timestamp", timeStampTwo);

        assertEquals("Body not equals", bodyOne.getText().toString(), MSG_BODY);
        assertEquals("Body not equals", bodyTwo.getText().toString(), MSG_BODY);

        assertEquals(mList.get(0).prettyTimeStamp(mContext), timeStampOne.getText());
        assertEquals(mList.get(1).prettyTimeStamp(mContext), timeStampTwo.getText());

        getInstrumentation().waitForIdleSync();

    }
}
