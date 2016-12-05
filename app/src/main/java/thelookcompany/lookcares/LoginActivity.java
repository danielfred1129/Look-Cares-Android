package thelookcompany.lookcares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import thelookcompany.lookcares.datamodel.UserObject;
import thelookcompany.lookcares.network.LookCaresResponseHandler;
import thelookcompany.lookcares.utils.UserUtils;
import thelookcompany.lookcares.utils.Utils;

public class LoginActivity extends AppCompatActivity {

    private EditText txt_username, txt_password;
    private ImageButton btn_rememberme;
    private Button btn_login;
    private Boolean isRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        isRemember = false;
        txt_username = (EditText) findViewById(R.id.txt_username);
        txt_password = (EditText) findViewById(R.id.txt_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLogin();
            }
        });

        btn_rememberme = (ImageButton) findViewById(R.id.btn_rememberme);
        btn_rememberme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isRemember = !isRemember;
                if (isRemember)
                    btn_rememberme.setImageResource(R.drawable.checkbox);
                else
                    btn_rememberme.setImageResource(R.drawable.checkbox_square);
            }
        });

        String str_remember = UserUtils.getRememberMe(LoginActivity.this);
        if (str_remember != null) {
            UserObject user = UserUtils.getSession(this);
            if (user != null) {
                txt_username.setText(user.getUserName());
                txt_password.setText(user.getUserPass());
                onLogin();
            }
        }
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }

    private void onLogin() {
//        if (txt_username.getText().toString().isEmpty()) {
//            txt_username.setError("Cannot be blank");
//            return;
//        }
//        if (txt_password.getText().toString().isEmpty()) {
//            txt_password.setError("Cannot be blank");
//            return;
//        }
        RequestParams params = new RequestParams();
//        params.put("username", txt_username.getText().toString());
//        params.put("password", txt_password.getText().toString());

        params.put("username", "custadmin");
        params.put("password", "p@ssword!");



        AsyncHttpClient client = new AsyncHttpClient();
        client.post(Utils.BASE_URL + "Auth/Login", params, new LookCaresResponseHandler(this) {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        JSONObject json = (JSONObject)response;

                        UserObject user = new UserObject();
                        user.setClientKey(json.getString("clientKey"));
                        user.setUserPass(txt_password.getText().toString());
                        user.setUserName(json.getString("userName"));
                        user.setToken(json.getString("token"));
                        user.setUserKey(json.getString("userKey"));

                        if (isRemember)
                            UserUtils.storeRememberMe(LoginActivity.this, "yes");
                        UserUtils.storeSession(LoginActivity.this, user);
                        Intent intent = new Intent(LoginActivity.this, LocationSelectionActivity.class);
                        startActivity(intent);

                    } catch (JSONException e) {
                        Utils.showAlert(LoginActivity.this, "Failed to login!");
                    }
                }
            }
        });
    }
}
