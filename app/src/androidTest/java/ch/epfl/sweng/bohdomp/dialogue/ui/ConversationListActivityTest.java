package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListActivity;
import ch.epfl.sweng.bohdomp.dialogue.ui.newConversation.NewConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListActivity}
 */
public class ConversationListActivityTest extends ActivityInstrumentationTestCase2<ConversationListActivity> {
    private static final int TIMEOUT = 3000;

    private ConversationListActivity mActivity;
    private Instrumentation mInstrumentation;

    private int mConversationCountAtStart;

    public ConversationListActivityTest() {
        super(ConversationListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mConversationCountAtStart = DefaultDialogData.getInstance().getConversations().size();

        mInstrumentation = getInstrumentation();

        mActivity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        assertEquals("Not reset", mConversationCountAtStart, DefaultDialogData.getInstance().getConversations().size());
        super.tearDown();
    }

    public void testNewConversationClick() {
        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(
                NewConversationActivity.class.getName(), null, false);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.openOptionsMenu();
                mActivity.findViewById(R.id.action_addConversation).performClick();
            }
        });

        mInstrumentation.waitForIdleSync();

        Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitor, TIMEOUT);

        // Check if send to right activity
        assertNotNull(activity);
        assertEquals(activity.getClass(), NewConversationActivity.class);
        assertTrue(getInstrumentation().checkMonitorHit(monitor, 1));

        activity.finish();
    }
}
