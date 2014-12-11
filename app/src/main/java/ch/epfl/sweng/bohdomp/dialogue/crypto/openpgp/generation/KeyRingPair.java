package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.generation;

import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Regroups a public and secret key ring
 */
public class KeyRingPair {

    private final PublicKeyRing mPublic;
    private final SecretKeyRing mSecret;

    public KeyRingPair(PublicKeyRing pub, SecretKeyRing sec) {
        Contract.throwIfArgNull(pub, "pub");
        Contract.throwIfArgNull(sec, "sec");

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
