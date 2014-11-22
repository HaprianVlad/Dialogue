package ch.epfl.sweng.bohdomp.dialogue.ui.conversationList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Telephony;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueDataListener;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.ui.newConversation.NewConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * @author swengTeam 2013 BohDomp
 * Activity displaying the set of conversation
 */
public class ConversationListActivity extends Activity implements DialogueDataListener{
    private final static String LOG_TAG = "ConversationListActivity";
    private final static String APP_DATA = "APP_DATA";

    private ListView mContactListView;
    private LinearLayout mDefaultAppWarningLayout;
    private Button mChangeDefaultAppButton;

    private String myPackageName;

    private List<Conversation> mConversationList;
    private ConversationListAdapter mConversationItemListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);
        DefaultDialogData.getInstance().addListener(this);

        initData();
        setViewElements();
        setupListener();
    }

    /*
     * Initialize the data used by the activity
     */
    private void initData() {
        myPackageName = getPackageName();
        mConversationList = DefaultDialogData.getInstance().getConversations();
        mConversationItemListAdapter = new ConversationListAdapter(this, mConversationList);
    }

    @Override
    public void onDialogueDataChanged() {
        Log.i("DialogueOutgoingDispatcher", "2");
        mConversationList = DefaultDialogData.getInstance().getConversations();

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mConversationItemListAdapter.update(mConversationList);
            }
        });
    }

    /*
     * Set all view elements
     */
    private void setViewElements() {
        this.setTitle(getString(R.string.conversationListTitle));
        mDefaultAppWarningLayout = (LinearLayout) findViewById(R.id.notDefaultWarning);
        checkDefaultApp();

        mChangeDefaultAppButton = (Button) findViewById(R.id.setDefaultAppButton);
        mContactListView = (ListView) findViewById(R.id.listConversationsView);
        mContactListView.setAdapter(mConversationItemListAdapter);
    }

    /**
     * Check if Dialogue is the default sms app, given the result it display or not a warning
     */
    private void checkDefaultApp() {
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
            mDefaultAppWarningLayout.setVisibility(View.VISIBLE);
        }  else {
            mDefaultAppWarningLayout.setVisibility(View.GONE);
        }
    }

    /**
     * Setup all listener related to the view displayed by the activity
     */
    private void setupListener() {

        mChangeDefaultAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                startActivity(intent);
            }
        });

        mContactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Contract.throwIfArgNull(parent, "parent");
                Contract.throwIfArgNull(view, "view");

                ListView listView = (ListView) parent;
                DialogueConversation c = (DialogueConversation) listView.getItemAtPosition(position);

                Intent intent = new Intent(view.getContext(), ConversationActivity.class);
                intent.putExtra(DialogueConversation.CONVERSATION_ID, c.getId());

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        checkDefaultApp();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present
        getMenuInflater().inflate(R.menu.list_conversations, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return id == R.id.action_settings || super.onOptionsItemSelected(item);
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Contract.throwIfArgNull(savedInstanceState, "savedInstanceState");

        Bundle b =DefaultDialogData.getInstance().createBundle();
        savedInstanceState.putBundle(APP_DATA, b);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Contract.throwIfArgNull(savedInstanceState, "savedInstanceState");

        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        DefaultDialogData.getInstance().restoreFromBundle(savedInstanceState.getBundle(APP_DATA));
    }

    @Override
    protected void onStop() {
        DefaultDialogData.getInstance().removeListener(this);
        super.onStop();
    }


    /**
     * Method called when the new conversation is clicked
     * Start the "new message activity"
     */
    public void newConversationClicked(MenuItem item) {
        Intent intent = new Intent(this, NewConversationActivity.class);
        startActivity(intent);
    }

}
