package com.example.jwells;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Set your XML layout here

        // Floating Action Button to navigate to Input_activity
        FloatingActionButton bt = findViewById(R.id.fab);
        bt.setOnClickListener(v -> {
            Intent nt = new Intent(MainActivity.this, Input_activity.class);
            startActivity(nt);
        });

        // Initial UI setup
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Refresh UI when returning to this activity
        updateUI();
    }

    private void updateUI() {
        LinearLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.removeAllViews();

        MyDBHelper dbHelper = new MyDBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                MyDBHelper.TABLE_NAME,
                new String[]{MyDBHelper.DATA_ID, MyDBHelper.DATA_NAME, MyDBHelper.DATA_SIZE, MyDBHelper.DATA_WEIGHT, MyDBHelper.DATA_PRICE, MyDBHelper.DATA_IMG_PATH}, // Include image path
                null,
                null,
                null,
                null,
                null
        );

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(MyDBHelper.DATA_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.DATA_NAME));
                String size = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.DATA_SIZE));
                double weight = cursor.getDouble(cursor.getColumnIndexOrThrow(MyDBHelper.DATA_WEIGHT));
                double price = cursor.getDouble(cursor.getColumnIndexOrThrow(MyDBHelper.DATA_PRICE));
                String imgPath = cursor.getString(cursor.getColumnIndexOrThrow(MyDBHelper.DATA_IMG_PATH));

                LinearLayout itemLayout = new LinearLayout(this);
                itemLayout.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));
                itemLayout.setOrientation(LinearLayout.VERTICAL);
                itemLayout.setPadding(10, 10, 10, 10);

                TextView idTextView = new TextView(this);
                idTextView.setText("ID: " + id);
                idTextView.setTextSize(16);

                TextView nameTextView = new TextView(this);
                nameTextView.setText("Name: " + name);
                nameTextView.setTextSize(16);

                TextView sizeTextView = new TextView(this);
                sizeTextView.setText("Size: " + size);
                sizeTextView.setTextSize(16);

                TextView weightTextView = new TextView(this);
                weightTextView.setText("Weight: " + weight);
                weightTextView.setTextSize(16);

                TextView priceTextView = new TextView(this);
                priceTextView.setText("Price: " + price);
                priceTextView.setTextSize(16);

                // Add ImageView for the image
                ImageView imageView = new ImageView(this);
                imageView.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        300 // Set height in pixels
                ));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);

                // Load the image from the file path
                if (imgPath != null && !imgPath.isEmpty()) {
                    imageView.setImageURI(Uri.parse(imgPath));
                } else {
                    imageView.setImageResource(android.R.drawable.ic_menu_report_image); // Placeholder
                }

                itemLayout.addView(idTextView);
                itemLayout.addView(nameTextView);
                itemLayout.addView(sizeTextView);
                itemLayout.addView(weightTextView);
                itemLayout.addView(priceTextView);
                itemLayout.addView(imageView); // Add the image view

                linearLayout.addView(itemLayout);

                View divider = new View(this);
                divider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        2
                ));
                divider.setBackgroundColor(ContextCompat.getColor(this, android.R.color.darker_gray));
                linearLayout.addView(divider);

            } while (cursor.moveToNext());

        } else {
            Log.d("DisplayActivity", "No data found in the database.");
        }

        cursor.close();
        db.close();
    }
}
