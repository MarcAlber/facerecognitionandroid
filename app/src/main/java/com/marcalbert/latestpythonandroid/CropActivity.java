package com.marcalbert.latestpythonandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;

public class CropActivity extends AppCompatActivity {
    ImageView resultImg;
    ImageView img;
    EditText X, Y;
    CropImageView cropImageView;
    Button cropButton, uploadButton;
    String imageMetadata;
    Bitmap cropped;
    float x = 0f;
    float y = 0f;

    byte[] byteArray;
    Bitmap b, c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);
        cropImageView = findViewById(R.id.cropImageView);
        cropButton = findViewById(R.id.cropImage);
        uploadButton = findViewById(R.id.uploadImage);
        resultImg = findViewById(R.id.resultView);
        cropButton.setVisibility(View.GONE);
        uploadButton.setVisibility(View.GONE);

        Intent intent = getIntent();
        imageMetadata = intent.getStringExtra("title"); //float

        img = findViewById(R.id.imageView);
//        Glide.with(this).load(imageMetadata).into(img);

//        Glide.with(this)
//                .asBitmap()
//                .load(imageMetadata)
//                .into(new CustomTarget<Bitmap>(400,300) {
//                    @Override
//                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
//                        img.setImageBitmap(resource);
//                    }
//
//                    @Override
//                    public void onLoadCleared(@Nullable Drawable placeholder) {
//                    }
//                });

        Glide.with(this)
                .asBitmap()
                .load(imageMetadata)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        img.setImageBitmap(resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                    }
                });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImage();
            }
        });

        cropButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.getCroppedImageAsync();
                cropButton.setVisibility(View.GONE);
                uploadButton.setVisibility(View.VISIBLE);
            }
        });

        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("CROPACTIVITY","Going to Future Detection!");

                Intent i = new Intent(CropActivity.this, PhotoActivity.class);
                Bitmap b; // your bitmap
                b = c;
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.PNG, 50, bs);
                i.putExtra("byteArray", bs.toByteArray());
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);


//                Intent intent = new Intent(CropActivity.this, SeventhActivity.class);
//                //intent.putExtra("url", imageMetadata);
//                intent.putExtra("url", c);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });

        cropImageView.setOnCropImageCompleteListener(new CropImageView.OnCropImageCompleteListener() {
            @Override
            public void onCropImageComplete(CropImageView view, CropImageView.CropResult result) {
                cropped = result.getBitmap();
                resultImg.setImageBitmap(cropped);

                resultImg.setDrawingCacheEnabled(true);
                b = resultImg.getDrawingCache();

                BitmapDrawable drawable = (BitmapDrawable) resultImg.getDrawable();
                c = drawable.getBitmap();

//                Bitmap bmp = BitmapFactory.decodeFile(String.valueOf(resultImg));
//                ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//                byteArray = stream.toByteArray();

                //cropImageView.getCroppedImageAsync();
                cropImageView.setVisibility(View.INVISIBLE);


            }
        });
    }

    private void cropImage() {
//        ImageView img = findViewById(R.id.imageView);
//        Glide.with(this).load(data).into(img);
//        img.setImageDrawable(Drawable.createFromPath(data));
        img.getDrawable();
        img.setDrawingCacheEnabled(true);
        Bitmap bitmap = img.getDrawingCache();
        cropImageView.setImageBitmap(bitmap);
        img.setVisibility(View.INVISIBLE);
        cropButton.setVisibility(View.VISIBLE);


    }
}