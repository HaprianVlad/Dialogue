package ch.epfl.sweng.bohdomp.dialogue.crypto;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPublicKeyRing;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;

import java.io.IOException;
import java.io.InputStream;

/**
 *  Factory class for creating PublicKeyRings from different types of inputs.
 */
public class PublicKeyRingBuilder extends KeyRingBuilder<PublicKeyRing> {

    protected PublicKeyRing build(InputStream pgp, KeyFingerPrintCalculator calculator)
        throws IOException, PGPException {

        return new PublicKeyRing(new PGPPublicKeyRing(pgp, calculator));
    }

}
