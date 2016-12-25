package thelookcompany.lookcares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import thelookcompany.lookcares.datamodel.UserObject;
import thelookcompany.lookcares.network.LookCaresResponseHandler;
import thelookcompany.lookcares.utils.UserUtils;
import thelookcompany.lookcares.utils.Utils;

public class RemoveFabricActivity extends AppCompatActivity {
    ImageView img_fabric1, img_fabric2;
    ImageButton btn_chk_fabric1, btn_chk_fabric2;
    String fabricKey1, fabricKey2;
    Button btn_remove;
    boolean status_Fabric1 = false, status_Fabric2 = false;
    JSONArray fabrics;


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
                if (fabrics.length() == 2 && status_Fabric2 == true &&  status_Fabric1 == true)
                {

                    //Delete first fabric
                    UserObject user = UserUtils.getSession(RemoveFabricActivity.this);
                    String token = user.getToken();

                    AsyncHttpClient client = new AsyncHttpClient();
                    String authorization = "base " + token;
                    client.addHeader("Authorization", authorization);
                    client.addHeader("Content-Type", "application/json");
                    client.addHeader("Accept", "application/json");
                    client.delete(Utils.BASE_URL + "Frames/Fabric/" + fabricKey1, new LookCaresResponseHandler(RemoveFabricActivity.this) {
                        @Override
                        public void onFailure(int statusCode, Header[] headers,	String responseString, Throwable throwable) {
                            if (statusCode == 200) {
                                deleteFabric(fabricKey2);
                            }
                            else
                                Toast.makeText(RemoveFabricActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
                        }
                        @Override
                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                            super.onSuccess(statusCode, headers, response);
                            deleteFabric(fabricKey2);
                        }
                    });
                }
                else if (fabrics.length() == 2 && status_Fabric1 == true)
                {

                    //Delete first fabric
                    deleteFabric(fabricKey1);
                }
                else if (fabrics.length() == 2 && status_Fabric2 == true )
                {

                    //Delete first fabric
                    deleteFabric(fabricKey2);
                }
                else if (status_Fabric1) {
                    deleteFabric(fabricKey1);
                }
                if (status_Fabric2) {
                    deleteFabric(fabricKey2);
                }
            }
        });
        try {
            String tempStr = UserUtils.getInstalledFabrics(RemoveFabricActivity.this);
            fabrics = new JSONArray(tempStr);
            if (fabrics.length() == 1)
            {
                JSONObject fabric1 = fabrics.getJSONObject(0);
                String img_url = fabric1.getString("vcFileName");
                fabricKey1 = fabric1.getString("kFabric");
                img_url = "http://files.lookcares.com/files/" + img_url;
                final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();
                ImageLoader.getInstance().displayImage(img_url, img_fabric1, options);
            }
            else if (fabrics.length() == 2)
            {
                final DisplayImageOptions options = new DisplayImageOptions.Builder().cacheOnDisk(true).build();

                JSONObject fabric1 = fabrics.getJSONObject(0);
                String img_url = fabric1.getString("vcFileName");
                img_url = "http://files.lookcares.com/files/" + img_url;
                fabricKey1 = fabric1.getString("kFabric");
                ImageLoader.getInstance().displayImage(img_url, img_fabric1, options);

                JSONObject fabric2 = fabrics.getJSONObject(1);
                String img_url2 = fabric2.getString("vcFileName");
                fabricKey2 = fabric2.getString("kFabric");
                img_url2 = "http://files.lookcares.com/files/" + img_url2;
                ImageLoader.getInstance().displayImage(img_url2, img_fabric2, options);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        updateFabricStatus();
    }
    private void deleteFabric(String fabricKey) {
        UserObject user = UserUtils.getSession(RemoveFabricActivity.this);
        String token = user.getToken();

        AsyncHttpClient client = new AsyncHttpClient();
        String authorization = "base " + token;
        client.addHeader("Authorization", authorization);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.delete(Utils.BASE_URL + "Frames/Fabric/" + fabricKey, new LookCaresResponseHandler(RemoveFabricActivity.this) {
            @Override
            public void onFailure(int statusCode, Header[] headers,	String responseString, Throwable throwable) {
                if (statusCode == 200) {
                    if (fabrics.length() == 2)
                        Toast.makeText(RemoveFabricActivity.this, "The fabrics removed successfully!", Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(RemoveFabricActivity.this, "The fabric removed successfully!", Toast.LENGTH_LONG).show();
                    toNextActivity();
                }
                else
                    Toast.makeText(RemoveFabricActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                if (fabrics.length() == 2)
                    Toast.makeText(RemoveFabricActivity.this, "The fabrics removed successfully!", Toast.LENGTH_LONG).show();
                else
                    Toast.makeText(RemoveFabricActivity.this, "The fabric removed successfully!", Toast.LENGTH_LONG).show();
                toNextActivity();
            }
        });
    }
    private void toNextActivity() {
        Intent intent;
        JSONObject frame = null;
        try {
            frame = new JSONObject(UserUtils.getSelectedFrame(RemoveFabricActivity.this));
            String vcExtrusion = frame.getString("vcExtrusion");
            if (vcExtrusion.equals("120mm") || (vcExtrusion.equals("36mm")) || (vcExtrusion.equals("50mm")))
            {
                intent = new Intent(RemoveFabricActivity.this, FrameSelectionActivity.class);
                intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
                intent.putExtra("FRAME_SIZE", 2);
                startActivity(intent);
            }
            else
            {
                intent = new Intent(RemoveFabricActivity.this, FrameSelectionActivity.class);
                intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
                intent.putExtra("FRAME_SIZE", 1);
                startActivity(intent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
