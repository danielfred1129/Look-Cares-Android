package thelookcompany.lookcares;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import thelookcompany.lookcares.datamodel.UserObject;
import thelookcompany.lookcares.fragments.BarCodeReaderFragment;
import thelookcompany.lookcares.fragments.NFCReaderFragment;
import thelookcompany.lookcares.fragments.TextInputFragment;
import thelookcompany.lookcares.network.LookCaresResponseHandler;
import thelookcompany.lookcares.utils.UserUtils;
import thelookcompany.lookcares.utils.Utils;


public class FrameSelectionActivity extends AppCompatActivity {

    private Button btn_nfc_tap_frame, btn_bar_code_frame, btn_text_input_frame, btn_select_frame;
    public String value;
    int frame_size;
    int status = 0;
    boolean installed;
    private JSONObject selectedFrame, selectedFabric;
    private String serialNumber;
    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_selection);
        status = 0;
//        installed = true;

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            value = extras.getString("SERIAL_NUMBER_SELECTION_TYPE");
            frame_size = extras.getInt("FRAME_SIZE");
            TextView lbl_selection_type = (TextView) findViewById(R.id.lbl_selection_type);
            if (value.equals("FRAME"))
                lbl_selection_type.setText("Frame Selection");
            else if (value.equals("FABRIC"))
                lbl_selection_type.setText("Fabric Selection");

            //The key argument here must match that used in the other activity
        }

        btn_nfc_tap_frame = (Button) findViewById(R.id.btn_nfc_tap_frame);
        btn_nfc_tap_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 0;
                updateTapButtons();

            }
        });
        btn_bar_code_frame = (Button) findViewById(R.id.btn_bar_code_frame);
        btn_bar_code_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 1;
                updateTapButtons();

            }
        });
        btn_text_input_frame = (Button) findViewById(R.id.btn_text_input_frame);
        btn_text_input_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                status = 2;
                updateTapButtons();

            }
        });
        btn_select_frame = (Button) findViewById(R.id.btn_select_frame);
        btn_select_frame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (status == 0)
                    serialNumber = "";
                else if (status == 1)
                    serialNumber = UserUtils.getSelectedBarcode(FrameSelectionActivity.this);
                else if (status == 2)
                    serialNumber = ((EditText)findViewById(R.id.txt_input_serial_number)).getText().toString();

                if (serialNumber.equals(""))
                {
                    Utils.showAlertWithTitleNoCancel(FrameSelectionActivity.this, "Warning", "Please input Serial Number");
                }
                else
                {
                    if (value.equals("FRAME")) {
                        getFrameWithSerialNumber(serialNumber);
                    }
                    else if (value.equals("FABRIC")) {
                        getFabricWithSerialNumber(serialNumber);
                    }
                }

            }
        });
        updateTapButtons();
    }

    @Override
    public void onNewIntent(Intent intent) {
//        resolveIntent(intent);
//        mLogger.pushStatus("onNewIntent");
        if (fragment instanceof NFCReaderFragment) {
            NFCReaderFragment myFragement = (NFCReaderFragment) fragment;
            // Pass intent or its data to the fragment's method
            myFragement.resolveIntent(intent);
        }
    }
    private void updateTapButtons() {
        if (status == 0)
        {
            btn_nfc_tap_frame.setTypeface(null, Typeface.BOLD);
            btn_bar_code_frame.setTypeface(null, Typeface.NORMAL);
            btn_text_input_frame.setTypeface(null, Typeface.NORMAL);
            btn_nfc_tap_frame.setBackgroundResource(R.drawable.tap_btn_back);
            btn_bar_code_frame.setBackgroundColor(Color.BLACK);
            btn_text_input_frame.setBackgroundColor(Color.BLACK);
            fragment = new NFCReaderFragment();
            replaceFragment(fragment);
        }
        else if (status == 1)
        {
            btn_nfc_tap_frame.setTypeface(null, Typeface.NORMAL);
            btn_bar_code_frame.setTypeface(null, Typeface.BOLD);
            btn_text_input_frame.setTypeface(null, Typeface.NORMAL);
            btn_nfc_tap_frame.setBackgroundColor(Color.BLACK);
            btn_bar_code_frame.setBackgroundResource(R.drawable.tap_btn_back);
            btn_text_input_frame.setBackgroundColor(Color.BLACK);
            fragment = new BarCodeReaderFragment();
            replaceFragment(fragment);
        }
        else if (status == 2)
        {
            btn_nfc_tap_frame.setTypeface(null, Typeface.NORMAL);
            btn_bar_code_frame.setTypeface(null, Typeface.NORMAL);
            btn_text_input_frame.setTypeface(null, Typeface.BOLD);
            btn_nfc_tap_frame.setBackgroundColor(Color.BLACK);
            btn_bar_code_frame.setBackgroundColor(Color.BLACK);
            btn_text_input_frame.setBackgroundResource(R.drawable.tap_btn_back);
            fragment = new TextInputFragment();
            replaceFragment(fragment);
        }
    }
    private void replaceFragment (Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.fragment_serial_number, fragment);
        mFragmentTransaction.commit();
    }
    public void getFrameWithSerialNumber(String serialNumber) {
        RequestParams params = new RequestParams();
        UserObject user = UserUtils.getSession(this);
        String token = user.getToken();

        AsyncHttpClient client = new AsyncHttpClient();
        String authorization = "base " + token;
        client.addHeader("Authorization", authorization);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.get(Utils.BASE_URL + "Frames/" + serialNumber, new LookCaresResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers,	Throwable throwable, JSONObject errorResponse) {
                if (errorResponse == null) {
                    Toast.makeText(FrameSelectionActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String message = errorResponse.getString("message");
                        Toast.makeText(FrameSelectionActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(FrameSelectionActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
                    }
                }
                if (status == 1) {
                    updateTapButtons();
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        selectedFrame = response;
                        String strFrame = selectedFrame.getString("frame");
                        JSONObject frame = new JSONObject(strFrame);
                        String strFabrics = selectedFrame.getString("fabrics");

                        UserUtils.storeSelectedFrame(FrameSelectionActivity.this, strFrame);
                        UserUtils.storeInstalledFabrics(FrameSelectionActivity.this, strFabrics);

                        String vcInstalled = frame.getString("vcInstalled");
                        if (vcInstalled.equals("Uninstalled"))
                            installed = false;
                        else
                            installed = true;
                        Intent intent;
                        if (installed) {
                            String vcExtrusion = frame.getString("vcExtrusion");
                            if (vcExtrusion.equals("120mm") || (vcExtrusion.equals("36mm")) || (vcExtrusion.equals("50mm")))
                            {
                                intent = new Intent(FrameSelectionActivity.this, RemoveFabricActivity.class);
                                startActivity(intent);
                            }
                            else {
                                intent = new Intent(FrameSelectionActivity.this, RemoveFabricSingleActivity.class);
                                startActivity(intent);
                            }

                        } else {

                            String vcExtrusion = frame.getString("vcExtrusion");
                            if (vcExtrusion.equals("120mm") || (vcExtrusion.equals("36mm")) || (vcExtrusion.equals("50mm")))
                            {
                                intent = new Intent(FrameSelectionActivity.this, InStoreLocationSelection.class);
                                intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
                                intent.putExtra("FRAME_SIZE", 2);
                                startActivity(intent);
                            }
                            else
                            {
                                intent = new Intent(FrameSelectionActivity.this, InStoreLocationSelection.class);
                                intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
                                intent.putExtra("FRAME_SIZE", 1);
                                startActivity(intent);
                            }
                        }
                    } catch (JSONException e) {
                    }
                }
            }
        });
    }
    public void getFabricWithSerialNumber(final String serialNumber) {
        RequestParams params = new RequestParams();
        UserObject user = UserUtils.getSession(this);
        String token = user.getToken();

        AsyncHttpClient client = new AsyncHttpClient();
        String authorization = "base " + token;
        client.addHeader("Authorization", authorization);
        client.addHeader("Content-Type", "application/json");
        client.addHeader("Accept", "application/json");
        client.get(Utils.BASE_URL + "Frames/Fabric/" + serialNumber, new LookCaresResponseHandler(this) {
            @Override
            public void onFailure(int statusCode, Header[] headers,	Throwable throwable, JSONObject errorResponse) {
                if (errorResponse == null) {
                    Toast.makeText(FrameSelectionActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
                } else {
                    try {
                        String message = errorResponse.getString("message");
                        Toast.makeText(FrameSelectionActivity.this, message, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        Toast.makeText(FrameSelectionActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
                    }
                }
                if (status == 1) {
                    updateTapButtons();
                }
            }
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);

                if (response != null) {
                    try {
                        selectedFabric = response;
                        UserUtils.storeSelectedFabric(FrameSelectionActivity.this, selectedFabric.toString());

                        String storedFrame = UserUtils.getSelectedFrame(FrameSelectionActivity.this);
                        selectedFrame = new JSONObject(storedFrame);
                        String storedLocation = UserUtils.getSelectedLocation(FrameSelectionActivity.this);
                        JSONObject objLocation = new JSONObject(storedLocation);
                        String storedInStoreLocation = UserUtils.getSelectedStoreLocation(FrameSelectionActivity.this);
                        JSONObject objInStoreLocation = new JSONObject(storedInStoreLocation);

                        RequestParams params = new RequestParams();
//                        params.put("ClientLocationKey", objLocation.getString("kLookClientCustomer"));
//                        params.put("FrameKey", selectedFrame.getString("kFrame"));
//                        params.put("InStoreLocation", objInStoreLocation.getString("vcInStoreLocation"));
//                        params.put("SerialNumber", serialNumber);

                        String ClientLocationKey = objLocation.getString("kLookClientCustomer");
                        String FrameKey = selectedFrame.getString("kFrame");
                        String InStoreLocation = objInStoreLocation.getString("vcInStoreLocation");

                        params.put("ClientLocationKey", ClientLocationKey);
                        params.put("FrameKey", FrameKey);
                        params.put("InStoreLocation", InStoreLocation);
                        params.put("SerialNumber", serialNumber);

                        UserObject user = UserUtils.getSession(FrameSelectionActivity.this);
                        String token = user.getToken();

                        AsyncHttpClient client = new AsyncHttpClient();
                        String authorization = "base " + token;
                        client.addHeader("Authorization", authorization);
                        client.post(Utils.BASE_URL + "Frames/Fabric", params, new LookCaresResponseHandler(FrameSelectionActivity.this) {
                            @Override
                            public void onFailure(int statusCode, Header[] headers,	Throwable throwable, JSONObject errorResponse) {
                                super.onFailure(statusCode, headers, throwable,	errorResponse);
                                if (errorResponse == null) {
                                    Toast.makeText(FrameSelectionActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
                                } else {
                                    try {
                                        if (!errorResponse.isNull("error")) {
                                            String message = errorResponse.getString("message");
                                            Utils.showAlertWithTitleNoCancel(FrameSelectionActivity.this, "Warning", message);
                                        }
                                    } catch (JSONException e) {
                                        Toast.makeText(FrameSelectionActivity.this, "Please check your network status", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            @Override
                            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                                super.onSuccess(statusCode, headers, response);

                                if (response != null) {
                                    Toast.makeText(FrameSelectionActivity.this, "Fabric is added successfully!", Toast.LENGTH_LONG).show();
//                                    Bundle extras = getIntent().getExtras();
//                                    if (extras != null) {
//                                        frame_size = extras.getInt("FRAME_SIZE");
//                                        //The key argument here must match that used in the other activity
//                                    }
                                    Intent intent;
                                    if (frame_size == 2) {
                                        intent = new Intent(FrameSelectionActivity.this, FrameSelectionActivity.class);
                                        intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FABRIC");
                                        intent.putExtra("FRAME_SIZE", 1);
                                    } else { // == 1
                                        intent = new Intent(FrameSelectionActivity.this, TakePictureActivity.class);
                                    }
                                    startActivity(intent);
                                }
                            }
                        });


                    } catch (JSONException e) {
                    }
                }
            }
        });
    }
}
