package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListActivity;
import ch.epfl.sweng.bohdomp.dialogue.ui.newConversation.NewConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for the NewConversationActivity class
 */
public class NewConversationActivityTest extends ActivityInstrumentationTestCase2<NewConversationActivity> {
    protected NewConversationActivity mActivity;

    private int mConversationCountAtStart;

    public NewConversationActivityTest() {
        super(NewConversationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mConversationCountAtStart = DefaultDialogData.getInstance().getConversations().size();
        mActivity = getActivity();
    }

    @Override
    protected void tearDown() throws Exception {
        assertEquals("Not reset", mConversationCountAtStart, DefaultDialogData.getInstance().getConversations().size());
        super.tearDown();
    }

    public void testActivityHasParent() {
        Intent intent = mActivity.getParentActivityIntent();
        assertEquals(intent.getComponent().getClassName(), ConversationListActivity.class.getName());
    }
}
