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

public class InStoreLocationSelection extends AppCompatActivity {
    int frame_size;
    private Button btn_store_location;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store_location_selection);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            frame_size = extras.getInt("FRAME_SIZE");
            //The key argument here must match that used in the other activity
        }

        btn_store_location = (Button) findViewById(R.id.btn_store_location);
        btn_store_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelectFragment fragment = new DialogSelectFragment();
                Bundle bd = new Bundle();
                bd.putString("type", "in-store-location");
                fragment.setArguments(bd);
                replaceFragmentToBackStack(fragment);
            }
        });

        Button btn_select_store = (Button) findViewById(R.id.btn_select_store);
        btn_select_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectedStoreLocation = (String)btn_store_location.getText();

                if (selectedStoreLocation.equals("")) {
                    Utils.showAlertWithTitleNoCancel(InStoreLocationSelection.this, "Missing", "Please select in-store location.");
                }
                else {
                    Intent intent = new Intent(InStoreLocationSelection.this, FrameSelectionActivity.class);
                    intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
                    intent.putExtra("FRAME_SIZE", frame_size);
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
        mFragmentTransaction.replace(R.id.frame_dialog_store, fragment);
        mFragmentTransaction.commit();
    }
}
