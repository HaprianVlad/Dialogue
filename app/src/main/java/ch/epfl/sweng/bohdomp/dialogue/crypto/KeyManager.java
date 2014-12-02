package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.content.Context;

import org.bouncycastle.openpgp.PGPException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.KeyChain;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.KeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyChain;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyChain;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyChainBuilder;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Utility class to manage storage of keyrings.
 */
public class KeyManager {

    private static final String SECRET_KEY_RING = "secring.asc";
    private static final String PUBLIC_KEY_RING = "pubring.asc";

    private Context mContext;

    public KeyManager(Context context) {
        Contract.throwIfArgNull(context, "context");

        mContext = context;
    }

    private <T extends KeyChain<?>> T getKeyChain(String path, KeyChainBuilder<T> builder)
        throws IOException, PGPException {

        File keyRingFile = new File(mContext.getExternalFilesDir(null), path);
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

    public SecretKeyChain getSecretKeyChain()
        throws FileNotFoundException, IOException, PGPException {
        return getKeyChain(SECRET_KEY_RING, new SecretKeyChainBuilder());
    }

    public PublicKeyChain getPublicKeyChain()
        throws FileNotFoundException, IOException, PGPException {
        return getKeyChain(PUBLIC_KEY_RING, new PublicKeyChainBuilder());
    }

}
