package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.test.AndroidTestCase;

import junit.framework.Assert;

/**
 * Tests features of the key manager
 */
public class KeyManagerTest extends AndroidTestCase {

    private KeyManager mManager;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        mManager = new KeyManager(getContext());
    }

    public void testInitial() throws Exception {
        TestKeyData.clearKeys(getContext());
        Assert.assertEquals(0, mManager.getSecretKeyChain().getKeyRings().size());
        Assert.assertFalse(mManager.hasOwnFingerprint());
    }

    public void testPassphraseCache() throws Exception {
        TestKeyData.setKeys(getContext());
        Assert.assertEquals(TestKeyData.PASSPHRASE, mManager.getOwnPassphrase());
    }

    public void testFingerprint() throws Exception {
        TestKeyData.setKeys(getContext());
        Assert.assertEquals(TestKeyData.FINGERPRINT, mManager.getOwnFingerprint());
    }

    public void testSecretKeySize() throws Exception {
        TestKeyData.setKeys(getContext());
        Assert.assertEquals(1, mManager.getSecretKeyChain().getKeyRings().size());
    }

    public void testLocal() throws Exception {
        TestKeyData.setKeys(getContext());
        mManager.getPublicKeyRing(TestKeyData.FINGERPRINT);
    }

    public void testRemote() throws Exception {
        mManager.getPublicKeyRing(TestKeyData.FINGERPRINT);
    }

    public void testGeneration() throws Exception {
        TestKeyData.clearKeys(getContext());
        mManager.createKeyPair("John White", "foo");
        Assert.assertEquals(1, mManager.getSecretKeyChain().getKeyRings().size());
    }


}
