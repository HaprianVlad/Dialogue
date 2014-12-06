package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.PhoneNumber;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.testing.MockTestCase;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListAdapter;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageDirection;

/**
 * @author swengTeam 2013 BohDomp
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListAdapter}
 */
public class ConversationListAdapterTest extends MockTestCase {
    private static final String CONTACT_NUMBER = "9876";
    private static final String MSG_BODY = "HELLO";

    private Context mContext;

    private List<Contact> mContactList;
    private List<Conversation> mConversationList;

    private ConversationListAdapter mAdapter;

    public ConversationListAdapterTest() {
        super();
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mContext = getInstrumentation().getTargetContext();

        mContactList = new ArrayList<Contact>();
        Contact contact = new ContactFactory(mContext).contactFromNumber(CONTACT_NUMBER);
        mContactList.add(contact);

        PhoneNumber number = mContactList.get(0).getPhoneNumbers().iterator().next();
        ChannelType channel = ChannelType.SMS;

        mConversationList = new ArrayList<Conversation>();
        mConversationList.add(new DialogueConversation(mContactList));
        mConversationList.get(0).addMessage(new DialogueTextMessage(mContactList.get(0), channel, number, "HEllO",
                MessageDirection.OUTGOING));


        mAdapter = new ConversationListAdapter(mContext, mConversationList);
    }

    public void testUpdateDataNull() {
        try {
            mAdapter.update(null);
            fail("Null context argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testUpdateDataContainsNull() {
        try {
            mConversationList.add(null);
            mAdapter.update(mConversationList);
            fail("Null context argument not throwing");
        } catch (IllegalArgumentException e) {
            // Everything works fine
        }
    }

    public void testUpdateData() {
        mConversationList.add(new DialogueConversation(mContactList));

        mAdapter.update(mConversationList);

        assertEquals(mConversationList.size(), mAdapter.getCount());
        assertEquals(mConversationList.get(0), mAdapter.getItem(0));
        assertEquals(mConversationList.get(1), mAdapter.getItem(1));
    }

    public void testNullContext() {
        try {
            new ConversationListAdapter(null, mConversationList);
            fail("Null context argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testNullItems() {
        try {
            new ConversationListAdapter(mContext, null);
            fail("Null list argument not throwing");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testItemsContainNull() {
        try {
            mConversationList.add(null);
            new ConversationListAdapter(mContext, mConversationList);
            fail("Null list argument not throwing");
        } catch (IllegalArgumentException e) {
            // Everything works fine
        }
    }

    public void testParentNull() {
        try {
            mAdapter.getView(0, null, null);
            fail("Parent null");
        } catch (NullArgumentException e) {
            // Everything works fine
        }
    }

    public void testGetCount() {
        assertEquals("Count", mAdapter.getCount(), mConversationList.size());
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
        assertEquals("Item", mConversationList.get(position), mAdapter.getItem(position));
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
        assertEquals("Item", mConversationList.get(position).getId().getLong(),
                mAdapter.getItemId(position));
    }

    public void testGetView() {
        ViewGroup parent = new LinearLayout(mContext);
        View viewInit = mAdapter.getView(0, null, parent);

        assertNotNull("View", viewInit);

        ImageView contactPicture = (ImageView) viewInit.findViewById(R.id.contactPicture);
        TextView contactName = (TextView)  viewInit.findViewById(R.id.contactName);
        TextView contactChannels = (TextView)  viewInit.findViewById(R.id.contactChannels);
        TextView lastMessage = (TextView)  viewInit.findViewById(R.id.contactLastMessage);
        TextView unRead = (TextView)  viewInit.findViewById(R.id.unReadDot);

        assertNotNull("Picutre", contactPicture);
        assertNotNull("Name", contactName);
        assertNotNull("Channels", contactChannels);
        assertNotNull("Last", lastMessage);
        assertNotNull("Unread", unRead); 
    }
}
