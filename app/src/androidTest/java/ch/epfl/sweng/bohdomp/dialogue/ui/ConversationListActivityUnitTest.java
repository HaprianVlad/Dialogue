package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Instrumentation;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.test.ActivityUnitTestCase;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.ListView;

import org.mockito.Mockito;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Test for {@link ch.epfl.sweng.bohdomp.dialogue.ui.conversationList.ConversationListActivity}
 */
public class ConversationListActivityUnitTest extends ActivityUnitTestCase<ConversationListActivity> {
    private ConversationListActivity mActivity;
    private Instrumentation mInstrumentation;

    private ListView mContactListView;
    private Button mChangeDefaultAppButton;

    private int mConversationCountAtStart;

    public ConversationListActivityUnitTest() {
        super(ConversationListActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mConversationCountAtStart = DefaultDialogData.getInstance().getConversations().size();

        mInstrumentation = getInstrumentation();
        hackMockito();

        startActivity(new Intent(), null, null);
        mActivity = getActivity();

        mChangeDefaultAppButton = (Button) mActivity.findViewById(R.id.setDefaultAppButton);
        mContactListView = (ListView) mActivity.findViewById(R.id.listConversationsView);
    }

    private void hackMockito() {
        System.setProperty("dexmaker.dexcache", mInstrumentation.getTargetContext().getCacheDir().getPath());
    }

    @Override
    protected void tearDown() throws Exception {
        assertEquals("Not reset", mConversationCountAtStart, DefaultDialogData.getInstance().getConversations().size());
        super.tearDown();
    }

    public void testTitleName() {
        assertEquals(mActivity.getTitle().toString(), mActivity.getString(R.string.conversationListTitle));
    }

    public void testLayoutSetup() {
        assertNotNull(mChangeDefaultAppButton);
        assertNotNull(mContactListView);
    }

    public void testConDialogueDataChangedNotifyAdapter() {
        DataSetObserver observer = Mockito.mock(DataSetObserver.class);

        Adapter adapter = mContactListView.getAdapter();
        adapter.registerDataSetObserver(observer);

        mActivity.onDialogueDataChanged();

        Mockito.verify(observer, Mockito.times(1)).onChanged();
    }

    public void testClickOnConversation() {
        ListView list = (ListView) mActivity.findViewById(R.id.listConversationsView);

        assertEquals(mConversationCountAtStart, list.getAdapter().getCount());

        Contact contact = Mockito.mock(Contact.class);
        Conversation conversation = DefaultDialogData.getInstance().createOrGetConversation(contact);

        assertEquals(mConversationCountAtStart + 1, list.getAdapter().getCount());

        list.performItemClick(list, 0, 0);

        Intent intent = getStartedActivityIntent();
        assertNotNull(intent);

        Bundle b = intent.getExtras();
        ConversationId id = (ConversationId) b.get(DialogueConversation.CONVERSATION_ID);

        assertNotNull(id);

        assertEquals(id, conversation.getId());

        DefaultDialogData.getInstance().removeConversation(conversation.getId());
    }
}
