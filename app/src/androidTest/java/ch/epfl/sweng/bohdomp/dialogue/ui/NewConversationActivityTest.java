package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListActivity;
import ch.epfl.sweng.bohdomp.dialogue.ui.newConversation.NewConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for the NewConversationActivity class
 */
public class NewConversationActivityTest extends ActivityInstrumentationTestCase2<NewConversationActivity> {

    public NewConversationActivityTest() {
        super(NewConversationActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        setActivityIntent(null);
    }

    public void testActivityHasParent() {
        Intent intent = getActivity().getParentActivityIntent();
        assertEquals(intent.getComponent().getClassName(), ConversationListActivity.class.getName());
    }
}
