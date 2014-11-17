package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.ui.contactList.ConversationListActivity;
import ch.epfl.sweng.bohdomp.dialogue.ui.newConversation.NewConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.contactList.ConversationListActivity}
 */
public class ConversationListActivityTest extends ActivityInstrumentationTestCase2<ConversationListActivity> {
    private static final int TIMEOUT = 1500;

    private ConversationListActivity mActivity;
    private Instrumentation mInstrumentation;

    private ListView mContactListView;
    private LinearLayout mDefaultAppWarningLayout;
    private Button mChangeDefaultAppButton;

    private Contact mContact;
    private Conversation mConversation;

    public ConversationListActivityTest() {
        super(ConversationListActivity.class);
    }

    protected void setUp() throws Exception {
        super.setUp();

        mInstrumentation = getInstrumentation();

        mContact = new ContactFactory(mInstrumentation.getTargetContext()).contactFromNumber("1234");
        mConversation = DefaultDialogData.getInstance().createOrGetConversation(mContact);

        mActivity = getActivity();

        mChangeDefaultAppButton = (Button) mActivity.findViewById(R.id.setDefaultAppButton);
        mContactListView = (ListView) mActivity.findViewById(R.id.listConversationsView);
        mDefaultAppWarningLayout = (LinearLayout) mActivity.findViewById(R.id.notDefaultWarning);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testLayoutSetup() {
        assertNotNull(mChangeDefaultAppButton);
        assertNotNull(mContactListView);
    }

    public void testNotDefaultApp() {
        assertEquals(mDefaultAppWarningLayout.getVisibility(), View.VISIBLE);
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

    /*

    public void testClickOnConversation() {

        Instrumentation.ActivityMonitor monitor = getInstrumentation().addMonitor(
                ConversationActivity.class.getName(), null, false);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                int position = 0;

                ((ListView) getActivity().findViewById(R.id.listConversationsView))
                        .performItemClick(mContactListView.getChildAt(position),
                        position,
                        mContactListView.getAdapter().getItemId(position));
            }
        });

        final int times = 3;
        Activity activity = getInstrumentation().waitForMonitorWithTimeout(monitor, TIMEOUT*times);
        activity.finish();

        // Check if send to right activity
        assertNotNull("Activity is null", activity);
        assertEquals("Send to the wrong activity", activity.getClass(), ConversationActivity.class);

        // Check content of the intent
        // Check intent contains id
        Intent intent = activity.getIntent();

        Bundle b = intent.getExtras();
        ConversationId id = (ConversationId) b.get(DialogueConversation.CONVERSATION_ID);
        assertNotNull("Intent has no ID", id);
    }

    */
}
