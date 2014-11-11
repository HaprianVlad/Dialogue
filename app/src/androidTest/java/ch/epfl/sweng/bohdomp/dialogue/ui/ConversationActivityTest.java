package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.ui.messages.ConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.utils.SystemTimeProvider;

/**
 * @author swengTeam 2013 BohDomp
 * Test for the ConversationActivity class
 */
public class ConversationActivityTest extends android.test.ActivityUnitTestCase<ConversationActivity> {
    private static final String CONTACT_NUMBER = "1234567890";
    private static final String MSG_BODY = "HELLO";

    private Context mContext;

    private ConversationActivity activity;
    private int mMessageListId;
    private int mNewMessageTextId;
    private int mSendButtonId;

    private Contact mContact;
    private DialogueMessage mMessage;
    private List<Contact> mContactList;
    private Conversation mConversation;


    public ConversationActivityTest(Class<ConversationActivity> activityClass) {
        super(activityClass);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mContext = getInstrumentation().getTargetContext();

        mContact = new ContactFactory(mContext).contactFromNumber(CONTACT_NUMBER);
        mMessage = new DialogueTextMessage(mContact, MSG_BODY, DialogueMessage.MessageStatus.INCOMING);

        mContactList = new ArrayList<Contact>();
        mContactList.add(mContact);

        mConversation = new DialogueConversation(mContactList, new SystemTimeProvider());
        mConversation.addMessage(mMessage);

        Intent intent = new Intent(getInstrumentation().getTargetContext(), ConversationActivity.class);
        intent.putExtra(DialogueConversation.CONVERSATION_ID, mConversation.getId());

        startActivity(intent, null, null);

        activity = getActivity();
    }

    public void testActivityTitle() {
        assertEquals(activity.getTitle().toString(), mConversation.getName());
    }

    public void testLayout() {
        mMessageListId = R.id.message_List;
        mNewMessageTextId = R.id.new_message_content;
        mSendButtonId = R.id.send_message_button;

        assertNotNull(activity.findViewById(mMessageListId));
        assertNotNull(activity.findViewById(mNewMessageTextId));
        assertNotNull(activity.findViewById(mSendButtonId));
    }
}
