package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ui.messages.ConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.ui.newConversation.NewConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for the NewConversationActivity class
 */
public class NewConversationActivityTest extends ActivityInstrumentationTestCase2<NewConversationActivity> {
    private static final int TIMEOUT = 1500;
    private static String phoneNumber = "1234567";

    protected NewConversationActivity mActivity;

    protected Instrumentation mInstrumentation;

    private EditText mEditText;
    private Button mSendButton;

    public NewConversationActivityTest() {
        super(NewConversationActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();
        mInstrumentation = getInstrumentation();

        mActivity = getActivity();
        mEditText = (EditText) mActivity.findViewById(R.id.message_to);
        mSendButton = (Button) mActivity.findViewById(R.id.create_conversation_button);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
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

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText(phoneNumber);
            }
        });

        mInstrumentation.waitForIdleSync();
        assertTrue(mSendButton.isEnabled());
    }

    public void testEditTextWrongInput() {
        phoneNumber = "---";

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText(phoneNumber);

            }
        });

        mInstrumentation.waitForIdleSync();
        assertFalse(mSendButton.isEnabled());
    }

    public void testEditTextNoInput() {
        phoneNumber = "";

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText(phoneNumber);
            }
        });

        mInstrumentation.waitForIdleSync();
        assertFalse(mSendButton.isEnabled());
    }

    public void testEditWrongInputEnableButton() {
        phoneNumber = "-+-12";

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText(phoneNumber);
                mSendButton.setEnabled(true);
                mSendButton.performClick();
            }
        });

        mInstrumentation.waitForIdleSync();
        assertFalse(mSendButton.isEnabled());
    }

    public void testIntent() {
        phoneNumber = "12345";

        assertFalse(mSendButton.isEnabled());

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(
                ConversationActivity.class.getName(), null, false);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mEditText.setText(phoneNumber);
                mSendButton.performClick();
            }
        });

        Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitor, TIMEOUT);

        // Check if send to right activity
        assertNotNull(activity);
        assertEquals(activity.getClass(), ConversationActivity.class);
        assertTrue(getInstrumentation().checkMonitorHit(monitor, 1));

        // Check intent contains id
        Intent intent = activity.getIntent();
        activity.finish();

        Bundle b = intent.getExtras();
        ConversationId id = (ConversationId) b.get(DialogueConversation.CONVERSATION_ID);
        assertNotNull(id);
    }
}
