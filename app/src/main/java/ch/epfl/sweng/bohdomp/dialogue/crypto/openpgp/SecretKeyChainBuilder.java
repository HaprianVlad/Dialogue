package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 *  Factory class for creating SecretKeyRings from different types of inputs.
 */
public class SecretKeyChainBuilder extends KeyChainBuilder<SecretKeyChain> {

    protected SecretKeyChain build(InputStream pgpStream, KeyFingerPrintCalculator fingerPrintCalculator)
        throws IOException, PGPException {

        return new SecretKeyChain(new PGPSecretKeyRingCollection(pgpStream, fingerPrintCalculator));
    }

    public SecretKeyChain empty() {
        try {
            return new SecretKeyChain(new PGPSecretKeyRingCollection(new ArrayList<Byte>()));
        } catch (IOException ex) {
            throw new AssertionError("IOException while constructing empty keychain", ex);
        } catch (PGPException ex) {
            throw new AssertionError("IOException while constructing empty keychain", ex);
        }
    }

}
