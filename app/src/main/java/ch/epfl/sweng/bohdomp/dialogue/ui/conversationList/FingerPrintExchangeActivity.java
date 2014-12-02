package ch.epfl.sweng.bohdomp.dialogue.ui.conversationList;

import android.app.Activity;
import android.content.Context;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

import ch.epfl.sweng.bohdomp.dialogue.R;

/**
 * @author swengTeam 2014 BohDomp
 * Activity enables exchange of fingerprint for crypto
 */
public class FingerPrintExchangeActivity extends Activity{

    private Button mCancelButton;
    private NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_print_exchange);

        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

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
        mNfcAdapter.setNdefPushMessage(createNdefMessage(), this);
        super.onResume();
    }

    public NdefMessage createNdefMessage() {
        TelephonyManager tMgr = (TelephonyManager) getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
        String phoneNumber = tMgr.getLine1Number();

        String fingerprint = "FINGERPRINT : ";
        String stringOut = fingerprint + phoneNumber;

        byte[] bytesOut = stringOut.getBytes();

        NdefRecord nDefRecordOut = new NdefRecord(
                NdefRecord.TNF_MIME_MEDIA,
                "text/fingerprint".getBytes(),
                new byte[] {},
                bytesOut);

        NdefMessage nDefMessageOut = new NdefMessage(nDefRecordOut);

        return nDefMessageOut;
    }
}
