package ch.epfl.sweng.bohdomp.dialogue.ui.conversation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.BuildConfig;
import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.channels.DialogueOutgoingDispatcher;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.ConversationListener;
import ch.epfl.sweng.bohdomp.dialogue.data.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.data.StorageManager;
import ch.epfl.sweng.bohdomp.dialogue.ids.ConversationId;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueMessage;
import ch.epfl.sweng.bohdomp.dialogue.messaging.DialogueTextMessage;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * @author swengTeam 2014 BohDomp
 * Activity displaying a set of messages
 */
public class ConversationActivity extends Activity implements ConversationListener {
    private static final String LOG_TAG = "ConversationActivity";

    private ListView mMessageList;
    private EditText mNewMessageText;
    private Button mSendButton;

    private MessagesAdapter mMessageItemListAdapter;

    private Conversation mConversation;
    private List<DialogueMessage> mMessages;

    private StorageManager mStorageManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);

        setupActionBar();

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

    private void setupActionBar() {
        ActionBar ab = getActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
    }

    /*
     * Initialize the data used by the activity
     */
    public void initData(ConversationId conversationId) {
        Contract.throwIfArgNull(conversationId, "conversationId");

        mStorageManager = new StorageManager(getApplicationContext());

        mConversation = DefaultDialogData.getInstance().getConversation(conversationId);

        if (BuildConfig.DEBUG && mConversation == null) {
            throw new AssertionError("null mConversation");
        }


        mConversation.addListener(this);

        mMessages = mConversation.getMessages();
        mMessageItemListAdapter = new MessagesAdapter(this, mMessages);
    }

    @Override
    public void onConversationChanged(ConversationId conversationId) {
        Contract.throwIfArgNull(conversationId, "id");
        Contract.throwIfArg(mConversation.getId().getLong() != conversationId.getLong(), "Wrong listener");

        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMessageItemListAdapter.updateData(mConversation.getMessages());
            }
        });
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
                Contract.throwIfArgNull(editable, "editable");

                if (editable.length() != 0) {
                    mSendButton.setEnabled(true);
                } else {
                    mSendButton.setEnabled(false);
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Contract.throwIfArgNull(view, "view");

                String draftText = mNewMessageText.getText().toString();

                for (Contact contact : mConversation.getContacts()) {
                    DialogueMessage message = new DialogueTextMessage(contact, draftText,
                            DialogueMessage.MessageStatus.OUTGOING);

                    DialogueOutgoingDispatcher.sendMessage(view.getContext(), message);
                }

                mNewMessageText.setText("");
            }

        });

    }


    protected void onPause() {
        mStorageManager.saveData();
        super.onPause();
    }

    @Override
    protected void onStop() {
        mConversation.removeListener(this);
        super.onStop();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent parent = this.getParentActivityIntent();
            startActivity(parent);
        }
        return super.onKeyDown(keyCode, event);
    }
}
