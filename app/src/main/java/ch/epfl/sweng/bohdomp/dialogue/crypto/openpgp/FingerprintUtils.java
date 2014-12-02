package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Utility methods for converting and checking fingerprints
 */
public class FingerprintUtils {

    /**
     * Create a pretty fingerprint string from a raw byte array.
     *
     * @see FingerprintUtils#fromString
     */
    public static String fromBytes(byte[] fingerprint) {
        Contract.throwIfArgNull(fingerprint, "fingerprint");

        StringBuilder builder = new StringBuilder(fingerprint.length * 2);
        for (byte b : fingerprint) {
            builder.append(String.format("%02x", b & 0xff));
        }
        return fromString(builder.toString());
    }

    /**
     * Create a pretty fingerprint from a fingerpint in hex string format.
     * A pretty fingerprint is divided into groups of four hex digits, i.e
     * 0000 0000 0000 0000 0000 0000 0000 0000 0000 0000
     */
    public static String fromString(String fingerprint) {
        Contract.throwIfArgNull(fingerprint, "fingerprint");

        String cleaned = fingerprint.replaceAll("\\s+", "");
        String upper = cleaned.toUpperCase();
        return upper.replaceAll("(.{5}?)", "$0 ").trim();
    }

}
