package me.rishabhkhanna.nfc;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.os.Parcelable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.charset.Charset;

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
        Toast.makeText(this, "new tag", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onNewIntent: ");
        tvTag = (TextView) findViewById(R.id.tvTag);
        tvTag.setText(String.valueOf(tag));
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setMessage("Wheel char detected")
                .setPositiveButton("switch on", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        
                    }
                }).setNegativeButton("switch off", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                
            }
        }).show();

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon()
        
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfcreader);
        tvTag = (TextView) findViewById(R.id.tvTag);
        Log.d(TAG, "onCreate: " + getIntent().getAction());
        Log.d(TAG, "onCreate: " + getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG));
        Parcelable[] rawMessages = getIntent().getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
        Log.d(TAG, "onCreate: " + MifareUltralight.get((Tag) getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG)));
        Tag tag = getIntent().getParcelableExtra(NfcAdapter.EXTRA_TAG);
        Log.d(TAG, "onCreate: Id: " + tag.getId());

        Log.d(TAG, "onCreate: ID data : " + bytesToHexString(tag.getId()));
        String[] list = tag.getTechList();
        for(String s : list){
            Log.d(TAG, "onCreate: " + s);
        }
        Log.d(TAG, "onCreate: " + tag.describeContents());

    }

    public String readTag(Tag tag){
        MifareUltralight mifareUltralight = MifareUltralight.get(tag);
        try {
            mifareUltralight.connect();
            byte[] payload = mifareUltralight.readPages(4);
            return new String(payload, Charset.forName("US-ASCII"));
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "readTag: " + e.getMessage());
        }finally {
            if(mifareUltralight != null){
                try {
                    mifareUltralight.close();
                } catch (IOException e) {
                    Log.d(TAG, "readTag: error in closing the tag" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder("0x");
        if (src == null || src.length <= 0) {
            return null;
        }

        char[] buffer = new char[2];
        for (int i = 0; i < src.length; i++) {
            buffer[0] = Character.forDigit((src[i] >>> 4) & 0x0F, 16);
            buffer[1] = Character.forDigit(src[i] & 0x0F, 16);
            System.out.println(buffer);
            stringBuilder.append(buffer);
        }

        return stringBuilder.toString();
    }
}
