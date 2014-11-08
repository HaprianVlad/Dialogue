package ch.epfl.sweng.bohdomp.dialogue.ui.messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;

import static ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation.ConversationId;


/**
 * @author swengTeam 2013 BohDomp
 * Activity displaying a set of messages
 */
public class MessagesActivity extends Activity {
    private static final String LOG_TAG = "MessagesActivity";

    private ListView mMessageList;
    private EditText mNewMessageText;
    private Button mSendButton;

    private BaseAdapter mMessageItemListAdapter;

    private Conversation mConversation;
    private List<DialogueMessage> mMessages;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();

        try {
            ConversationId conversationID;
            conversationID = intent.getParcelableExtra(DialogueConversation.CONVERSATION_ID);
            Log.i("OSWALD", "Id transmitted: " + conversationID.getLong());


            initData(conversationID);
            setViewElement();
        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
    }


    /*
     * Initialize the data used by the activity
     */
    public void initData(ConversationId conversationId) {
        mConversation = DefaultDialogData.getInstance().getConversation(conversationId);

        if (mConversation != null) {
            mMessages = mConversation.getConversationMessages();
            mMessageItemListAdapter = new MessagesAdapter(this, mMessages);
        } else {
            throw new NullPointerException("Conversation is Null");
        }
    }


    /*
     * Set all view elements
     */
    private void setViewElement() {

        mMessageList = (ListView) findViewById(R.id.message_List);
        mMessageList.setAdapter(mMessageItemListAdapter);
        mMessageList.setSelectionFromTop(mMessageItemListAdapter.getCount(), 0);

        mNewMessageText = (EditText) findViewById(R.id.new_message_content);
        mSendButton = (Button) findViewById(R.id.send_message_button);
    }


    /**
     * Setup all listener related to the view displayed by the activity
     */
    private void setupListener() {

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String draftText = mNewMessageText.getText().toString();
                Log.i(LOG_TAG, "New msg to be sent :" + draftText);
            }

        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.conversation, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Boolean settingsSelected = item.getItemId() == R.id.action_settings;

        return settingsSelected || super.onOptionsItemSelected(item);
    }
}
