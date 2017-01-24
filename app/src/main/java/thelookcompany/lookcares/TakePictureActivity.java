package thelookcompany.lookcares;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.Date;

import thelookcompany.lookcares.datamodel.UserObject;
import thelookcompany.lookcares.network.LookCaresResponseHandler;
import thelookcompany.lookcares.utils.UserUtils;
import thelookcompany.lookcares.utils.Utils;

public class TakePictureActivity extends AppCompatActivity {

    private ImageView img_photo;
    private Bitmap photoBMP, mBitmap;
    private Uri mPicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_picture);
        ImageButton btn_take_photo = (ImageButton) findViewById(R.id.btn_take_photo);
        btn_take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        img_photo = (ImageView) findViewById(R.id.img_photo);

        Button btn_done_photo = (Button) findViewById(R.id.btn_done_photo);
        btn_done_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photoBMP != null) {
                    postImageToServer();
                }
                showAlertForMakeMorechanges();
            }
        });
    }
    private void postImageToServer(){
        RequestParams params = new RequestParams();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        photoBMP.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        params.put("uploadedfile", new ByteArrayInputStream(stream.toByteArray()), "img_"+timeStamp+".jpg", "image/jpeg");

        String storedFrame = UserUtils.getSelectedFrame(TakePictureActivity.this);
        try {
            JSONObject selectedFrame = new JSONObject(storedFrame);
            String FrameKey = selectedFrame.getString("kFrame");
            params.put("kFrame", FrameKey);
            UserObject user = UserUtils.getSession(TakePictureActivity.this);
            String token = user.getToken();

            AsyncHttpClient client = new AsyncHttpClient();
            String authorization = "base " + token;
            client.addHeader("Authorization", authorization);
            client.post(Utils.BASE_URL + "Frames/Upload", params, new LookCaresResponseHandler(TakePictureActivity.this) {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    super.onSuccess(statusCode, headers, response);

                    if (response != null) {
                        Toast.makeText(TakePictureActivity.this, "Upload successfully!", Toast.LENGTH_LONG);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void showAlertForMakeMorechanges() {
        AlertDialog.Builder builder = new AlertDialog.Builder(TakePictureActivity.this);
        builder.setTitle("Make more changes?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
                Intent intent = new Intent(TakePictureActivity.this, FrameSelectionActivity.class);
                intent.putExtra("SERIAL_NUMBER_SELECTION_TYPE", "FRAME");
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //TODO
                dialog.dismiss();
                UserUtils.storeRememberMe(TakePictureActivity.this, "no");
                Intent intent = new Intent(TakePictureActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(TakePictureActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, 0);//zero can be replaced with any action code
                } else if (items[item].equals("Choose from Library")) {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch (requestCode) {
            case 0:
                if (resultCode == RESULT_OK) {
                    try {
                        photoBMP = (Bitmap) imageReturnedIntent.getExtras().get("data");
                        //captured image set in imageview
                        img_photo.setImageBitmap(photoBMP);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                break;
            case 1:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = imageReturnedIntent.getData();
                    mPicUri = selectedImage;
                    img_photo.setImageURI(selectedImage);
                    try {
                        photoBMP = BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
