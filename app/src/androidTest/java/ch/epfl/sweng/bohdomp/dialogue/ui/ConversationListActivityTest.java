package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.sweng.bohdomp.dialogue.R;
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

    public ConversationListActivityTest() {
        super(ConversationListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mInstrumentation = getInstrumentation();

        mActivity = getActivity();
    }

    public void testNewConversationClick() {
        Instrumentation.ActivityMonitor monitor = mInstrumentation.addMonitor(
                NewConversationActivity.class.getName(), null, false);

        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.openOptionsMenu();
                assertTrue(mActivity.findViewById(R.id.action_addConversation).isClickable());
                mActivity.findViewById(R.id.action_addConversation).callOnClick();
            }
        });

        Activity activity = mInstrumentation.waitForMonitorWithTimeout(monitor, TIMEOUT);
        activity.finish();

        assertNotNull(activity);
        assertEquals(activity.getClass(), NewConversationActivity.class);
        assertTrue(mInstrumentation.checkMonitorHit(monitor, 1));
    }
}
