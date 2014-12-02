package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

/**
 * Marker for keys
 */
public interface Key {

    /** Is this key a master key? */
    boolean isMasterKey();

    /** Returns this key's fingerprint in string format. */
    String getFingerprint();

}
