package thelookcompany.lookcares;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import thelookcompany.lookcares.fragments.BarCodeReaderFragment;
import thelookcompany.lookcares.fragments.TextInputFragment;


public class FrameSelectionActivity extends AppCompatActivity {

    private Button btn_nfc_tap_frame, btn_bar_code_frame, btn_text_input_frame, btn_select_frame;
    String value;
    int status = 0;
    boolean installed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_selection);

        status = 0;
//        installed = true;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("SERIAL_NUMBER_SELECTION_TYPE");
            TextView lbl_selection_type = (TextView) findViewById(R.id.lbl_selection_type);
            if (value.equals("FRAME"))
                lbl_selection_type.setText("Frame Selection");
            else if (value.equals("FABRIC"))
                lbl_selection_type.setText("Fabric Selection");

            //The key argument here must match that used in the other activity
        }

        btn_nfc_tap_frame = (Button) findViewById(R.id.btn_nfc_tap_frame);
        btn_nfc_tap_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 0;
                updateTapButtons();
            }
        });
        btn_bar_code_frame = (Button) findViewById(R.id.btn_bar_code_frame);
        btn_bar_code_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 1;
                updateTapButtons();
                BarCodeReaderFragment fragment = new BarCodeReaderFragment();
                replaceFragment(fragment);
            }
        });
        btn_text_input_frame = (Button) findViewById(R.id.btn_text_input_frame);
        btn_text_input_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 2;
                updateTapButtons();
                TextInputFragment fragment = new TextInputFragment();
                replaceFragment(fragment);
            }
        });
        btn_select_frame = (Button) findViewById(R.id.btn_select_frame);
        btn_select_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (value.equals("FRAME")) {
                    if (installed) {
                        intent = new Intent(FrameSelectionActivity.this, RemoveFabricActivity.class);
                    } else {
                        intent = new Intent(FrameSelectionActivity.this, InStoreLocationSelection.class);
                    }
                }
                else
                    intent = new Intent(FrameSelectionActivity.this, TakePictureActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updateTapButtons() {
        if (status == 0)
        {
            btn_nfc_tap_frame.setTypeface(null, Typeface.BOLD);
            btn_bar_code_frame.setTypeface(null, Typeface.NORMAL);
            btn_text_input_frame.setTypeface(null, Typeface.NORMAL);
            btn_nfc_tap_frame.setBackgroundResource(R.drawable.tap_btn_back);
            btn_bar_code_frame.setBackgroundColor(Color.BLACK);
            btn_text_input_frame.setBackgroundColor(Color.BLACK);
        }
        else if (status == 1)
        {
            btn_nfc_tap_frame.setTypeface(null, Typeface.NORMAL);
            btn_bar_code_frame.setTypeface(null, Typeface.BOLD);
            btn_text_input_frame.setTypeface(null, Typeface.NORMAL);
            btn_nfc_tap_frame.setBackgroundColor(Color.BLACK);
            btn_bar_code_frame.setBackgroundResource(R.drawable.tap_btn_back);
            btn_text_input_frame.setBackgroundColor(Color.BLACK);
        }
        else if (status == 2)
        {
            btn_nfc_tap_frame.setTypeface(null, Typeface.NORMAL);
            btn_bar_code_frame.setTypeface(null, Typeface.NORMAL);
            btn_text_input_frame.setTypeface(null, Typeface.BOLD);
            btn_nfc_tap_frame.setBackgroundColor(Color.BLACK);
            btn_bar_code_frame.setBackgroundColor(Color.BLACK);
            btn_text_input_frame.setBackgroundResource(R.drawable.tap_btn_back);
        }
    }
    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_serial_number, fragment);
        mFragmentTransaction.commit();
    }
}
