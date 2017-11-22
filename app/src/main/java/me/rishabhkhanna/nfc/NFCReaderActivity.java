package me.rishabhkhanna.nfc;

import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.w3c.dom.Text;

import static android.R.attr.tag;

public class NFCReaderActivity extends AppCompatActivity {

    public static final String TAG = "NFC";
    TextView tvTag;
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent != null && NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] rawMessages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMessages != null) {
                NdefMessage[] messages = new NdefMessage[rawMessages.length];
                for (int i = 0; i < rawMessages.length; i++) {
                    messages[i] = (NdefMessage) rawMessages[i];
                    Log.d(TAG, "onNewIntent: Ndef message " + i + " : " + messages[i]);
                }
            }
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            Log.d(TAG, "onNewIntent: " + tag);
        }
        Log.d(TAG, "onNewIntent: ");
        tvTag = (TextView) findViewById(R.id.tvTag);
        tvTag.setText(String.valueOf(tag));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcreader);
        tvTag = (TextView) findViewById(R.id.tvTag);
        Log.d(TAG, "onCreate: " + getIntent().getAction());
        Log.d(TAG, "onCreate: " + getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG));

        Parcelable[] rawMessages = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        if (rawMessages != null) {
            NdefMessage[] messages = new NdefMessage[rawMessages.length];
            for (int i = 0; i < rawMessages.length; i++) {
                messages[i] = (NdefMessage) rawMessages[i];
                Log.d(TAG, "onNewIntent: Ndef message " + i + " : " + messages[i]);
            }
        }
    }
}
