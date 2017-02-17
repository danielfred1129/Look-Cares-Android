package thelookcompany.lookcares.fragments;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import thelookcompany.lookcares.R;
import thelookcompany.lookcares.nfc_handlers.Logger;
import thelookcompany.lookcares.nfc_handlers.NfcHandler;
import thelookcompany.lookcares.nfc_handlers.NfcStatus;


public class NFCReaderFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NfcAdapter nfcAdapter;

    private View rootView;
    private ImageButton btn_nfc_reader;

    private Logger mLogger = null;
    private NfcHandler mNfcHandler = null;
    private NfcStatus mStatus = null;

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

        mLogger = new Logger();
        mLogger.pushStatus("onCreate");

        // NfcStatus
        mStatus = new NfcStatus();

        // NfcHandler
        Activity activity = getActivity();
        mNfcHandler = new NfcHandler(getActivity(), mStatus, mLogger);

        // Passing intent
        Intent intent = getActivity().getIntent();
        resolveIntent(intent);
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
        mNfcHandler.onResume();
        mLogger.pushStatus("onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        mNfcHandler.onPause();
        mLogger.pushStatus("onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        mLogger.pushStatus("onStop");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLogger.pushStatus("onDestroy");
    }

//    @Override
//    public void onNewIntent(Intent intent) {
//        resolveIntent(intent);
//        mLogger.pushStatus("onNewIntent");
//    }

    private void onNFCRead() {

    }

    public Logger getLogger() {
        return mLogger;
    }

    public NfcStatus getStatus() {
        return mStatus;
    }

    public NfcHandler getNfcHandler() {
        return mNfcHandler;
    }

    public void resolveIntent(Intent intent) {
        if(mNfcHandler.isNfcIntent(intent)) {
            mNfcHandler.handleNfcIntent(intent);
        }
   }
}
