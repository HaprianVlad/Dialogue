package ch.epfl.sweng.bohdomp.dialogue.ui.newConversation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.ContactFactory;
import ch.epfl.sweng.bohdomp.dialogue.data.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.data.StorageManager;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.ContactLookupException;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.InvalidNumberException;
import ch.epfl.sweng.bohdomp.dialogue.ui.conversation.ConversationActivity;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * @author swengTeam 2013 BohDomp
 * Activity enables the user to create a new conversation
 */
public class NewConversationActivity extends Activity {

    private ContactFactory mContactFactory;

    private EditText mToEditText;
    private Button mSendButton;
    private Button mSelectContact;

    private StorageManager mStorageManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);

        mContactFactory = new ContactFactory(getApplicationContext());
        mStorageManager = new StorageManager(getApplicationContext());

        setupActionBar();
        setViewElement();
        setupListener();
    }

    /*
     * Setup the action bar
     */
    private void setupActionBar() {
        ActionBar ab = getActionBar();
        if (ab != null) {
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
        mSelectContact = (Button) findViewById(R.id.selectContact);
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
                
                try {
                    Contact contact = mContactFactory.contactFromNumber(mToEditText.getText().toString());
                    goToConversation(contact);
                } catch (InvalidNumberException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "This is not valid phone number, "
                            + "please retry!", Toast.LENGTH_LONG).show();
                    mSendButton.setEnabled(false);
                }
            }
        });

        mSelectContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {

            Uri contactData = data.getData();
            Cursor s = getContentResolver().query(contactData, null, null, null, null);

            if (s.moveToFirst()) {
                int index = s.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LOOKUP_KEY);
                String key = s.getString(index);

                try {
                    Contact contact = mContactFactory.contactFromLookupKey(key);
                    goToConversation(contact);
                } catch (ContactLookupException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "This is not a valid contact, "
                            + "please retry!", Toast.LENGTH_LONG).show();
                    mSendButton.setEnabled(false);
                }
            }
        }
    }

    private void goToConversation(Contact contact) {
        Conversation conversation = DefaultDialogData.getInstance().createOrGetConversation(contact);
        Intent intent = new Intent(this, ConversationActivity.class);
        intent.putExtra(DialogueConversation.CONVERSATION_ID, conversation.getId());
        startActivity(intent);
    }

    protected void onResume() {
        mStorageManager.saveData();
        super.onResume();
    }

}
