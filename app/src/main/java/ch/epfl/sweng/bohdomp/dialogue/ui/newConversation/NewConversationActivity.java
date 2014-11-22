package ch.epfl.sweng.bohdomp.dialogue.ui.newConversation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
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
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * @author swengTeam 2013 BohDomp
 * Activity enables the user to create a new conversation
 */
public class NewConversationActivity extends Activity {
    private static final String LOG_TAG = "NewMessageActivity";
    private static final String APP_DATA = "APP_DATA";


    private ContactFactory mContactFactory;

    private EditText mToEditText;
    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);

        mContactFactory = new ContactFactory(getApplicationContext());

        setupActionBar();
        setViewElement();
        setupListener();
    }

    /*
     * Setup the action bar
     */
    private void setupActionBar() {
        ActionBar ab = getActionBar();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB && ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }
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
                Contract.throwIfArgNull(editable, "editable");

                if (editable.length() != 0 && Patterns.PHONE.matcher(editable).matches()) {
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

                Intent intent = new Intent(view.getContext(), ConversationActivity.class);

                try {
                    Contact contact = mContactFactory.contactFromNumber(mToEditText.getText().toString());
                    Conversation conversation = DefaultDialogData.getInstance().createOrGetConversation(contact);

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

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Contract.throwIfArgNull(savedInstanceState, "savedInstanceState");

        // Save the current  state
        savedInstanceState.putBundle(APP_DATA, DefaultDialogData.getInstance().createBundle());

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
}
