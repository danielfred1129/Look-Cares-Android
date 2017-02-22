package thelookcompany.lookcares.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;

import thelookcompany.lookcares.FrameSelectionActivity;
import thelookcompany.lookcares.R;
import thelookcompany.lookcares.utils.UserUtils;


public class NFCReaderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static final String MIME_TEXT_PLAIN = "text/plain";
    public static final String TAG = "LookCares";

    private NfcAdapter mNfcAdapter = null;
    private PendingIntent pendingIntent = null;
    private IntentFilter[] filters = null;
    private String[][] techList = null;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private View rootView;
    private ImageButton btn_nfc_reader;

    public NFCReaderFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NFCReaderFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NFCReaderFragment newInstance(String param1, String param2) {
        NFCReaderFragment fragment = new NFCReaderFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mNfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());

        if (mNfcAdapter == null) {
            // Stop here, we definitely need NFC
            Toast.makeText(getActivity(), "This device doesn't support NFC.", Toast.LENGTH_LONG).show();
            return;

        }
        if (!mNfcAdapter.isEnabled()) {
            Toast.makeText(getActivity(), "NFC is disabled.", Toast.LENGTH_LONG).show();
        }
        setupForegroundDispatch();

        handleIntent(getActivity().getIntent());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_nfcreader, container, false);;
        btn_nfc_reader = (ImageButton)rootView.findViewById(R.id.btn_nfc_reader);
        btn_nfc_reader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNFCRead();
            }
        });

        return rootView;

    }
    @Override
    public void onResume() {
        super.onResume();

        mNfcAdapter.enableForegroundDispatch(getActivity(), pendingIntent, filters, techList);
    }

    @Override
    public void onPause() {
        super.onPause();

        mNfcAdapter.disableForegroundDispatch(getActivity());
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

//    @Override
//    public void onNewIntent(Intent intent) {
//        resolveIntent(intent);
//        mLogger.pushStatus("onNewIntent");
//    }

    private void onNFCRead() {

    }

    public void resolveIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {

            String type = intent.getType();
            if (MIME_TEXT_PLAIN.equals(type)) {

                Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
                new NdefReaderTask().execute(tag);

            } else {
                Log.d(TAG, "Wrong mime type: " + type);
            }
        } else if (NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // In case we would still use the Tech Discovered Intent
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            String[] techList = tag.getTechList();
            String searchedTech = Ndef.class.getName();

            for (String tech : techList) {
                if (searchedTech.equals(tech)) {
                    new NdefReaderTask().execute(tag);
                    break;
                }
            }
        }
    }


    /*
     * @param activity The corresponding {@link Activity} requesting the foreground dispatch.
     * @param adapter The {@link NfcAdapter} used for the foreground dispatch.
     */
    public void setupForegroundDispatch() {
        final Intent intent = new Intent(getActivity().getApplicationContext(), getActivity().getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0, intent, 0);

        filters = new IntentFilter[1];
        techList = new String[][] { getTechList(getActivity().getResources()) };

        // Notice that this is the same filter as in our manifest.
        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Check your mime type.");
        }
    }

    /**
     * Background task for reading the data. Do not block the UI thread while reading.
     *
     * @author Ralf Wondratschek
     *
     */
    private class NdefReaderTask extends AsyncTask<Tag, Void, String> {

        @Override
        protected String doInBackground(Tag... params) {
            Tag tag = params[0];

            Ndef ndef = Ndef.get(tag);
            if (ndef == null) {
                // NDEF is not supported by this Tag.
                return null;
            }

            NdefMessage ndefMessage = ndef.getCachedNdefMessage();

            NdefRecord[] records = ndefMessage.getRecords();
            for (NdefRecord ndefRecord : records) {
                if (ndefRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN && Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
                    try {
                        return readText(ndefRecord);
                    } catch (UnsupportedEncodingException e) {
                        Log.e(TAG, "Unsupported Encoding", e);
                    }
                }
            }

            return null;
        }


        private String readText(NdefRecord record) throws UnsupportedEncodingException {
        /*
         * See NFC forum specification for "Text Record Type Definition" at 3.2.1
         *
         * http://www.nfc-forum.org/specs/
         *
         * bit_7 defines encoding
         * bit_6 reserved for future use, must be 0
         * bit_5..0 length of IANA language code
         */

            byte[] payload = record.getPayload();

            // Get the Text Encoding
            String textEncoding = ((payload[0] & 128) == 0) ? "UTF-8" : "UTF-16";

            // Get the Language Code
            int languageCodeLength = payload[0] & 0063;

            // String languageCode = new String(payload, 1, languageCodeLength, "US-ASCII");
            // e.g. "en"

            // Get the Text
            return new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
        }

        @Override
        protected void onPostExecute(final String result) {
            if (result != null) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Serial Number");
                builder.setMessage(result);

                builder.setPositiveButton("Use this scan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        UserUtils.storeSelectedBarcode(getActivity(), result);
                        if (((FrameSelectionActivity)getActivity()).value.equals("FRAME"))
                            ((FrameSelectionActivity)getActivity()).getFrameWithSerialNumber(result);
                        else if (((FrameSelectionActivity)getActivity()).value.equals("FABRIC"))
                            ((FrameSelectionActivity)getActivity()).getFabricWithSerialNumber(result);
                    }
                });
                builder.setNegativeButton("Rescan", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        //TODO
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                Toast.makeText(getActivity(), result, Toast.LENGTH_LONG).show();
            }
        }
    }

    private static String[] getTechList(Resources resources) {
        ArrayList<String> list = new ArrayList<String>();
        XmlResourceParser xml = resources.getXml(R.xml.techlist);

        // Assumed simple parsing because of simple file structure
        for (;;) {
            int eventType = XmlPullParser.END_DOCUMENT;
            try {
                eventType = xml.next();
            } catch (XmlPullParserException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if (eventType == XmlPullParser.TEXT) {
                list.add(xml.getText());
            } else if (eventType == XmlPullParser.END_DOCUMENT) {
                break;
            }

        }

        return list.toArray(new String[list.size()]);
    }
}

