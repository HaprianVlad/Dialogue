package ch.epfl.sweng.bohdomp.dialogue.ui.newConversation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import ch.epfl.sweng.bohdomp.dialogue.ui.messages.ConversationActivity;

/**
 * @author swengTeam 2013 BohDomp
 * Activity enables the user to create a new conversation
 */
public class NewConversationActivity extends Activity {
    private static final String LOG_TAG = "NewMessageActivity";
    private static final String APP_DATA = "APP_DATA";

    private EditText mToEditText;
    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);


        setViewElement();
        setupListener();

    }


    /*
     * Set all view elements
     */
    private void setViewElement() {
        setTitle("New Conversation");
        mToEditText = (EditText) findViewById(R.id.message_to);
        mSendButton = (Button) findViewById(R.id.create_conversation_button);
    }


    /**
     * Setup all listener related to the view displayed by the activity
     */
    private void setupListener() {
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ConversationActivity.class);

                ContactFactory factory = new ContactFactory(getApplicationContext());
                Contact contact = null;
                try {
                    contact = factory.contactFromNumber(mToEditText.getText().toString());
                } catch (InvalidNumberException e) {
                    Toast.makeText(getApplicationContext(), "This is not a valid input for phone number, please retry!",
                            Toast.LENGTH_LONG).show();
                }
                Conversation conversation = DefaultDialogData.getInstance().createOrGetConversation(contact);

                intent.putExtra(DialogueConversation.CONVERSATION_ID, conversation.getId());
                startActivity(intent);
            }
        });
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
