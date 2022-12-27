package com.example.Signupsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class RegisterDB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "testDB.db";
    public static final String TABLE_NAME = "RegisterData";
    public static final String FilePath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/SignUpDatabase/"+DATABASE_NAME;
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FIRSTNAME";
    public static final String COL_3 = "FATHERNAME";
    public static final String COL_4 = "PHONE";
    public static final String COL_5 = "EMAIL";
    public static final String COL_6 = "PASS";


    public RegisterDB(Context context){

        super(context, FilePath , null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" Create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, FATHERNAME TEXT,PHONE TEXT, EMAIL TEXT, PASS TEXT)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEmpData(String FIRSTNAME, String FATHERNAME, String PHONE , String EMAIL , String PASS){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues co= new ContentValues();

        co.put(COL_2,FIRSTNAME);
        co.put(COL_3,FATHERNAME);
        co.put(COL_4,PHONE);
        co.put(COL_5,EMAIL);
        co.put(COL_6,PASS);
        db.insert(TABLE_NAME,null,co);
        db.close();
    }

    public Cursor readData(String phone){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME,null,"PHONE = ? ",new String[]{phone},null,null,null);
    }


}
