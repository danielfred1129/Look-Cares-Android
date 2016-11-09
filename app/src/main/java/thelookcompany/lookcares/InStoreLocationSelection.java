package thelookcompany.lookcares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import thelookcompany.lookcares.fragments.DialogSelectFragment;

public class InStoreLocationSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_store_location_selection);

        Button btn_store_location = (Button) findViewById(R.id.btn_store_location);
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
                Intent intent = new Intent(InStoreLocationSelection.this, FrameSelectionActivity.class);
                intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
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
        mFragmentTransaction.replace(R.id.frame_dialog_store, fragment);
        mFragmentTransaction.commit();
    }
}
