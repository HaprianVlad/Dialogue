package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

import java.util.List;

/**
 * Common behaviour of key rings. A key ring contains one master key
 * with its associated sub-keys.
 * @param <T> type of keys contained in this keyring
 */
public interface KeyRing<T extends Key> {

    /**
     * Retrieve a list of all keys contained in this key ring.
     */
    List<T> getKeys();

}
