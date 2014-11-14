package ch.epfl.sweng.bohdomp.dialogue.ui.newConversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.ui.messages.ConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Activity enables the user to create a new conversation
 */
public class NewConversationActivity extends Activity {
    private static final String LOG_TAG = "NewMessageActivity";
    private static final String APP_DATA = "APP_DATA";

    private DialogueData mData;
    private ContactFactory mContactFactory;

    private EditText mToEditText;
    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        setDialogueData(DefaultDialogData.getInstance());
        mContactFactory = new ContactFactory(getApplicationContext());

        setViewElement();
        setupListener();
    }

    /*
     * Set DialogueData
     */
    private void setDialogueData(DialogueData data) {
        if (data == null) {
            throw new IllegalStateException("DialogueData is null");
        }

        mData = data;
    }

    /*
     * Set all view elements
     */
    private void setViewElement() {
        setTitle(getString(R.string.newConversationActivityTitle));
        mToEditText = (EditText) findViewById(R.id.message_to);
        mSendButton = (Button) findViewById(R.id.create_conversation_button);
        mSendButton.setEnabled(false);
    }


    /**
     * Setup all listener related to the view displayed by the activity
     */
    private void setupListener() {
        mToEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.length() != 0 && Patterns.PHONE.matcher(editable).matches()) {
                    mSendButton.setEnabled(true);

                } else {
                    mSendButton.setEnabled(false);
                }
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ConversationActivity.class);

                Contact contact = null;

                try {
                    contact = mContactFactory.contactFromNumber(mToEditText.getText().toString());
                    Conversation conversation = mData.createOrGetConversation(contact);
                    intent.putExtra(DialogueConversation.CONVERSATION_ID, conversation.getId());
                    startActivity(intent);
                } catch (InvalidNumberException e) {
                    mSendButton.setEnabled(false);
                    Toast.makeText(getApplicationContext(), "This is not a valid input for phone number, please retry!",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /*

    *** IF MENU IS NEEDED UNCOMMENT ***

    /**
     * Set the DialogueData used in this activity
     * @param data the new DialogueData to use
     */

    /*
    public void setDialogueData(DialogueData data){
        if(data == null){
            throw new NullArgumentException("Data");
        }

        mData = data;
    }

    public void setContactFactory(ContactFactory contactFactory){
        if(contactFactory == null){
            throw new NullArgumentException("Data");
        }

        mContactFactory = contactFactory;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_conversation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    */

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
