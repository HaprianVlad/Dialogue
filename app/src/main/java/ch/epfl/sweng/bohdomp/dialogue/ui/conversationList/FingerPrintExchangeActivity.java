package ch.epfl.sweng.bohdomp.dialogue.ui.conversationList;

import android.app.Activity;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.NfcEvent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import ch.epfl.sweng.bohdomp.dialogue.R;
import ch.epfl.sweng.bohdomp.dialogue.crypto.KeyManager;

/**
 * @author swengTeam 2014 BohDomp
 * Activity enables exchange of fingerprint for crypto
 */
public class FingerPrintExchangeActivity extends Activity implements NfcAdapter.OnNdefPushCompleteCallback {
    public static final String SPLIT_NFC = "#!#";

    private Button mCancelButton;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_exchange);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        mNfcAdapter.setOnNdefPushCompleteCallback(this, this);

        setupView();
        setupListeners();
    }

    private void setupView() {
        mCancelButton = (Button) findViewById(R.id.button_cancel);
    }

    private void setupListeners() {
        mCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onResume() {
        mNfcAdapter.setNdefPushMessage(createNDefMessage(), this);
        super.onResume();
    }

    private NdefMessage createNDefMessage() {
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();

        String stringOut = KeyManager.getInstance(getApplicationContext()).getOwnFingerprint()
                + SPLIT_NFC + phoneNumber;

        byte[] bytesOut = stringOut.getBytes();

        NdefRecord nDefRecordOut = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA,
                "text/fingerprint".getBytes(),
                new byte[] {},
                bytesOut);

        NdefMessage nDefMessageOut = new NdefMessage(nDefRecordOut);

        return nDefMessageOut;
    }

    @Override
    public void onNdefPushComplete(NfcEvent nfcEvent) {
        finish();
    }
}
