package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import java.util.List;

import ch.epfl.sweng.bohdomp.dialogue.exceptions.NullArgumentException;

/**
 * Test keyring builders.
 */
public class BuilderTest extends AndroidTestCase {

    public void testPublicBuilder() throws Exception {
        List<PublicKey> keys = new PublicKeyRingBuilder()
                .fromString(TestKeyData.PUBLIC_KEY_RING)
                .getKeys();
        Assert.assertEquals(TestKeyData.PUBLIC_KEYS, keys.size());
    }

    public void testSecretBuilder() throws Exception {
        List<SecretKey> keys = new SecretKeyRingBuilder()
                .fromString(TestKeyData.SECRET_KEY_RING)
                .getKeys();
        Assert.assertEquals(TestKeyData.SECRET_KEYS, keys.size());
    }

    public void testPublicBuilderNull() throws Exception {
        try {
            List<PublicKey> keys = new PublicKeyRingBuilder()
                .fromString(null)
                .getKeys();
            Assert.fail("Null argument expected");
        } catch (NullArgumentException ex) {
            //all is well
        }
    }

    public void testSetcretBuilderNull() throws Exception {
        try {
            List<SecretKey> keys = new SecretKeyRingBuilder()
                .fromString(null)
                .getKeys();
            Assert.fail("Null argument expected");
        } catch (NullArgumentException ex) {
            //all is well
        }
    }

}
