package ch.epfl.sweng.bohdomp.dialogue.ui.conversationList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.bouncycastle.openpgp.PGPException;

import java.io.IOException;

import ch.epfl.sweng.bohdomp.dialogue.crypto.KeyManager;
import ch.epfl.sweng.bohdomp.dialogue.crypto.hkp.HkpServerException;
import ch.epfl.sweng.bohdomp.dialogue.utils.Contract;
import ch.epfl.sweng.bohdomp.dialogue.utils.Either;
import ch.epfl.sweng.bohdomp.dialogue.utils.Left;
import ch.epfl.sweng.bohdomp.dialogue.utils.Right;

/**
 * Helper task that supports generating keys
 */
public class KeyGenerator extends AsyncTask<Void, Void, Either<Void, String>> {
    private Context mContext;
    private KeyManager mKeyManager;
    private ProgressDialog mProgress;

    public KeyGenerator(Context context, ProgressDialog progress) {
        Contract.throwIfArgNull(context, "context");
        Contract.throwIfArgNull(progress, "progress");

        this.mContext = context;
        this.mKeyManager = KeyManager.getInstance(context);
        this.mProgress = progress;
    }

    private Either<Void, String> failure(String message, Exception exception) {
        Log.e("KeyGeneration", message, exception);
        return new Right<Void, String>(message);
    }

    @Override
    public void onPreExecute() {
        mProgress.show();
    }

    @Override
    public Either<Void, String> doInBackground(Void... unused) {
        Log.i("KeyGeneration", "Generating keypair");
        try {
            String pass = "pass"; //get password from user or generate random
            String id = "john carpenter"; //get id from user or generate random
            String fingerprint = mKeyManager.createKeyPair(id, pass);
            mKeyManager.setOwn(fingerprint, pass);
            return new Left<Void, String>(null);
        } catch (IOException ex) {
            return failure("Error occurred while saving keys", ex);
        } catch (PGPException ex) {
            return failure("PGP exception occured", ex);
        } catch (HkpServerException ex) {
            return failure("Error contacting key server, try again later", ex);
        }
    }

    @Override
    public void onPostExecute(Either<Void, String> result) {
        mProgress.dismiss();
        if (result.isRight()) {
            Toast.makeText(mContext, result.getRight(), Toast.LENGTH_LONG).show();
        }
    }

    public static void showDialog(Activity activity) {
        Contract.throwIfArgNull(activity, "activity");

        ProgressDialog progress = new ProgressDialog(activity);
        KeyGenerator generator = new KeyGenerator(activity, progress);
        progress.setMessage("Generating private key");
        generator.execute();
    }
}
