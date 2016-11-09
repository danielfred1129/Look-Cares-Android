package thelookcompany.lookcares.network;

import android.app.Activity;
import android.app.ProgressDialog;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by buddy on 11/8/2016.
 */


public class LookCaresResponseHandler extends JsonHttpResponseHandler {

    private Activity context;
    private ProgressDialog progressDlg;

    public LookCaresResponseHandler(Activity context) {
        this.context = context;

        progressDlg = new ProgressDialog(context);
        progressDlg.setCancelable(false);
        progressDlg.setCanceledOnTouchOutside(false);
        progressDlg.setMessage("Loading...");
    }

    @Override
    public void onStart() {
        super.onStart();

        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDlg != null)
                    progressDlg.show();
            }
        });
    }

    @Override
    public void onFinish() {
        super.onFinish();
        context.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (progressDlg != null &&progressDlg.isShowing())
                    progressDlg.dismiss();
            }
        });
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,	String responseString, Throwable throwable) {
        super.onFailure(statusCode, headers, responseString, throwable);
        Toast.makeText(context, "Please check your network status", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,	Throwable throwable, JSONArray errorResponse) {
        super.onFailure(statusCode, headers, throwable,	errorResponse);
        Toast.makeText(context, "Please check your network status", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onFailure(int statusCode, Header[] headers,	Throwable throwable, JSONObject errorResponse) {
        super.onFailure(statusCode, headers, throwable,	errorResponse);
        if (errorResponse == null) {
            Toast.makeText(context, "Please check your network status", Toast.LENGTH_LONG).show();
        } else {
            try {
                if (!errorResponse.isNull("error")) {
                    JSONObject object = errorResponse.getJSONObject("error");
                    if (object.getString("message").indexOf("uploaded file exceeds") != -1) {
                        Toast.makeText(context, "File is too large, please trim the video down and try again", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(context, object.getString("message"), Toast.LENGTH_LONG).show();
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(context, "Please check your network status", Toast.LENGTH_LONG).show();
            }
        }
    }

}