package ch.epfl.sweng.bohdomp.dialogue.channels.mms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import ch.epfl.sweng.bohdomp.dialogue.R;

/**
 * Defines an MMS Broadcast Receiver
 */
public class MmsBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, context.getString(R.string.MMS_Detected), Toast.LENGTH_LONG).show();
    }
}