package ch.epfl.sweng.bohdomp.dialogue.crypto;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.ResultReceiver;

import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;

/**
 * An {@link IntentService} exposing the OpenPGP crypto API.
 */
public class CryptoService extends IntentService {

    private static final String ACTION_ENCRYPT =
            "ch.epfl.sweng.bohdomp.dialogue.crypto.action.ENCRYPT";
    private static final String ACTION_DECRYPT =
            "ch.epfl.sweng.bohdomp.dialogue.crypto.action.DECRYPT";
    private static final String ACTION_ENCRYPTION_STATUS =
            "ch.epfl.sweng.bohdomp.dialogue.crypto.action.ENCRYPTION_STATUS";

    public static final String EXTRA_FINGERPRINT =
            "ch.epfl.sweng.bohdomp.dialogue.crypto.extra.RECEIVER_FINGERPRINT";
    public static final String EXTRA_CLEAR_TEXT =
            "ch.epfl.sweng.bohdomp.dialogue.crypto.extra.EXTRA_CLEAR_TEXT";
    public static final String EXTRA_ENCRYPTED_TEXT =
            "ch.epfl.sweng.bohdomp.dialogue.crypto.extra.EXTRA_ENCRYPTED_TEXT";
    public static final String EXTRA_RECEIVER =
            "ch.epfl.sweng.bohdomp.dialogue.crypto.extra.RECEIVER";

    public static final int RESULT_SUCCESS = 0;

    private KeyManager mKeyManager;

    public CryptoService() {
        super("CryptoService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Context context = getApplicationContext();
        Contract.throwIfArgNull(context, "Cannot create a CryptoService with an empty context.");
        mKeyManager = new KeyManager(context);
    }

    /**
     * Encrypt a message with the given fingerprint's public key.
     * @param context android context
     * @param fingerprint public key's fingerprint
     * @param message the message to encrypt
     * @param receiver a receiver that will be notified once the message is encrypted
     */
    public static void startActionEncrypt(Context context, String fingerprint, String message,
                                          ResultReceiver receiver) {
        Contract.throwIfArgNull(context, "context");
        Contract.throwIfArgNull(fingerprint, "fingerprint");
        Contract.throwIfArgNull(message, "message");
        Contract.throwIfArgNull(receiver, "receiver");

        Intent intent = new Intent(context, CryptoService.class);
        intent.setAction(ACTION_ENCRYPT);
        intent.putExtra(EXTRA_FINGERPRINT, fingerprint);
        intent.putExtra(EXTRA_CLEAR_TEXT, message);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }

    /**
     * Decrypt a message.
     * @param context android context
     * @param message the message to decrypt
     * @param receiver a receiver that will be notified once the message is decrypted
     */
    public static void startActionDecrypt(Context context, String message, ResultReceiver receiver) {
        Contract.throwIfArgNull(context, "context");
        Contract.throwIfArgNull(message, "message");
        Contract.throwIfArgNull(receiver, "receiver");

        Intent intent = new Intent(context, CryptoService.class);
        intent.setAction(ACTION_DECRYPT);
        intent.putExtra(EXTRA_ENCRYPTED_TEXT, message);
        intent.putExtra(EXTRA_RECEIVER, receiver);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Contract.throwIfArgNull(intent, "intent");

        String action = intent.getAction();
        ResultReceiver receiver = intent.getParcelableExtra(EXTRA_RECEIVER);

        if (action.equals(ACTION_ENCRYPT)) {
            String fingerprint = intent.getStringExtra(EXTRA_FINGERPRINT);
            String message = intent.getStringExtra(EXTRA_CLEAR_TEXT);
            handleActionEncrypt(fingerprint, message, receiver);
        } else if (action.equals(ACTION_DECRYPT)) {
            String message = intent.getStringExtra(EXTRA_ENCRYPTED_TEXT);
            handleActionDecrypt(message, receiver);
        }

    }

    private static final String DUMMY_PREFIX = "----- Dummy Encrypted Message -----";

    private void handleActionEncrypt(String fingerprint, String message, ResultReceiver receiver) {
        Contract.throwIfArgNull(fingerprint, "fingerprint");
        Contract.throwIfArgNull(message, "message");
        Contract.throwIfArgNull(receiver, "receiver");

        //TODO do actual encryption
        String encrypted = DUMMY_PREFIX + message;
       /* try {
            if (mKeyManager.getPublicKeyRing().getEncryptionKeys().size() != 0) {
                throw new AssertionError("Expected size 0");
            }
        } catch (IOException ex) {
            throw new AssertionError("IOException", ex);
        } catch (PGPException ex) {
            throw new AssertionError("PGPException", ex);
        }*/

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_FINGERPRINT, fingerprint);
        bundle.putString(EXTRA_ENCRYPTED_TEXT, encrypted);
        receiver.send(RESULT_SUCCESS, bundle);
    }

    private void handleActionDecrypt(String message, ResultReceiver receiver) {
        Contract.throwIfArgNull(message, "message");
        Contract.throwIfArgNull(receiver, "receiver");

        //TODO do actual decryption
        String decrypted = message.replaceAll("^" + DUMMY_PREFIX, "");

        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_CLEAR_TEXT, decrypted);
        receiver.send(RESULT_SUCCESS, bundle);
    }

}
