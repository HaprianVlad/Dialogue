package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * A collection of all public keys.
 */
public class PublicKeyChain extends KeyChain<PublicKeyRing> {

    private final PGPPublicKeyRingCollection mUnderlying;

    public PublicKeyChain(PGPPublicKeyRingCollection underlying) {
        Contract.throwIfArgNull(underlying, "underlying");
        this.mUnderlying = underlying;
    }

    public List<PublicKeyRing> getKeyRings() {
        List<PublicKeyRing> rings = new ArrayList<PublicKeyRing>();
        @SuppressWarnings("unchecked") // bouncy castle returns a raw iterator
        Iterator<PGPPublicKeyRing> iterator = mUnderlying.getKeyRings();
        while (iterator.hasNext()) {
            rings.add(new PublicKeyRing(iterator.next()));
        }
        return rings;
    }

}
