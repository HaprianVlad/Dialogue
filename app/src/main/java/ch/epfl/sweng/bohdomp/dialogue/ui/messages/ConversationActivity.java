package ch.epfl.sweng.bohdomp.dialogue.ui.messages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.channels.DialogueOutgoingDispatcher;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.ConversationListener;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;

/**
 * @author swengTeam 2014 BohDomp
 * Activity displaying a set of messages
 */
public class ConversationActivity extends Activity implements ConversationListener {
    private static final String LOG_TAG = "ConversationActivity";
    private static final String APP_DATA = "APP_DATA";

    private ListView mMessageList;
    private EditText mNewMessageText;
    private Button mSendButton;

    private MessagesAdapter mMessageItemListAdapter;

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

            initData(conversationID);
            setViewElement();
            setupListener();

        } catch (IllegalArgumentException e) {
            Log.e(LOG_TAG, e.getMessage());
        }

    }

    /*
     * Initialize the data used by the activity
     */
    public void initData(ConversationId conversationId) {
        mConversation = DefaultDialogData.getInstance().getConversation(conversationId);
        mConversation.addListener(this);

        if (mConversation != null) {
            mMessages = mConversation.getMessages();
            mMessageItemListAdapter = new MessagesAdapter(this, mMessages);
        } else {
            throw new NullPointerException("Conversation is Null");
        }
    }

    @Override
    public void onConversationChanged(ConversationId id) {
        if (id == null) {
            throw new NullArgumentException("id");
        }

        if (mConversation.getId() != id) {
            throw new IllegalStateException("Wrong listener");
        }

        mMessageItemListAdapter.updateData(mConversation.getMessages());
    }


    /*
     * Set all view elements
     */
    private void setViewElement() {

        setTitle(mConversation.getName());

        mMessageList = (ListView) findViewById(R.id.message_List);
        mMessageList.setAdapter(mMessageItemListAdapter);
        mMessageList.setSelectionFromTop(mMessageItemListAdapter.getCount(), 0);

        mNewMessageText = (EditText) findViewById(R.id.new_message_content);
        mSendButton = (Button) findViewById(R.id.send_message_button);
        mSendButton.setEnabled(false);
    }


    /**
     * Setup all listener related to the view displayed by the activity
     */
    private void setupListener() {

        mNewMessageText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0) {
                    mSendButton.setEnabled(true);

                } else {
                    mSendButton.setEnabled(false);
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String draftText = mNewMessageText.getText().toString();

                for (Contact contact : mConversation.getContacts()) {
                    DialogueMessage message = new DialogueTextMessage(contact, draftText,
                            DialogueMessage.MessageStatus.OUTGOING);

                    DialogueOutgoingDispatcher.sendMessage(v.getContext(), message);
                }

                mNewMessageText.setText("");
            }

        });

    }

    /*

    *** IF MENU IS NEEDED UNCOMMENT ***

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

    */

    @Override
    protected void onStop() {
        mConversation.removeListener(this);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the current  state
        savedInstanceState.putBundle(APP_DATA, DefaultDialogData.getInstance().createBundle());

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);

        DefaultDialogData.getInstance().restoreFromBundle(savedInstanceState.getBundle(APP_DATA));
    }
}
