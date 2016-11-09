package thelookcompany.lookcares;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

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
//                if (txt_username.getText().toString().isEmpty()) {
//                    txt_username.setError("Cannot be blank");
//                    return;
//                }
//                if (txt_password.getText().toString().isEmpty()) {
//                    txt_password.setError("Cannot be blank");
//                    return;
//                }
                Intent intent = new Intent(LoginActivity.this, LocationSelectionActivity.class);
                startActivity(intent);
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
    }
    @Override
    public void onBackPressed() {
        // Do Here what ever you want do on back press;
    }
}
