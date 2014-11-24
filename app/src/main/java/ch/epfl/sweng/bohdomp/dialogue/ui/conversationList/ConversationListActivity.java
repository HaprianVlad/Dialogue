package ch.epfl.sweng.bohdomp.dialogue.ui.conversationList;

import android.app.Activity;
import android.content.Intent;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcel;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.Conversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DefaultDialogData;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueData;
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
    private final String saveFileName ="fileName";

    private ListView mContactListView;
    private LinearLayout mDefaultAppWarningLayout;
    private Button mChangeDefaultAppButton;

    private AlertDialog mDialog;

    private String myPackageName;

    private DialogueData mData;

    private List<Conversation> mConversationList;
    private ConversationListAdapter mConversationItemListAdapter;

    private Bundle mSavedInstance;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_contact_list);

        initData();
        setViewElements();
        setupListener();

        mData.addListener(this);
    }

    /*
     * Initialize the data used by the activity
     */
    private void initData() {
        myPackageName = getPackageName();
        mData = DefaultDialogData.getInstance();
        mConversationList = mData.getConversations();
        mConversationItemListAdapter = new ConversationListAdapter(this, mConversationList);
        retreiveDataFromFile();

    }

    @Override
    public void onDialogueDataChanged() {

        mConversationList = mData.getConversations();
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

        setDialogDeleteAll();
    }

    private void setDialogDeleteAll() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        mData.removeAllConversations();
                        dialog.cancel();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                    default:
                        break;
                }
            }
        };

        builder.setTitle(getString(R.string.deleteAllDialog_title));
        builder.setMessage(getString(R.string.deleteAllDialog_question));
        builder.setPositiveButton("Yes", dialogClickListener);
        builder.setNegativeButton("No", dialogClickListener);

        mDialog = builder.create();
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

                c.setAllMessagesAsRead();

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        checkDefaultApp();

        mData = DefaultDialogData.getInstance();
        mConversationList = mData.getConversations();
        mData.addListener(this);

        super.onResume();
    }

    @Override
    protected void onPause() {
        mData.removeListener(this);
        super.onPause();
    }

    @Override
    protected void onStop() {
        mData.removeListener(this);
        saveDataToFile();
        super.onStop();
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

        switch (item.getItemId()) {
            case R.id.action_settings:
                return true;
            case R.id.action_deleteAll:
                mDialog.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Contract.throwIfArgNull(savedInstanceState, "savedInstanceState");

        Bundle b = DefaultDialogData.getInstance().createBundle();
        savedInstanceState.putBundle(APP_DATA, b);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        Contract.throwIfArgNull(savedInstanceState, "savedInstanceState");

        super.onRestoreInstanceState(savedInstanceState);

        DefaultDialogData.getInstance().restoreFromBundle(savedInstanceState.getBundle(APP_DATA));
    }

    /**
     * Method called when the new conversation is clicked
     * Start the "new message activity"
     */
    public void newConversationClicked(MenuItem item) {
        Intent intent = new Intent(this, NewConversationActivity.class);
        startActivity(intent);
    }

    private void saveDataToFile() {

        FileOutputStream outputStream = null;

        Bundle bundle = DefaultDialogData.getInstance().createBundle();

        Parcel parcel = Parcel.obtain();
        bundle.writeToParcel(parcel, 0);
        parcel.setDataPosition(0);

        try {
            outputStream = openFileOutput(saveFileName, getApplicationContext().MODE_PRIVATE);
            outputStream.write(parcel.marshall());


        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Unable to save messages", Toast.LENGTH_LONG).show();

        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Unable to save messages", Toast.LENGTH_LONG).show();

                }
            }
        }


    }

    private void retreiveDataFromFile() {
        Parcel parcel = Parcel.obtain();

        byte [] data = readFile();
        if (data != null) {
            parcel.unmarshall(data, 0, data.length);

            parcel.setDataPosition(0);

            DefaultDialogData.getInstance().restoreFromBundle(parcel.readBundle());


        }
        parcel.recycle();

    }

    private byte[] readFile() {
        byte[] data = null;
        FileInputStream inputStream = null;
        try {
            inputStream =openFileInput(saveFileName);
            data = new byte[inputStream.available()];
            inputStream.read(data);

        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "Unable to retreive old messages", Toast.LENGTH_LONG).show();

        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Toast.makeText(getApplicationContext(), "Unable to retreive old messages", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

        return data;
    }

}
