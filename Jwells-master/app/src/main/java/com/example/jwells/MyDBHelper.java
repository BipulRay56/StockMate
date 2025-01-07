package com.example.jwells;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class MyDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NeroDB";
    public static final int DATABASE_VERSION = 3; // Incremented version number
    public static final String TABLE_NAME = "My_TABLE";
    public static final String DATA_ID = "ID";
    public static final String DATA_NAME = "NAME";
    public static final String DATA_SIZE = "SIZE";
    public static final String DATA_WEIGHT = "WEIGHT";
    public static final String DATA_PRICE = "PRICE";
    public static final String DATA_IMG_PATH = "IMG_PATH"; // New column

    public MyDBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                DATA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                DATA_NAME + " TEXT, " +
                DATA_SIZE + " TEXT, " +
                DATA_WEIGHT + " TEXT, " +
                DATA_PRICE + " TEXT, " +
                DATA_IMG_PATH + " TEXT)"); // Include the new column here
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) { // Check version to add new column for existing users
            db.execSQL("ALTER TABLE " + TABLE_NAME + " ADD COLUMN " + DATA_IMG_PATH + " TEXT");
        }
    }
}
