package thelookcompany.lookcares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class RemoveFabricActivity extends AppCompatActivity {
    ImageView img_fabric1, img_fabric2;
    ImageButton btn_chk_fabric1, btn_chk_fabric2;
    Button btn_remove;
    boolean status_Fabric1 = false, status_Fabric2 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_fabric);
        status_Fabric1 = false;
        status_Fabric2 = false;
        img_fabric1 = (ImageView) findViewById(R.id.img_fabric1);
        img_fabric2 = (ImageView) findViewById(R.id.img_fabric2);
        btn_chk_fabric1 = (ImageButton) findViewById(R.id.btn_chk_fabric1);
        btn_chk_fabric1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status_Fabric1 = !status_Fabric1;
                updateFabricStatus();
            }
        });
        btn_chk_fabric2 = (ImageButton) findViewById(R.id.btn_chk_fabric2);
        btn_chk_fabric2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status_Fabric2 = !status_Fabric2;
                updateFabricStatus();
            }
        });
        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RemoveFabricActivity.this, FrameSelectionActivity.class);
                intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
                startActivity(intent);
            }
        });
        updateFabricStatus();
    }
    private void updateFabricStatus() {
        if (status_Fabric1)
            btn_chk_fabric1.setImageResource(R.drawable.chb_rounded_small);
        else
            btn_chk_fabric1.setImageResource(android.R.color.transparent);
        if (status_Fabric2)
            btn_chk_fabric2.setImageResource(R.drawable.chb_rounded_small);
        else
            btn_chk_fabric2.setImageResource(android.R.color.transparent);
    }
}
