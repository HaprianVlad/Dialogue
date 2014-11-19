package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Instrumentation;
import android.content.Intent;
import android.database.DataSetObserver;
import android.test.ActivityUnitTestCase;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.UnknownContact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for the ConversationActivity class
 */
public class ConversationActivityUnitTest extends ActivityUnitTestCase<ConversationActivity> {
    private static final String CONTACT_NAME = "ContactName";
    private static final String CONTACT_NUMBER = "12345667889";

    private static final String MSG_BODY = "HELLO";


    private ConversationActivity mActivity;
    private Instrumentation mInstrumentation;

    private ListView mMessageList;
    private EditText mMessageContent;
    private Button mSendButton;

    private Contact mContact;
    private Conversation mConversation;

    private int mConversationCount;

    public ConversationActivityUnitTest() {
        super(ConversationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mConversationCount = DefaultDialogData.getInstance().getConversations().size();

        mInstrumentation = getInstrumentation();

        hackMockito();
        mockContact();

        mConversation = DefaultDialogData.getInstance().createOrGetConversation(mContact);

        Intent intent = new Intent(getInstrumentation().getTargetContext(), ConversationActivity.class);
        intent.putExtra(DialogueConversation.CONVERSATION_ID, mConversation.getId());

        startActivity(intent, null, null);
        mActivity = getActivity();

        mMessageList = (ListView) mActivity.findViewById(R.id.message_List);
        mMessageContent = (EditText) mActivity.findViewById(R.id.new_message_content);
        mSendButton = (Button) mActivity.findViewById(R.id.send_message_button);
    }

    @Override
    protected void tearDown() throws Exception {
        DefaultDialogData.getInstance().removeConversation(mConversation.getId());
        assertEquals("Not reset", mConversationCount, DefaultDialogData.getInstance().getConversations().size());
        super.tearDown();
    }

    private void hackMockito() {
        System.setProperty("dexmaker.dexcache", mInstrumentation.getTargetContext().getCacheDir().getPath());
    }

    private void mockContact() {
        Set<String> phoneNumbers= new HashSet<String>();
        phoneNumbers.add(CONTACT_NUMBER);

        mContact = Mockito.mock(UnknownContact.class);

        Mockito.doReturn(CONTACT_NAME).when(mContact).getDisplayName();
        Mockito.doReturn(phoneNumbers).when(mContact).getPhoneNumbers();
    }

    public void testSetup() {
        assertNotNull("Not setup correctly", DefaultDialogData.getInstance().getConversation(mConversation.getId()));
        assertEquals("Not setup correctly", 0, mConversation.getMessageCount());
        assertNotNull(mMessageList);
        assertNotNull(mMessageContent);
        assertNotNull(mSendButton);
    }

    public void testActivityTitle() {
        assertEquals(mActivity.getTitle().toString(), CONTACT_NAME);
    }

    public void testSendButtonText() {
        assertEquals(mSendButton.getText().toString(), mActivity.getString(R.string.send_message_button));
    }

    public void testSendButtonNotEnableAtStart() {
        assertFalse(mSendButton.isEnabled());
    }

    public void testSendButton() {
        mMessageContent.setText(MSG_BODY);
        assertTrue(mSendButton.isEnabled());
    }

    public void testSendButtonEmptyText() {
        final String msgBody = "";
        mMessageContent.setText(msgBody);
        assertFalse(mSendButton.isEnabled());
    }

    public void testChangeInConversationNull() {
        assertEquals(mMessageList.getAdapter().getCount(), mConversation.getMessageCount());
        try {
            mActivity.onConversationChanged(null);
            fail("Exception should have been thrown");
        } catch (NullArgumentException e) {
        }
    }

    public void testChangeInConversationIllegalId() {
        assertEquals(mMessageList.getAdapter().getCount(), mConversation.getMessageCount());
        try {
            mActivity.onConversationChanged(IdManager.getInstance().newConversationId());
            fail("Exception should have been thrown");
        } catch (IllegalStateException e) {
        }
    }

    public void testChangeInConversationNotifyAdapter() {
        DataSetObserver observer = Mockito.mock(DataSetObserver.class);

        Adapter adapter = mMessageList.getAdapter();
        adapter.registerDataSetObserver(observer);

        mActivity.onConversationChanged(mConversation.getId());

        Mockito.verify(observer, Mockito.times(1)).onChanged();
    }
}