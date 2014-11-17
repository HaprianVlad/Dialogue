package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.ids.IdManager;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.ui.messages.ConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for the ConversationActivity class
 */
public class ConversationActivityTest extends ActivityInstrumentationTestCase2<ConversationActivity> {
    private static final String CONTACT_NUMBER = "1234567890";
    private static final String MSG_BODY = "HELLO";

    private ConversationActivity mActivity;
    private Instrumentation mInstrumentation;

    private ListView mMessageList;
    private EditText mMessageContent;
    private Button mSendButton;

    private Contact mContact;
    private DialogueMessage mMessage;
    private Conversation mConversation;

    public ConversationActivityTest() {
        super(ConversationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();

        mContact = new ContactFactory(mInstrumentation.getTargetContext()).contactFromNumber(CONTACT_NUMBER);
        mMessage = new DialogueTextMessage(mContact, MSG_BODY, DialogueMessage.MessageStatus.INCOMING);

        mConversation = DefaultDialogData.getInstance().createOrGetConversation(mContact);

        Intent intent = new Intent(getInstrumentation().getTargetContext(), ConversationActivity.class);
        intent.putExtra(DialogueConversation.CONVERSATION_ID, mConversation.getId());

        setActivityIntent(intent);
        mActivity = getActivity();

        mMessageList = (ListView) mActivity.findViewById(R.id.message_List);
        mMessageContent = (EditText) mActivity.findViewById(R.id.new_message_content);
        mSendButton = (Button) mActivity.findViewById(R.id.send_message_button);
    }

    protected void tearDown() throws Exception {
        DefaultDialogData.getInstance().removeConversation(mConversation.getId());
        assertNull(DefaultDialogData.getInstance().getConversation(mConversation.getId()));
        super.tearDown();
    }

    public void testActivityTitle() {
        assertEquals(mActivity.getTitle().toString(), mConversation.getName());
    }

    public void testSendButtonText() {
        assertEquals(mSendButton.getText().toString(), mActivity.getString(R.string.send_message_button));
    }

    public void testSendButtonNotEnableAtStart() {
        assertFalse(mSendButton.isEnabled());
    }

    public void testSendButton() {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageContent.setText(MSG_BODY);
            }
        });

        mInstrumentation.waitForIdleSync();
        assertTrue(mSendButton.isEnabled());
    }

    public void testSendButtonEmptyText() {
        final String msgBody = "";
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageContent.setText(msgBody);

            }
        });
        mInstrumentation.waitForIdleSync();
        assertFalse(mSendButton.isEnabled());
    }

    public void testChangeInConversation() {
        int count = mConversation.getMessageCount();
        assertEquals(count, mMessageList.getAdapter().getCount());

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConversation.addMessage(mMessage);
            }
        });

        mInstrumentation.waitForIdleSync();
        assertEquals(count + 1, mMessageList.getAdapter().getCount());
    }

    public void testChangeInConversationNull() {
        assertEquals(mMessageList.getAdapter().getCount(), mConversation.getMessageCount());
        try {
            mActivity.onConversationChanged(null);
            fail();
        } catch (NullArgumentException e) {
        }
    }

    public void testChangeInConversationIllegalId() {
        assertEquals(mMessageList.getAdapter().getCount(), mConversation.getMessageCount());
        try {
            mActivity.onConversationChanged(IdManager.getInstance().newConversationId());
            fail();
        } catch (IllegalStateException e) {
        }
    }

    public void testSendSms() {
        int count = mConversation.getMessageCount();

        assertEquals(mMessageList.getAdapter().getCount(), count);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageContent.setText(MSG_BODY);
                mSendButton.performClick();
            }
        });

        mInstrumentation.waitForIdleSync();

        assertEquals(mMessageList.getAdapter().getCount(), count + 1);

        DialogueMessage msg = (DialogueMessage) mMessageList.getAdapter().getItem(0);

        assertEquals(msg.getBody().getMessageBody(), MSG_BODY);
        assertEquals(msg.getContact(), mContact);
    }
}
