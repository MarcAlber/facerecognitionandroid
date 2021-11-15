package com.marcalbert.latestpythonandroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class PhotoActivity extends AppCompatActivity {
    ArrayList<String> imagelist, downloadedImages;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    ImageAdapter adapter;
    StorageReference listRef;

    Python py;

    ImageView originalImage, inviImage;
    BitmapDrawable drawable, drawabletwo;
    Bitmap bitmap, bitmaptwo;
    String imageString = "";

    String hello;
    String original;
    String bitmapUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        imagelist = new ArrayList<>();
        downloadedImages = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new ImageAdapter(imagelist, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(null));
        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);

        originalImage = findViewById(R.id.theImage);
        inviImage = findViewById(R.id.invisibleImage);

        if(getIntent().hasExtra("byteArray")) {
            //ImageView previewThumbnail = new ImageView(this);
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent().getByteArrayExtra("byteArray").length);
            originalImage.setImageBitmap(b);
        }

//        try {
//            drawable = (BitmapDrawable) originalImage.getDrawable();
//            bitmap = drawable.getBitmap();
//            imageString = getStringImage(bitmap);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        drawable = (BitmapDrawable) originalImage.getDrawable();
        bitmap = drawable.getBitmap();
        imageString = getStringImage(bitmap);


        //imageUri = intent.getStringExtra("url"); //float

        // Log.d("URL", "URL IS: " + imageUri);

        //Bitmap bitmap = (Bitmap) intent.getParcelableExtra("bitmap");



        if (!Python.isStarted())
            Python.start(new AndroidPlatform(this));

        py = Python.getInstance();

        listRef = FirebaseStorage.getInstance().getReference().child("/");

        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            int i = 0;
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference file : listResult.getItems()) {
                    file.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            downloadedImages.add(uri.toString()); // <- all the images
                            getImagesFromArray(i);
                            i++;
                            //downloadedImages.
                            Log.d("ItemValue", uri.toString());
                        }
                    }).addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                        }
                    });
                }
            }
        });

        // Log.d("ARRAYSIZE","INDEX: " + downloadedImages.size());

    }

    public void getImagesFromArray(int i) {
        Glide.with(this)
                .asBitmap()
                .load(downloadedImages.get(i))
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        inviImage.setImageBitmap(resource);
                        drawabletwo = (BitmapDrawable) inviImage.getDrawable();
                        bitmaptwo = drawabletwo.getBitmap();

                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmaptwo.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] imageBytes = baos.toByteArray();
                        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);

                        hello = encodedImage;
                        original = downloadedImages.get(i);

                        PyObject pyo = py.getModule("returnfaces");
                        PyObject obj = pyo.callAttr("main", imageString, hello, original);

                        String str = obj.toString();

                        Log.d("IMAGES", "DIRECT FROM PYTHON: " + str);

                        if(str.equals("---")) {
                            Log.d("IMAGES", "VALUE WAS ---");
                        } else {
                            imagelist.add(str);
                        }

                        inviImage.setVisibility(View.GONE);
                        originalImage.setVisibility(View.GONE);
                        recyclerView.setAdapter(adapter); // <- set the recycler view adapter
                        progressBar.setVisibility(View.GONE); // <- makes the progress bar go away
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = android.util.Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }
}