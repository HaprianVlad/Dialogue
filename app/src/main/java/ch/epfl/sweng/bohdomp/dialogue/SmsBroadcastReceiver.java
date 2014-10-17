package ch.epfl.sweng.bohdomp.dialogue;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Defines an Sms Broadcast Receiver
 */
public class SmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "SMS Detected.", Toast.LENGTH_LONG).show();
    }

}