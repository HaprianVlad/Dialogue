package ch.epfl.sweng.bohdomp.dialogue.crypto;

import java.util.List;

/**
 * Common behaviour of keyrings
 * @param <T> type of keys contained in this keyring
 */
public interface KeyRing<T extends Key> {

    /**
     * Retrieve a list of all keys contained in this keyring.
     */
    List<T> getKeys();

}
