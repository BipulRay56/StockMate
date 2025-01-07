package com.example.jwells;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DatabaseUtils {
    public static void m1(Context context) {
        MyDBHelper dbHelper = new MyDBHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase(); // Open database in writable mode

        // Log the database name
        Log.d("DatabaseUtils", "Database Name: " + db.getPath());

        // Check if the table exists
        Cursor cursor = db.rawQuery(
                "SELECT name FROM sqlite_master WHERE type='table' AND name='" + MyDBHelper.TABLE_NAME + "'",
                null
        );

        if (cursor.moveToFirst()) {
            // Log the table name
            Log.d("DatabaseUtils", "Table exists: " + cursor.getString(0));
        } else {
            Log.d("DatabaseUtils", "Table does not exist.");
            cursor.close();
            db.close();
            return;
        }
        cursor.close();

        // Insert two entries into the table
        ContentValues values1 = new ContentValues();
        values1.put(MyDBHelper.DATA_NAME, Input_activity.name);
        values1.put(MyDBHelper.DATA_SIZE, Input_activity.size);
        values1.put(MyDBHelper.DATA_WEIGHT, Input_activity.weight);
        values1.put(MyDBHelper.DATA_PRICE, Input_activity.price);
        values1.put(MyDBHelper.DATA_IMG_PATH, Input_activity.imgPath); // Insert image path
        // Inserting data into the database
        db.insert(MyDBHelper.TABLE_NAME, null, values1);

        // Query and log all entries
        Cursor dataCursor = db.rawQuery("SELECT * FROM " + MyDBHelper.TABLE_NAME, null);

        if (dataCursor.moveToFirst()) {
            do {
                int id = dataCursor.getInt(dataCursor.getColumnIndexOrThrow(MyDBHelper.DATA_ID));
                String name = dataCursor.getString(dataCursor.getColumnIndexOrThrow(MyDBHelper.DATA_NAME));
                String size = dataCursor.getString(dataCursor.getColumnIndexOrThrow(MyDBHelper.DATA_SIZE));
                String weight = dataCursor.getString(dataCursor.getColumnIndexOrThrow(MyDBHelper.DATA_WEIGHT));
                String price = dataCursor.getString(dataCursor.getColumnIndexOrThrow(MyDBHelper.DATA_PRICE));
                String imgPath = dataCursor.getString(dataCursor.getColumnIndexOrThrow(MyDBHelper.DATA_IMG_PATH)); // Retrieve image path

                Log.d("DatabaseUtils", "Row: ID=" + id + ", Name=" + name + ", Size=" + size +
                        ", Weight=" + weight + ", Price=" + price + ", Image Path=" + imgPath);
            } while (dataCursor.moveToNext());
        } else {
            Log.d("DatabaseUtils", "No data found in the table.");
        }

        dataCursor.close();
        db.close();
    }
}
