package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKey;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.generation.KeyRingPair;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.generation.RsaKeyRingGenerator;

/**
 * Tests key ring generation
 */
public class GenerationTest extends AndroidTestCase {

    private static final String ID = "John Smith";
    private static final String PASSPHRASE = "testpassword";
    private static final String CLEAR = "test message 1212";

    //use weak parameters to conserve entropy and speed up testing
    private final static int KEY_LENGTH = 512;
    private final static int KEY_EXPONENT = 0x10001;
    private final static int KEY_CERTAINTY = 1;
    private final static int KEY_S2K_COUNT = 0x20;

    private RsaKeyRingGenerator getGenerator() {
        return new RsaKeyRingGenerator(KEY_LENGTH, KEY_EXPONENT, KEY_CERTAINTY, KEY_S2K_COUNT);
    }

    public void testGeneration() throws Exception {
        getGenerator().generate(ID, PASSPHRASE);
    }

    public void testEncryptDecrypt() throws Exception {
        KeyRingPair pair = getGenerator().generate(ID, PASSPHRASE);
        SecretKeyRing sec = pair.getSecretKeyRing();
        PublicKey enc = pair.getPublicKeyRing().getEncryptionKeys().get(0);

        Assert.assertEquals(CLEAR, sec.decrypt(enc.encrypt(CLEAR), PASSPHRASE));
    }

}
