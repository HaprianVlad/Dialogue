package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *  Factory class for creating PublicKeyRings from different types of inputs.
 */
public class PublicKeyChainBuilder extends KeyChainBuilder<PublicKeyChain> {

    protected PublicKeyChain build(InputStream pgp, KeyFingerPrintCalculator calculator)
        throws IOException, PGPException {

        return new PublicKeyChain(new PGPPublicKeyRingCollection(pgp, calculator));
    }

    public PublicKeyChain empty() {
        try {
            return new PublicKeyChain(new PGPPublicKeyRingCollection(new ArrayList<Byte>()));
        } catch (IOException ex) {
            throw new AssertionError("IOException while constructing empty keychain", ex);
        } catch (PGPException ex) {
            throw new AssertionError("PGPException while constructing empty keychain", ex);
        }
    }

}
