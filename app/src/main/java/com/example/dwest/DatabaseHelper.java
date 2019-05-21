package com.example.dwest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "savenumber.db";
    private static final String TABLE_NAME = "calls";
    private static final String COLUMN_NUM = "num";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_LAT = "latitude";
    private static final String COLUMN_LONG = "longitude";
    private SQLiteDatabase db;
    private static final String TABLE_CREATE = "CREATE TABLE calls (num integer not null, date text not null, latitude text not null, longitude text not null);";

    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
        this.db = db;
    }

    public void insertCall(CallData cd){
        db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(COLUMN_NUM, cd.getNumber());
        values.put(COLUMN_DATE, cd.getDate());
        values.put(COLUMN_LAT, cd.getLatitude());
        values.put(COLUMN_LONG, cd.getLongitude());

        db.insert(TABLE_NAME, null, values);
        db.close();
    }


    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " ORDER BY date DESC";
        Cursor data = db.rawQuery(query, null);
        return data;
    }




    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE IF EXISTS "+TABLE_NAME;
        db.execSQL(query);
        db.close();
        this.onCreate(db);
    }
}
