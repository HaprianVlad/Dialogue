package ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.generation;

import org.bouncycastle.bcpg.HashAlgorithmTags;
import org.bouncycastle.bcpg.SymmetricKeyAlgorithmTags;
import org.bouncycastle.bcpg.sig.Features;
import org.bouncycastle.bcpg.sig.KeyFlags;
import org.bouncycastle.crypto.generators.RSAKeyPairGenerator;
import org.bouncycastle.crypto.params.RSAKeyGenerationParameters;
import org.bouncycastle.openpgp.PGPEncryptedData;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPKeyPair;
import org.bouncycastle.openpgp.PGPKeyRingGenerator;
import org.bouncycastle.openpgp.PGPPublicKey;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketVector;
import org.bouncycastle.openpgp.operator.PBESecretKeyEncryptor;
import org.bouncycastle.openpgp.operator.PGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.PGPDigestCalculator;
import org.bouncycastle.openpgp.operator.bc.BcPBESecretKeyEncryptorBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.bc.BcPGPDigestCalculatorProvider;
import org.bouncycastle.openpgp.operator.bc.BcPGPKeyPair;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Date;

import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.PublicKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.crypto.openpgp.SecretKeyRing;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * Utility class for generating key rings which use RSA for signing and encrypting.
 * Key rings created are made of a master key capable of signing and a sub-key capable of encryption.
 * Provides methods for key generation that use some sensible default settings.
 * <p/>
 * The generation procedure is inspired by the instructions from KB Sriram,
 * at http://bouncycastle-pgp-cookbook.blogspot.de/
 */
public class RsaKeyRingGenerator {

    private final static int DEFAULT_KEY_LENGTH = 4096;

    private final static int DEFAULT_EXPONENT = 0x10001;

    private final static int DEFAULT_CERTAINTY = 5;

    /**
     * Note: s2kcount is a number between 0 and 0xff that controls the
     * number of times to iterate the password hash before use. More
     * iterations are useful against offline attacks, as it takes more
     * time to check each password. The actual number of iterations is
     * rather complex, and also depends on the hash function in use.
     * Refer to Section 3.7.1.3 in rfc4880.txt. Bigger numbers give
     * you more iterations.  As a rough rule of thumb, when using
     * SHA256 as the hashing function, 0x10 gives you about 64
     * iterations, 0x20 about 128, 0x30 about 256 and so on till 0xf0,
     * or about 1 million iterations. The maximum you can go to is
     * 0xff, or about 2 million iterations. Use 0xc0 as a
     * default -- about 130,000 iterations.
     */
    private final static int DEFAULT_S2K_COUNT = 0xc0;

    public static RsaKeyRingGenerator getDefault() {
        return new RsaKeyRingGenerator(DEFAULT_KEY_LENGTH, DEFAULT_EXPONENT, DEFAULT_CERTAINTY, DEFAULT_S2K_COUNT,
                new SecureRandom());
    }

    private final RSAKeyGenerationParameters mRsaParameters;
    private final int mS2kCount;

    public RsaKeyRingGenerator(int keyLength, int exponent, int certainty, int s2kCount, SecureRandom random) {
        Contract.throwIfArgNull(random, "random");

        this.mRsaParameters = new RSAKeyGenerationParameters(
                BigInteger.valueOf(exponent),
                random,
                keyLength,
                certainty);
        this.mS2kCount = s2kCount;
    }

    private RSAKeyPairGenerator getRsaGenerator() {
        RSAKeyPairGenerator rsa = new RSAKeyPairGenerator();
        rsa.init(mRsaParameters);
        return rsa;
    }

    /**
     * Creates a signature vector that contains key preferences for signing
     */
    private PGPSignatureSubpacketVector getSignVector() {
        PGPSignatureSubpacketGenerator generator = new PGPSignatureSubpacketGenerator();

        generator.setKeyFlags(false, KeyFlags.SIGN_DATA | KeyFlags.CERTIFY_OTHER);
        generator.setPreferredSymmetricAlgorithms(false, new int[] {
            SymmetricKeyAlgorithmTags.AES_256,
            SymmetricKeyAlgorithmTags.AES_192,
            SymmetricKeyAlgorithmTags.AES_128
        });
        generator.setPreferredHashAlgorithms(false, new int[]{
            HashAlgorithmTags.SHA256,
            HashAlgorithmTags.SHA1,
            HashAlgorithmTags.SHA384,
            HashAlgorithmTags.SHA512,
            HashAlgorithmTags.SHA224
        });
        generator.setFeature(false, Features.FEATURE_MODIFICATION_DETECTION);
        return generator.generate();
    }

    /**
     * Creates a signature vector that contains key preferences for encrypting
     */
    private PGPSignatureSubpacketVector getEncryptVector() {
        PGPSignatureSubpacketGenerator generator = new PGPSignatureSubpacketGenerator();
        generator.setKeyFlags(false, KeyFlags.ENCRYPT_COMMS | KeyFlags.ENCRYPT_STORAGE);
        return generator.generate();
    }

    /**
     * Create an encryptor for storing the private key
     */
    private PBESecretKeyEncryptor getKeyEncryptor(String passphrase) throws PGPException {
        PGPDigestCalculator digestCalculator = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA256);
        BcPBESecretKeyEncryptorBuilder builder = new BcPBESecretKeyEncryptorBuilder(PGPEncryptedData.AES_256,
                digestCalculator, mS2kCount);
        return builder.build(passphrase.toCharArray());
    }

    private PGPKeyRingGenerator getGenerator(String id, String passphrase) throws PGPException {
        RSAKeyPairGenerator rsa = getRsaGenerator();

        PGPKeyPair signPair = new BcPGPKeyPair(PGPPublicKey.RSA_SIGN, rsa.generateKeyPair(), new Date());
        PGPKeyPair encryptPair = new BcPGPKeyPair(PGPPublicKey.RSA_ENCRYPT, rsa.generateKeyPair(), new Date());

        PGPDigestCalculator digestCalculator = new BcPGPDigestCalculatorProvider().get(HashAlgorithmTags.SHA1);
        PGPContentSignerBuilder signerBuilder = new BcPGPContentSignerBuilder(signPair.getPublicKey().getAlgorithm(),
                HashAlgorithmTags.SHA1);

        //create master key generator
        PGPKeyRingGenerator keyRingGenerator = new PGPKeyRingGenerator(
                PGPSignature.POSITIVE_CERTIFICATION, //certification level
                signPair,                            //master key
                id,                                  //id
                digestCalculator,                    //checksumCalculator
                getSignVector(),                     //hashedPcks,
                null,                                //unhashedPcks
                signerBuilder,                       //keySignerBuilder
                getKeyEncryptor(passphrase)          //keyEncryptor
        );

        //add sub-keys
        keyRingGenerator.addSubKey(encryptPair, getEncryptVector(), null);
        return keyRingGenerator;
    }

    /**
     * Generates new public and secret key rings that use RSA. The key rings are comprised
     * of a master key capable of signing and a subkey capable of encryption.
     *
     * @param id         an identity to which the keys are associated
     * @param passphrase a passphrase with which the private keys will be encrypted
     */
    public KeyRingPair generate(String id, String passphrase) throws PGPException {
        Contract.throwIfArgNull(id, "id");
        Contract.throwIfArgNull(passphrase, "passphrase");

        PGPKeyRingGenerator generator = getGenerator(id, passphrase);
        PublicKeyRing pub = new PublicKeyRing(generator.generatePublicKeyRing());
        SecretKeyRing sec = new SecretKeyRing(generator.generateSecretKeyRing());
        return new KeyRingPair(pub, sec);
    }
}
