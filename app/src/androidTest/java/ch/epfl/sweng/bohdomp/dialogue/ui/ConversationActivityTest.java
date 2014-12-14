package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.ChannelType;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.data.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationSettingsActivity;

import static ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage.MessageDirection;

/**
 * @author swengTeam 2013 BohDomp
 * Test for the ConversationActivity class
 */
public class ConversationActivityTest extends ActivityInstrumentationTestCase2<ConversationActivity> {
    private static final String CONTACT_NUMBER = "1234901534867889";
    private static final String MSG_BODY = "HELLO";
    private static final long TIMEOUT = 9000;

    private ConversationActivity mActivity;
    private Instrumentation mInstrumentation;

    private ListView mMessageList;
    private EditText mMessageContent;
    private Button mSendButton;

    private Contact mContact;
    private DialogueMessage mMessage;
    private Conversation mConversation;

    private int mConversationCountAtStart;

    public ConversationActivityTest() {
        super(ConversationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mInstrumentation = getInstrumentation();

        mConversationCountAtStart = DefaultDialogData.getInstance().getConversations().size();

        mContact = new ContactFactory(mInstrumentation.getTargetContext()).contactFromNumber(CONTACT_NUMBER);
        mMessage = new DialogueTextMessage(mContact, null, null, MSG_BODY, MessageDirection.INCOMING);

        mConversation = DefaultDialogData.getInstance().createOrGetConversation(mContact);

        mConversation.setChannel(ChannelType.SMS);
        mConversation.setPhoneNumber(mContact.getPhoneNumbers().iterator().next());

        Intent intent = new Intent(getInstrumentation().getTargetContext(), ConversationActivity.class);
        intent.putExtra(DialogueConversation.CONVERSATION_ID, mConversation.getId());

        setActivityIntent(intent);
        mActivity = getActivity();

        mMessageList = (ListView) mActivity.findViewById(R.id.message_List);
        mMessageContent = (EditText) mActivity.findViewById(R.id.new_message_content);
        mSendButton = (Button) mActivity.findViewById(R.id.send_message_button);
    }

    @Override
    protected void tearDown() throws Exception {
        DefaultDialogData.getInstance().removeConversation(mConversation.getId());
        super.tearDown();
    }

    public void testSetup() {
        assertNotNull("Not setup correctly", DefaultDialogData.getInstance().getConversation(mConversation.getId()));

        assertNotNull(mMessageList);
        assertNotNull(mMessageContent);
        assertNotNull(mSendButton);
        assertEquals("Not setup correctly", mMessageList.getCount(), mConversation.getMessageCount());
    }

    public void testConversationSettings() {
        Instrumentation.ActivityMonitor monitor = mInstrumentation.addMonitor(
                ConversationSettingsActivity.class.getName(), null, false);

        Boolean b = mInstrumentation.invokeMenuActionSync(mActivity, R.id.action_settings, 0);

        if (!b) {
            fail("Something went wrong");
        }

        Activity activity = mInstrumentation.waitForMonitorWithTimeout(monitor, TIMEOUT);

        assertNotNull("2", activity);
        activity.finish();
        assertEquals("3", activity.getClass(), ConversationSettingsActivity.class);
    }
}
