package com.example.loginsqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class RegisterDB extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "RegisterData.db";
    public static final String TABLE_NAME = "RegisterData";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "FIRSTNAME";
    public static final String COL_3 = "LASTNAME";
    public static final String COL_4 = "FATHERNAME";
    public static final String COL_5 = "PHONE";
    public static final String COL_6 = "EMAIL";
    public static final String COL_7 = "PASS";
    public static final String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/LoginDatabase/" + DATABASE_NAME;
    public static final String DB_GETPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/NewDatabase/" + DATABASE_NAME;
    Context context;

    public RegisterDB(Context context) {
        super(context, DB_PATH, null, 2);
        this.context = context;
    }
    public void createDB(SQLiteDatabase sqliteDB){

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(" Create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, LASTNAME TEXT, FATHERNAME TEXT,PHONE TEXT, EMAIL TEXT, PASS TEXT)" );

//        boolean dbExist = checkDataBase();
//        SQLiteDatabase db_Read = null;
//        if (!dbExist) {
//            db.execSQL(" Create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, LASTNAME TEXT, FATHERNAME TEXT,PHONE TEXT, EMAIL TEXT, PASS TEXT)" );
//
//        } else {
////            db_Read = this.getReadableDatabase();
////            db_Read.close();
//
//            copyDataBase();
//            db.execSQL(" Create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, LASTNAME TEXT, FATHERNAME TEXT,PHONE TEXT, EMAIL TEXT, PASS TEXT)" );
//
//        }
    }

    private void copyDataBase() {
        FileChannel source=null;
        FileChannel destination=null;

        File currentDB = new File(DB_GETPATH);
        File backupDB = new File(DB_PATH);
        Log.v("Before import","Before import");
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Log.v("After import","After import");
            Toast.makeText(context, "DB Imported to Original", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try{
            String myPath = DB_PATH;
            File file = new File(myPath);
            checkDB = SQLiteDatabase.openDatabase(file.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        }catch(SQLiteException e){
            //database does't exist yet.
        }
        if(checkDB != null){
            checkDB.close();
        }


        return checkDB != null ? true : false;
    }

    private void createDataBase() {

    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void addEmpData(String FIRSTNAME, String LASTNAME , String FATHERNAME, String PHONE , String EMAIL , String PASS){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues co= new ContentValues();

        co.put(COL_2,FIRSTNAME);
        co.put(COL_3,LASTNAME);
        co.put(COL_4,FATHERNAME);
        co.put(COL_5,PHONE);
        co.put(COL_6,EMAIL);
        co.put(COL_7,PASS);
        db.insert(TABLE_NAME,null,co);
        db.close();
    }

    public Cursor readData(String phone){
        SQLiteDatabase db = this.getReadableDatabase();
       return db.query(TABLE_NAME,null,"PHONE = ? ",new String[]{phone},null,null,null);
    }


}
