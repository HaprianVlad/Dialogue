package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityUnitTestCase;

import org.mockito.Mockito;

import java.util.HashSet;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.data.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationSettingsActivity;

/**
 * Created by yoannponti on 09/12/14.
 */
public class ConversationSettingsActivityTest extends ActivityUnitTestCase<ConversationSettingsActivity> {
    private static final String CONTACT_NAME = "ContactName";
    private static final String CONTACT_NUMBER = "12345667889";

    private Activity mActivity;
    private Instrumentation mInstrumentation;

    private Contact mContact;
    private Conversation mConversation;


    public ConversationSettingsActivityTest() {
        super(ConversationSettingsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mInstrumentation = getInstrumentation();

        hackMockito();
        mockContact();

        mConversation = DefaultDialogData.getInstance().createOrGetConversation(mContact);

        Intent intent = new Intent(getInstrumentation().getTargetContext(), ConversationActivity.class);
        intent.putExtra(DialogueConversation.CONVERSATION_ID, mConversation.getId());

        startActivity(intent, null, null);
        mActivity = getActivity();
    }

    private void hackMockito() {
        System.setProperty("dexmaker.dexcache", mInstrumentation.getTargetContext().getCacheDir().getPath());
    }

    private void mockContact() {
        Set<String> phoneNumbers= new HashSet<String>();
        phoneNumbers.add(CONTACT_NUMBER);

        mContact = Mockito.mock(Contact.class);

        Mockito.doReturn(CONTACT_NAME).when(mContact).getDisplayName();
        Mockito.doReturn(phoneNumbers).when(mContact).getPhoneNumbers();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testSetup() {
        assertNotNull("Not setup correctly", DefaultDialogData.getInstance().getConversation(mConversation.getId()));
        assertEquals("Not setup correctly", 0, mConversation.getMessageCount());
    }
}
