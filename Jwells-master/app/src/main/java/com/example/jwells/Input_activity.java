package com.example.jwells;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;

public class Input_activity extends AppCompatActivity {

    static String name;
    static String size;
    static String weight;
    static String price;
    static String imgPath;
    ImageView imageView;
    Bitmap capturedPhoto;

    private static final String TAG = "InputActivity";

    // Camera result launcher
    private final ActivityResultLauncher<Intent> cameraLauncher =
            registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    capturedPhoto = (Bitmap) result.getData().getExtras().get("data");
                    imageView.setImageBitmap(capturedPhoto);
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input);

        // Initialize views
        imageView = findViewById(R.id.imagePreview);
        Button bt_Click = findViewById(R.id.buttonCamera);
        Button btSave = findViewById(R.id.buttonSave);

        // Camera button click listener
        bt_Click.setOnClickListener(v -> {
            Intent icamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(icamera);
        });



        // Save button click listener
        btSave.setOnClickListener(v -> {
            getDataFromFields();
            if (validateFields()) {
                savePhotoToPublicGallery();
                DatabaseUtils.m1(Input_activity.this);
                Toast.makeText(this, "Data saved successfully!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getDataFromFields() {
        // Initialize EditText fields
        EditText editTextName = findViewById(R.id.editTextName);
        EditText editTextSize = findViewById(R.id.editTextSize);
        EditText editTextWeight = findViewById(R.id.editTextWeight);
        EditText editTextPrice = findViewById(R.id.editTextPrice);

        // Retrieve data from EditText fields
        name = editTextName.getText().toString().trim();
        size = editTextSize.getText().toString().trim();
        weight = editTextWeight.getText().toString().trim();
        price = editTextPrice.getText().toString().trim();

        // Log the values for debugging
        Log.d(TAG, "Name: " + name);
        Log.d(TAG, "Size: " + size);
        Log.d(TAG, "Weight: " + weight);
        Log.d(TAG, "Price: " + price);
    }

    private boolean validateFields() {
        if (name.isEmpty() || size.isEmpty() || weight.isEmpty() || price.isEmpty()) {
            Toast.makeText(this, "All fields are required!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void savePhotoToPublicGallery() {
        if (capturedPhoto == null) {
            Toast.makeText(this, "No photo to save!", Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, name + "_" + System.currentTimeMillis() + ".jpg");
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/jewels");

        try {
            Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null) {
                try (OutputStream out = getContentResolver().openOutputStream(uri)) {
                    capturedPhoto.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Toast.makeText(this, "Photo saved to gallery!", Toast.LENGTH_SHORT).show();

                    // Save the URI path to imgPath
                    imgPath = uri.toString();
                    Log.d(TAG, "Image Path: " + imgPath);

                    // Log database columns and their values
                    for (String key : values.keySet()) {
                        Log.d(TAG, "Database Column: " + key + ", Value: " + values.getAsString(key));
                    }
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Failed to save photo", e);
            Toast.makeText(this, "Failed to save photo!", Toast.LENGTH_SHORT).show();
        }
    }

}
