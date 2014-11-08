package ch.epfl.sweng.bohdomp.dialogue.crypto;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 *  Wraps an OpenPGP secret keyring. A keyring is essentially a collection of keys. Even if a single
 *  key is exported, it is considered as a keyring (containing one key).
 */
public class SecretKeyRing extends SecretKeyLike implements KeyRing<SecretKey> {
    private final PGPSecretKeyRing underlying;

    SecretKeyRing(PGPSecretKeyRing underlyingKeyRing) {
        Contract.throwIfArgNull(underlyingKeyRing, "underlyingKeyRing");
        this.underlying = underlyingKeyRing;
    }

    public List<SecretKey> getKeys() {
        ArrayList<SecretKey> keys = new ArrayList<SecretKey>();

        @SuppressWarnings("unchecked") // bouncy castle returns a raw iterator
        Iterator<PGPSecretKey> iterator = underlying.getSecretKeys();

        while (iterator.hasNext()) {
            keys.add(new SecretKey(iterator.next()));
        }

        return keys;
    }

    @Override
    protected PGPPrivateKey extractPrivateKey(long id, char[] passphrase)
        throws PGPException, IncorrectPassphraseException {

        for (SecretKey key: getKeys()) {
            PGPPrivateKey p = key.extractPrivateKey(id, passphrase);
            if (p != null) {
                return p;
            }
        }

        return null;
    }

}
