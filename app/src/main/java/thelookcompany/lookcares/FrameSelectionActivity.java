package thelookcompany.lookcares;

import android.content.Intent;
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
    public TextView lbl_serial_number;

    private Button btn_nfc_tap_frame, btn_bar_code_frame, btn_text_input_frame, btn_select_frame;
    String value;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_selection);

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
        lbl_serial_number = (TextView) findViewById(R.id.lbl_serial_number);

        btn_nfc_tap_frame = (Button) findViewById(R.id.btn_nfc_tap_frame);
        btn_nfc_tap_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lbl_serial_number.setVisibility(View.GONE);
            }
        });
        btn_bar_code_frame = (Button) findViewById(R.id.btn_bar_code_frame);
        btn_bar_code_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lbl_serial_number.setVisibility(View.VISIBLE);
                lbl_serial_number.setText("Serial Number");
                BarCodeReaderFragment fragment = new BarCodeReaderFragment();
                replaceFragment(fragment);
            }
        });
        btn_text_input_frame = (Button) findViewById(R.id.btn_text_input_frame);
        btn_text_input_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lbl_serial_number.setVisibility(View.GONE);
                TextInputFragment fragment = new TextInputFragment();
                replaceFragment(fragment);
            }
        });
        btn_select_frame = (Button) findViewById(R.id.btn_select_frame);
        btn_select_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (value.equals("Frame Selection"))
                    intent = new Intent(FrameSelectionActivity.this, InStoreLocationSelection.class);
                else
                    intent = new Intent(FrameSelectionActivity.this, TakePictureActivity.class);
                startActivity(intent);
            }
        });
    }
    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_serial_number, fragment);
        mFragmentTransaction.commit();
    }
}
