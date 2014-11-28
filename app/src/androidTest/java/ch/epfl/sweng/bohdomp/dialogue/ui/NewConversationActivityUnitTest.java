package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.widget.Button;
import android.widget.EditText;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.data.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ui.newConversation.NewConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for the NewConversationActivity class
 */
public class NewConversationActivityUnitTest extends ActivityUnitTestCase<NewConversationActivity> {
    private static String phoneNumber = "1234567";

    protected NewConversationActivity mActivity;

    protected Instrumentation mInstrumentation;

    private EditText mEditText;
    private Button mSendButton;
    private Button mSelectContact;


    private ConversationId mId;

    public NewConversationActivityUnitTest() {
        super(NewConversationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mInstrumentation = getInstrumentation();

        startActivity(new Intent(), null, null);

        mActivity = getActivity();
        mEditText = (EditText) mActivity.findViewById(R.id.message_to);
        mSendButton = (Button) mActivity.findViewById(R.id.create_conversation_button);
        mSelectContact = (Button) mActivity.findViewById(R.id.selectContact);


        mId = null;
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        mEditText.setText("");
        DefaultDialogData.getInstance().removeConversation(mId);
    }

    public void testTitle() {
        assertEquals(mActivity.getTitle().toString(),
                mActivity.getString(R.string.newConversationActivityTitle));
    }

    public void testSendButtonOnStartDisable() {
        assertFalse(mSendButton.isEnabled());
    }

    public void testEditTextGoodInput() {
        phoneNumber = "12345";
        mEditText.setText(phoneNumber);
        assertTrue(mSendButton.isEnabled());
    }

    public void testEditTextWrongInput() {
        phoneNumber = "---";
        mEditText.setText(phoneNumber);
        assertFalse(mSendButton.isEnabled());
    }

    public void testEditTextNoInput() {
        phoneNumber = "";
        mEditText.setText(phoneNumber);
        assertFalse(mSendButton.isEnabled());
    }

    public void testEditWrongInputEnableButton() {
        phoneNumber = "-+-12";

        mEditText.setText(phoneNumber);
        mSendButton.setEnabled(true);
        mSendButton.performClick();

        assertFalse(mSendButton.isEnabled());
    }

    public void testIntentCreate() {
        phoneNumber = "12345";

        assertFalse(mSendButton.isEnabled());

        mEditText.setText(phoneNumber);

        assertTrue(mSendButton.isEnabled());

        mSendButton.performClick();

        Intent intent = getStartedActivityIntent();

        assertNotNull(intent);

        Bundle b = intent.getExtras();
        mId = (ConversationId) b.get(DialogueConversation.CONVERSATION_ID);

        assertNotNull(mId);
    }

    public void testIntentSelect() {
        assertTrue(mSelectContact.isEnabled());
        mSelectContact.performClick();
        Intent intent = getStartedActivityIntent();
        assertNotNull(intent);
    }
}
