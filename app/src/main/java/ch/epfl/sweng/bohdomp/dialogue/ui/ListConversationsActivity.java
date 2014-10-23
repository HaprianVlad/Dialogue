package ch.epfl.sweng.bohdomp.dialogue.ui;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.provider.Telephony;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;

import ch.epfl.sweng.bohdomp.dialogue.R;


/**
 * Activity displaying the list of conversations.
 * This is the main activity.
 */
public class ListConversationsActivity extends Activity {
    private ActionBar actionBar;
    private String myPackageName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_conversations);

    }

    @Override
    protected void onResume() {
        super.onResume();

        myPackageName = getPackageName();

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


    public void setDefaultSMSAppButtonHasBeenClicked(MenuItem item) {
        if (!Telephony.Sms.getDefaultSmsPackage(this).equals(myPackageName)) {
            Intent intent =
                    new Intent(Telephony.Sms.Intents.ACTION_CHANGE_DEFAULT);
            intent.putExtra(Telephony.Sms.Intents.EXTRA_PACKAGE_NAME,
                    myPackageName);
            startActivity(intent);
        }

    }
}
