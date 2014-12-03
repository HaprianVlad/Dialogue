package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * A collection of all secret keys.
 */
public class SecretKeyChain extends KeyChain<SecretKeyRing> {

    private final PGPSecretKeyRingCollection mUnderlying;

    public SecretKeyChain(PGPSecretKeyRingCollection underlying) {
        Contract.throwIfArgNull(underlying, "underlying");
        this.mUnderlying = underlying;
    }

    public List<SecretKeyRing> getKeyRings() {
        List<SecretKeyRing> rings = new ArrayList<SecretKeyRing>();

        @SuppressWarnings("unchecked") // bouncy castle returns a raw iterator
        Iterator<PGPSecretKeyRing> iterator = mUnderlying.getKeyRings();
        while (iterator.hasNext()) {
            rings.add(new SecretKeyRing(iterator.next()));
        }

        return rings;
    }

}

