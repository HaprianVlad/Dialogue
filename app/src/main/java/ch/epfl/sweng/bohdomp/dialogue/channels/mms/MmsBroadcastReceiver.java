package ch.epfl.sweng.bohdomp.dialogue.channels.mms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Defines an MMS Broadcast Receiver
 */
public class MmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "MMS Detected.", Toast.LENGTH_LONG).show();
    }
}