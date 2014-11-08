package ch.epfl.sweng.bohdomp.dialogue.channels;

import android.app.IntentService;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 *  Abstract class representing a sender service
 */
public abstract class SenderService extends IntentService {

    public SenderService(String name) {
        super(name);

        if (name == null) {
            throw new NullArgumentException("name");
        }
    }
}

