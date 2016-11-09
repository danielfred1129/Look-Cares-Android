package thelookcompany.lookcares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import thelookcompany.lookcares.fragments.DialogSelectFragment;

public class LocationSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);

        Button btn_client = (Button) findViewById(R.id.btn_client);
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
        Button btn_location = (Button) findViewById(R.id.btn_location);
        btn_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelectFragment fragment = new DialogSelectFragment();
                Bundle bd = new Bundle();
                bd.putString("type", "location");
                fragment.setArguments(bd);
                replaceFragmentToBackStack(fragment);
            }
        });
        Button btn_select = (Button) findViewById(R.id.btn_select);
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LocationSelectionActivity.this, FrameSelectionActivity.class);
                intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FRAME");
                startActivity(intent);
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
}
