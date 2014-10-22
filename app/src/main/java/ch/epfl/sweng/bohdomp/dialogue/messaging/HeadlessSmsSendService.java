package ch.epfl.sweng.bohdomp.dialogue.messaging;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 *  Represents a service that during a phone call sends an sms if needed
 */
public class HeadlessSmsSendService extends Service {
    public HeadlessSmsSendService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
