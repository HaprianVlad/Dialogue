package ch.epfl.sweng.bohdomp.dialogue.channels;

import android.app.IntentService;


/**
 *  Abstract class representing a sender service
 */
public abstract class SenderService extends IntentService {

    public SenderService(String name) {
        super(name);
    }


}

