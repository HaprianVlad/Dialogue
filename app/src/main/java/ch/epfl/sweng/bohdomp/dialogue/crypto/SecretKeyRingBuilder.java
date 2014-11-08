package ch.epfl.sweng.bohdomp.dialogue.crypto;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;

import java.io.IOException;
import java.io.InputStream;

/**
 *  Factory class for creating SecretKeyRings from different types of inputs.
 */
public class SecretKeyRingBuilder extends KeyRingBuilder<SecretKeyRing> {

    protected SecretKeyRing build(InputStream pgpStream, KeyFingerPrintCalculator fingerPrintCalculator)
        throws IOException, PGPException {

        return new SecretKeyRing(new PGPSecretKeyRing(pgpStream, fingerPrintCalculator));
    }

}
