package thelookcompany.lookcares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import thelookcompany.lookcares.datamodel.UserObject;
import thelookcompany.lookcares.network.LookCaresResponseHandler;
import thelookcompany.lookcares.utils.UserUtils;
import thelookcompany.lookcares.utils.Utils;

public class RemoveFabricSingleActivity extends AppCompatActivity {
    ImageView img_fabric1;
    ImageButton btn_chk_fabric1;
    String fabricKey1;
    Integer stage_status = 0;
    Button btn_remove;
    boolean status_Fabric1 = false;
    JSONArray fabrics;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_fabric_single);

        status_Fabric1 = false;
        img_fabric1 = (ImageView) findViewById(R.id.img_fabric_single);
        btn_chk_fabric1 = (ImageButton) findViewById(R.id.btn_chk_fabric_single);
        btn_chk_fabric1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status_Fabric1 = !status_Fabric1;
                updateFabricStatus();
            }
        });

        btn_remove = (Button) findViewById(R.id.btn_remove);
        btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stage_status = 1;
                //Delete first fabric
                deleteFabric(fabricKey1);
            }
        });
        try {
            String tempStr = UserUtils.getInstalledFabrics(RemoveFabricSingleActivity.this);
            fabrics = new JSONArray(tempStr);
            JSONObject fabric1 = fabrics.getJSONObject(0);
            String img_url = fabric1.getString("vcFileName");
            fabricKey1 = fabric1.getString("kFabric");
            img_url = "http://files.lookcares.com/files/" + img_url;
            final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
            Glide.with(this).load(img_url).into(img_fabric1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateFabricStatus();
    }
    private void deleteFabric(String fabricKey) {
        UserObject user = UserUtils.getSession(RemoveFabricSingleActivity.this);
        String token = user.getToken();

        AsyncHttpClient client = new AsyncHttpClient();
        String authorization = "base " + token;
        client.addHeader("Authorization", authorization);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.delete(Utils.BASE_URL + "Frames/Fabric/" + fabricKey, new LookCaresResponseHandler(RemoveFabricSingleActivity.this) {
            @Override
            public void onFailure(int statusCode, Header[] headers,	String responseString, Throwable throwable) {
                if (statusCode == 200) {
                    Toast.makeText(RemoveFabricSingleActivity.this, "The fabric removed successfully!", Toast.LENGTH_LONG).show();
                    toNextActivity();
                }
                else
                    Toast.makeText(RemoveFabricSingleActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                Toast.makeText(RemoveFabricSingleActivity.this, "The fabric removed successfully!", Toast.LENGTH_LONG).show();
                toNextActivity();
            }
        });
    }
    private void toNextActivity() {
        Intent intent;
        JSONObject frame = null;
        try {
            frame = new JSONObject(UserUtils.getSelectedFrame(RemoveFabricSingleActivity.this));
            String vcExtrusion = frame.getString("vcExtrusion");
            intent = new Intent(RemoveFabricSingleActivity.this, FrameSelectionActivity.class);
            intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
            intent.putExtra("FRAME_SIZE", 1);
            startActivity(intent);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void updateFabricStatus() {
        if (status_Fabric1)
            btn_chk_fabric1.setImageResource(R.drawable.chb_rounded_small);
        else
            btn_chk_fabric1.setImageResource(android.R.color.transparent);
    }
}
