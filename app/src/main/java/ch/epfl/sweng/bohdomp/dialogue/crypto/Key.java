package ch.epfl.sweng.bohdomp.dialogue.crypto;

/**
 * Marker for keys
 */
public interface Key {

    /**
     * Unique key id of this key. NOT the key's fingerprint.
     */
    long getId();

}
