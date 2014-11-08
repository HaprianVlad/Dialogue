package ch.epfl.sweng.bohdomp.dialogue.channels;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;
import android.app.IntentService;

/**
 * Class representing a receiver service
 */
public abstract class ReceiverService extends IntentService {

    public ReceiverService(String name) {
        super(name);

        if (name == null) {
            throw new NullArgumentException("name");
        }
    }
}