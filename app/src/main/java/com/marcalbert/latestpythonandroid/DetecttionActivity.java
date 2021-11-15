package com.marcalbert.latestpythonandroid;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class DetecttionActivity extends AppCompatActivity {
    private Button select_image;
    private ImageView image_v;
    //private age_gender_recognition age_gender_recognition;
    int SELECT_PICTURE = 200;
    private String returned_age = null;
    private String returned_gender = null;

    private TextView ageView;
    private TextView genderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detecttion);
        select_image = findViewById(R.id.selectImage);
        image_v = findViewById(R.id.imageV);

        ageView = findViewById(R.id.ageView);
        genderView = findViewById(R.id.genderView);

        select_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                imageChooser();
            }
        });
    }

    private void imageChooser() {
        Intent i = new Intent();

        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();

                if (selectedImageUri != null) {
                    Log.d("StorageActivity", "Output Uri: " + selectedImageUri);

                    image_v.setImageURI(selectedImageUri);

                    Bitmap bitmaptwo = null;

                    try {
                        bitmaptwo = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String h = String.valueOf(bitmaptwo);

                    BitmapDrawable drawable = (BitmapDrawable) image_v.getDrawable();
                    Bitmap bitmap = drawable.getBitmap();
                    //String imageString = String.valueOf(bitmap);

                    //image_v.setImageBitmap(bitmap1);


                    //Bitmap finalBitmap = bitmap;
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent i = new Intent(DetecttionActivity.this, PhotoActivity.class);
                            Bitmap b; // your bitmap
                            b = bitmap;
                            ByteArrayOutputStream bs = new ByteArrayOutputStream();
                            b.compress(Bitmap.CompressFormat.PNG, 50, bs);
                            i.putExtra("byteArray", bs.toByteArray());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);


//                            Intent intent = new Intent(PhotoActivity.this, SeventhActivity.class);
//                            intent.putExtra("bitmap", imageString);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(intent);
                        }
                    }, 2000);
//                    startActivity(new Intent(StoragePredictionActivity.this,GetCategory.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
                }
            }
        }
    }
}