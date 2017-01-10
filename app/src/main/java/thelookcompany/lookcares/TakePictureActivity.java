package thelookcompany.lookcares;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import thelookcompany.lookcares.utils.UserUtils;

public class TakePictureActivity extends AppCompatActivity {

    private ImageView img_photo;
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
                if (img_photo != null) {
                    postImageToServer();
                }
                showAlertForMakeMorechanges();
            }
        });
    }
    private void postImageToServer(){

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
        switch(requestCode) {
            case 0:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    img_photo.setImageURI(selectedImage);
                }

                break;
            case 1:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    img_photo.setImageURI(selectedImage);
                }
                break;
        }
    }
}
