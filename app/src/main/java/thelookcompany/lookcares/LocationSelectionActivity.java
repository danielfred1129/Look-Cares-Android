package thelookcompany.lookcares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import thelookcompany.lookcares.fragments.DialogSelectFragment;
import thelookcompany.lookcares.utils.Utils;

public class LocationSelectionActivity extends AppCompatActivity implements DialogSelectFragment.onSelectFragmentListener {
    public String selectedClientName="";
    private Button btn_client, btn_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        btn_client = (Button) findViewById(R.id.btn_client);
        btn_client.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSelectFragment fragment = new DialogSelectFragment();
                Bundle bd = new Bundle();
                bd.putString("type", "client");
                fragment.setArguments(bd);
                replaceFragmentToBackStack(fragment);
            }
        });
        btn_location = (Button) findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedClient = (String)btn_client.getText();
                if (selectedClient.equals("")) {
                    Utils.showAlertWithTitleNoCancel(LocationSelectionActivity.this, "Warning", "Please select the client");
                }
                else
                {
                    DialogSelectFragment fragment = new DialogSelectFragment();
                    Bundle bd = new Bundle();
                    bd.putString("type", "location");
                    fragment.setArguments(bd);
                    replaceFragmentToBackStack(fragment);
                }
//                if (!selectedClient.isEmpty()) {
//                    DialogSelectFragment fragment = new DialogSelectFragment();
//                    Bundle bd = new Bundle();
//                    bd.putString("type", "location");
//                    fragment.setArguments(bd);
//                    replaceFragmentToBackStack(fragment);
//                }
//                else
//                {
//                    Utils.showAlertWithTitleNoCancel(LocationSelectionActivity.this, "Missing", "Please select the client");
//                }
            }
        });
        Button btn_select = (Button) findViewById(R.id.btn_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedClient = (String)btn_client.getText();
                String selectedLocation = (String)btn_location.getText();
                if ((selectedClient.equals("")) || (selectedLocation.equals(""))){
                    Utils.showAlertWithTitleNoCancel(LocationSelectionActivity.this, "Missing", "Please select client and location");
                }
                else {
                    Intent intent = new Intent(LocationSelectionActivity.this, FrameSelectionActivity.class);
                    intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FRAME");
                    startActivity(intent);
                }
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void replaceFragmentToBackStack (DialogSelectFragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.addToBackStack(fragment.getClass().getName());
        mFragmentTransaction.replace(R.id.frame_dialog, fragment);
        mFragmentTransaction.commit();
    }

    @Override
    public void onClientSelection(String s) {
        btn_client.setText(s);
    }
}
