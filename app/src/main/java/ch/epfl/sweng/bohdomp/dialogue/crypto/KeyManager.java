package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.bouncycastle.openpgp.PGPException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import ch.epfl.sweng.bohdomp.dialogue.crypto.hkp.Client;
import ch.epfl.sweng.bohdomp.dialogue.crypto.hkp.HkpServerException;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.KeyChain;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.KeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.KeyNotFoundException;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyChain;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyChain;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.generation.KeyRingPair;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.generation.RsaKeyRingGenerator;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Utility class to manage storage and retrieval of keyrings.
 */
public class KeyManager {

    private final static String PREF_OWN_FINGERPRINT =
            "ch.epfl.sweng.bohdomp.dialogue.crypto.KeyManager.PREF_OWN_FINGERPRINT";
    private final static String PREF_OWN_PASSPHRASE = //TODO store password somewhere more secure
            "ch.epfl.sweng.bohdomp.dialogue.crypto.KeyManager.PREF_OWN_PASSPHRASE";

    private final static String KEY_SERVER = "pgp.mit.edu"; //TODO make user configurable
    private final static String PUBLIC_KEY_CHAIN_PATH = "pubchain.asc";
    private final static String SECRET_KEY_CHAIN_PATH = "secchain.asc";

    private final Context mContext;
    private final SharedPreferences mPreferences;
    private final Client mHkpClient;

    public KeyManager(Context context) {
        Contract.throwIfArgNull(context, "context");

        mContext = context;
        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mHkpClient = new Client(KEY_SERVER);
    }

    public static KeyManager getInstance(Context context) {
        return new KeyManager(context);
    }

    /**
     * Loads a key chain from a file into this key manager.
     */
    private <T extends KeyChain<?>> T loadKeyChain(String name, KeyChainBuilder<T> builder)
        throws IOException, PGPException {

        File keyRingFile = new File(mContext.getFilesDir(), name);
        InputStream keyRingStream = null;
        try {
            keyRingStream = new FileInputStream(keyRingFile);
            return builder.fromStream(keyRingStream);
        } catch (FileNotFoundException ex) {
            return builder.empty();
        } finally {
            if (keyRingStream != null) {
                keyRingStream.close();
            }
        }
    }

    /**
     * Saves a key chain to a file.
     */
    private void saveKeyChain(String name, KeyChain<?> chain) throws IOException {
        File keyRingFile = new File(mContext.getFilesDir(), name);
        FileOutputStream output = null;
        try {
            output = new FileOutputStream(keyRingFile);
            output.write(chain.toArmored().getBytes());
        } finally {
            if (output != null) {
                output.close();
            }
        }
    }

    private SecretKeyChain loadSecretKeyChain() throws IOException, PGPException {
        return loadKeyChain(SECRET_KEY_CHAIN_PATH, new SecretKeyChainBuilder());
    }

    private PublicKeyChain loadPublicKeyChain() throws IOException, PGPException {
        return loadKeyChain(PUBLIC_KEY_CHAIN_PATH, new PublicKeyChainBuilder());
    }

    private void saveSecretKeyChain(SecretKeyChain chain) throws IOException, PGPException {
        saveKeyChain(SECRET_KEY_CHAIN_PATH, chain);
    }

    private void savePublicKeyChain(PublicKeyChain chain) throws IOException, PGPException {
        saveKeyChain(PUBLIC_KEY_CHAIN_PATH, chain);
    }

    /**
     * Get a remote keyring identified by a fingerprint. This key can either be available locally
     * or online.
     *
     * @return the public keyring matching the given fingerpint
     */
    public synchronized PublicKeyRing getPublicKeyRing(String fingerprint)
        throws IOException, PGPException, HkpServerException, KeyNotFoundException {

        Contract.throwIfArgNull(fingerprint, "fingerprint");

        PublicKeyChain localChain = loadPublicKeyChain();

        PublicKeyRing ring;
        try {
            ring = localChain.getKeyRing(fingerprint);
        } catch (KeyNotFoundException ex) {
            PublicKeyRing remoteRing = mHkpClient.lookupKeyRing(fingerprint);
            PublicKeyChain updated = localChain.add(remoteRing);
            savePublicKeyChain(updated);
            ring = remoteRing;
        }

        return ring;
    }

    /**
     * Get the secret key chain stored on this device.
     */
    public synchronized SecretKeyChain getSecretKeyChain()
        throws IOException, PGPException {

        return loadSecretKeyChain();
    }

    /**
     * Has this device (account) got a fingerprint associated to it?
     */
    public boolean hasOwnFingerprint() {
        return mPreferences.contains(PREF_OWN_FINGERPRINT);
    }

    /**
     * Get this account's fingerprint
     */
    public String getOwnFingerprint() {
        return mPreferences.getString(PREF_OWN_FINGERPRINT, "");
    }

    /**
     * Get the cached password of this account's key
     */
    String getOwnPassphrase() {
        return mPreferences.getString(PREF_OWN_PASSPHRASE, "");
    }

    /**
     * Use the keyring specified by the fingerprint for this account
     */
    public void setOwn(String fingerprint, String passphrase) {
        Contract.throwIfArgNull(fingerprint, "fingerprint");
        Contract.throwIfArgNull(passphrase, "passphrase");

        mPreferences.edit().putString(PREF_OWN_PASSPHRASE, passphrase).apply();
        mPreferences.edit().putString(PREF_OWN_FINGERPRINT, fingerprint).apply();
    }

    /**
     * Publish this account's public key ring to a keyserver
     */
    private synchronized void publishOwnKeyRing() throws IOException, PGPException, HkpServerException,
        KeyNotFoundException {

        if (!hasOwnFingerprint()) {
            throw new KeyNotFoundException("No own key is set");
        }

        PublicKeyRing ring = getPublicKeyRing(getOwnFingerprint());

        mHkpClient.submitKeyRing(ring);
    }

    /**
     * Creates and saves new key pair.
     * @param id an arbitrary string defining the identity to which the keys should be associated
     * @param passphrase a passphrase with which the private key will be protected
     * @return the fingerprint of the pair's public master key
     */
    public synchronized String createKeyPair(String id, String passphrase) throws IOException, PGPException,
            HkpServerException {

        Contract.throwIfArgNull(id, "id");
        Contract.throwIfArgNull(passphrase, "passphrase");

        KeyRingPair keyRings = RsaKeyRingGenerator.getDefault().generate(id, passphrase);
        SecretKeyRing sec = keyRings.getSecretKeyRing();
        PublicKeyRing pub = keyRings.getPublicKeyRing();

        saveSecretKeyChain(loadSecretKeyChain().add(sec));
        savePublicKeyChain(loadPublicKeyChain().add(pub));

        return pub.getFingerprint();
    }

    /**
     * Import a secret and public key ring into the respective key chains.
     */
    public synchronized void importKeyPair(SecretKeyRing secretKeyRing, PublicKeyRing publicKeyRing)
        throws IOException, PGPException {

        Contract.throwIfArgNull(secretKeyRing, "secretKey");
        Contract.throwIfArgNull(publicKeyRing, "publicKey");

        saveSecretKeyChain(loadSecretKeyChain().add(secretKeyRing));
        savePublicKeyChain(loadPublicKeyChain().add(publicKeyRing));
    }

}
