package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.generation;

import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyRing;

/**
 * Regroups a public and secret key ring
 */
public class KeyRingPair {

    private final PublicKeyRing mPublic;
    private final SecretKeyRing mSecret;

    public KeyRingPair(PublicKeyRing pub, SecretKeyRing sec) {
        this.mPublic = pub;
        this.mSecret = sec;
    }

    public PublicKeyRing getPublicKeyRing() {
        return mPublic;
    }

    public SecretKeyRing getSecretKeyRing() {
        return mSecret;
    }

}
