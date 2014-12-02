package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.test.AndroidTestCase;

import junit.framework.Assert;

import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.FingerprintUtils;

/**
 * Test fingerprint utility class
 */
public class FingerprintUtilTest extends AndroidTestCase {

    public final static byte[] FINGERPRINT_BYTES = {101, 81, -62, 96, -1, 92, -50, -69, -18,
        55, -125, -47, -34, 117, -78, 126, 89, -58, 117, 17};
    public final static String FINGERPRINT_STRING_RANDOM_SPACE = "6551C260 F F5 CCEBB EE3783D1DE   75B27E59C675 11 ";
    public final static String FINGERPRINT_STRING_COMPACT = "6551C260FF5CCEBBEE3783D1DE75B27E59C67511";
    public final static String FINGERPRINT_STRING_PRETTY = "6551 C260 FF5C CEBB EE3783D1 DE75 B27E 59C6 7511";

    public void testBytes() throws Exception {
        Assert.assertEquals(FINGERPRINT_STRING_PRETTY, FingerprintUtils.fromBytes(FINGERPRINT_BYTES));
    }

    public void testCompact() throws Exception {
        Assert.assertEquals(FINGERPRINT_STRING_PRETTY, FingerprintUtils.fromString(FINGERPRINT_STRING_COMPACT));
    }

    public void testRandomSpace() throws Exception {
        Assert.assertEquals(FINGERPRINT_STRING_PRETTY, FingerprintUtils.fromString(FINGERPRINT_STRING_RANDOM_SPACE));
    }

    public void testDoublePretty() throws Exception {
        Assert.assertEquals(FINGERPRINT_STRING_PRETTY, FingerprintUtils.fromString(FINGERPRINT_STRING_PRETTY));
    }

}
