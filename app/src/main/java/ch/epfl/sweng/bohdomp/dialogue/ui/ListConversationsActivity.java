package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.Activity;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.conversation.DialogueConversation;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.Contact;
import ch.epfl.sweng.bohdomp.dialogue.conversation.contact.UnknownContact;

/**
 * Activity displaying the list of conversations.
 * This is the main activity.
 */
public class ListConversationsActivity extends Activity {
    // TODELETE
    static final int ID = 1234;

    private ListView contactListView;
    private LinearLayout defaultAppWarning;

    private String myPackageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_conversations);

        myPackageName = getPackageName();

        setupDefaultAppWarning();
        setupContactListView();
    }


    private void setupDefaultAppWarning() {

        defaultAppWarning = (LinearLayout) findViewById(R.id.notDefaultWarning);

        Button changeDefaultSmsAppButton = (Button) findViewById(R.id.setDefaultAppButton);

        changeDefaultSmsAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
                intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME, myPackageName);
                startActivity(intent);
            }
        });
    }

    private void checkDefaultApp() {
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
            defaultAppWarning.setVisibility(View.VISIBLE);
        }  else {
            defaultAppWarning.setVisibility(View.GONE);
        }
    }

    private void setupContactListView() {

        contactListView = (ListView) findViewById(R.id.listConversationsView);

        //TODO MUST CHANGE THIS TO THE REAL DATA
        Contact c = new UnknownContact("0040749475877");
        Set<Contact> contactSet = new HashSet<Contact>();
        contactSet.add(c);
        DialogueConversation newConv = new DialogueConversation(contactSet);

        List<DialogueConversation> convList = new ArrayList<DialogueConversation>();
        convList.add(newConv);
        convList.add(newConv);
        convList.add(newConv);

        BaseAdapter contactItemArrayAdapter = new ContactListAdapter(this, convList);
        contactListView.setAdapter(contactItemArrayAdapter);

        contactListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                System.out.println("Item Selected");

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        checkDefaultApp();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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

    public void newConversationButtonHasBeenClicked(MenuItem item) {
        Intent intent = new Intent(this, ConversationActivity.class);
        startActivity(intent);
    }
}
