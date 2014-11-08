package ch.epfl.sweng.bohdomp.dialogue.crypto;

import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.KeyFingerPrintCalculator;
import org.bouncycastle.openpgp.operator.bc.BcKeyFingerprintCalculator;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Common interface of keyring builders.
 * @param <T> type of keyrings this builder can create
 */
public abstract class KeyRingBuilder<T extends KeyRing<?>> {

    /** Build a keyring from the given inputs. */
    abstract protected T build(InputStream pgpStream, KeyFingerPrintCalculator fingerPrintCalculator)
        throws IOException, PGPException;

    /** Create a keyring from a binary stream. */
    private T fromStream(InputStream in) throws IOException, PGPException {
        Contract.throwIfArgNull(in, "in");
        KeyFingerPrintCalculator calculator = new BcKeyFingerprintCalculator();
        return build(PGPUtil.getDecoderStream(in), calculator);
    }

    private T fromStringUnsafe(String in) throws PGPException, IOException {
        byte[] bytes = in.getBytes(StandardCharsets.UTF_8);
        InputStream stream = new ByteArrayInputStream(bytes);
        stream.close();
        return fromStream(stream);
    }

    /** Create a keyring from an ascii-armored string. */
    public T fromString(String in) throws PGPException {
        Contract.throwIfArgNull(in, "in");

        try {
            return fromStringUnsafe(in);
        } catch (IOException ex) {
            throw new AssertionError("IOException thrown in impossible place", ex);
        }
    }

}
