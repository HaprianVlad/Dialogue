package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKey;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKey;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Test keyring builders.
 */
public class BuilderTest extends AndroidTestCase {

    public void testPublicBuilder() throws Exception {
        List<PublicKey> keys = new PublicKeyChainBuilder()
                .fromString(TestKeyData.PUBLIC_KEY_RING)
                .getKeyRings()
                .get(0)
                .getKeys();
        Assert.assertEquals(TestKeyData.PUBLIC_KEYS, keys.size());
    }

    public void testSecretBuilder() throws Exception {
        List<SecretKey> keys = new SecretKeyChainBuilder()
                .fromString(TestKeyData.SECRET_KEY_RING)
                .getKeyRings()
                .get(0)
                .getKeys();
        Assert.assertEquals(TestKeyData.SECRET_KEYS, keys.size());
    }

    public void testPublicBuilderNull() throws Exception {
        try {
            List<PublicKeyRing> rings = new PublicKeyChainBuilder()
                .fromString(null)
                .getKeyRings();
            Assert.fail("Null argument expected");
        } catch (NullArgumentException ex) {
            //all is well
        }
    }

    public void testSecretBuilderNull() throws Exception {
        try {
            List<SecretKeyRing> rings = new SecretKeyChainBuilder()
                .fromString(null)
                .getKeyRings();
            Assert.fail("Null argument expected");
        } catch (NullArgumentException ex) {
            //all is well
        }
    }

    public void testPublicEmpty() throws Exception {
        Assert.assertEquals(new PublicKeyChainBuilder().empty().getKeyRings().size(), 0);
    }

    public void testSecretEmpty() throws Exception {
        Assert.assertEquals(new SecretKeyChainBuilder().empty().getKeyRings().size(), 0);
    }

}
