package ch.epfl.sweng.bohdomp.dialogue.crypto;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyDecryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Wraps an OpenPGP secret key, used for signing and decrypting messages.
 *
 * Functionality and design of cryptographic methods is inspired by the SBT PGP plugin (https://github.com/sbt/sbt-pgp)
 * and the question posted to StackOverflow
 * http://stackoverflow.com/questions/19173181/bouncycastle-pgp-decrypt-and-verify
 */
public class SecretKey extends SecretKeyLike implements Key {
    private final PGPSecretKey underlying;

    SecretKey(PGPSecretKey underlyingKey) {
        Contract.throwIfArgNull(underlyingKey, "underlyingKey");
        this.underlying = underlyingKey;
    }

    public long getId() {
        return underlying.getKeyID();
    }

    @Override
    protected PGPPrivateKey extractPrivateKey(long id, char[] pass) throws PGPException, IncorrectPassphraseException {
        if (id != underlying.getKeyID()) {
            return null;
        }

        try {
            BcPGPDigestCalculatorProvider provider = new BcPGPDigestCalculatorProvider();
            BcPBESecretKeyDecryptorBuilder builder = new BcPBESecretKeyDecryptorBuilder(provider);
            return underlying.extractPrivateKey(builder.build(pass));
        } catch (PGPException ex) {
            if (ex.getMessage().contains("checksum mismatch")) {
                throw new IncorrectPassphraseException(ex);
            } else {
                throw ex;
            }
        }
    }

}
