package ch.epfl.sweng.bohdomp.dialogue.channels.sms;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 *  Represents a service that during a phone call sends an sms if needed
 */
public final class HeadlessSmsSendService extends Service {
    public HeadlessSmsSendService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        Contract.throwIfArgNull(intent, "intent");

        throw new UnsupportedOperationException("Not yet implemented");
    }
}
