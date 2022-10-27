package com.example.loginsqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class DatabaseOpenHelper extends SQLiteOpenHelper {
        public static final String DB_NAME = "testDB.db";
        public static final String TABLE_NAME = "RegisterData";
        public static final String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/LoginDatabase" + DB_NAME;
        public static final String DB_GETPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/NewDatabase/" + DB_NAME;
        public static final String COL_1 = "ID";
        public static final String COL_2 = "FIRSTNAME";
        public static final String COL_3 = "LASTNAME";
        public static final String COL_4 = "FATHERNAME";
        public static final String COL_5 = "PHONE";
        public static final String COL_6 = "EMAIL";
        public static final String COL_7 = "PASS";
      public static String DB_PATHNAME = null;
        public static  SQLiteDatabase db;
        private final Context context;

        public DatabaseOpenHelper(Context context){
            super(context, DB_NAME, null, 1);
            this.context = context;
            DB_PATHNAME = context.getExternalFilesDir("Database"+DB_NAME).getPath();
        }


    public void createDataBase() throws IOException {

        boolean dbExist = checkDataBase();
        SQLiteDatabase db_Read = null;


        if (dbExist) {
            //do nothing - database already exist

            try {
                db = context.openOrCreateDatabase(DatabaseOpenHelper.DB_NAME,
                        Context.MODE_PRIVATE, null);
                db.execSQL(" Create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, LASTNAME TEXT, FATHERNAME TEXT,PHONE TEXT, EMAIL TEXT, PASS TEXT)");
            }catch (Exception e){
                Log.e(context.toString(), e.getMessage());
            }
//            try {
//                db_Read.execSQL(" Create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, LASTNAME TEXT, FATHERNAME TEXT,PHONE TEXT, EMAIL TEXT, PASS TEXT)");
//                Log.v("krishna", "Krishna Else part");
//            }catch (SQLiteException e){
//                e.printStackTrace();
//
//            }

        } else {

            db_Read = this.getReadableDatabase();
            db_Read.close();

            try {
                copyDataBase();
            } catch (IOException e) {
//                throw new Error("Error copying database");
                e.toString();
            }
//            String Createtable = " Create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, LASTNAME TEXT, FATHERNAME TEXT,PHONE TEXT, EMAIL TEXT, PASS TEXT)";
//            db.execSQL(Createtable);
//
            Log.v("krishna", "Krishna Else Exception");
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


    private void copyDataBase() throws IOException {

        FileChannel source = null;
        FileChannel destination = null;
        File currentDB = new File(DB_GETPATH);
        File backupDB = new File(DB_PATH);
        Log.v("Before import11","Before import11");
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Log.v("After export","After export");
            Toast.makeText(context, "DB Imported!", Toast.LENGTH_SHORT).show();
        } catch(IOException e) {
            e.printStackTrace();
        }

//        String inputFileName = DB_GETPATH;
//        InputStream myInput = new FileInputStream(inputFileName);
//        String outFileName = DB_PATH;
//        OutputStream myOutput = new FileOutputStream(outFileName);
//
//        byte[] buffer = new byte[1024];
//        int length;
//        while ((length = myInput.read(buffer)) > 0) {
//            myOutput.write(buffer, 0, length);
//        }
//
//        myOutput.flush();
//        myOutput.close();
//        myInput.close();

    }


    public void openDataBase() throws SQLException {
//        SQLiteDatabase db = null;
        try {

            String myPath = DB_PATH;
            File file = new File(myPath);
            file.setWritable(true);
            if (file.exists() && !file.isDirectory()) {
                db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
            }
            if (db != null) {
                db.close();
            }

        }catch (SQLException e)
        {
            //
        }
    }
//    @Override
//    public synchronized void close() {
//        if(db != null)
//            db.close();
//        super.close();
//    }

    @Override
        public void onCreate(SQLiteDatabase db) {
//        db.execSQL(" Create table " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRSTNAME TEXT, LASTNAME TEXT, FATHERNAME TEXT,PHONE TEXT, EMAIL TEXT, PASS TEXT)" );

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + DB_NAME);
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
       try {
         db.insert(TABLE_NAME, null, co);
           db.close();
       }catch (Exception e)
       {
           Log.e("DB ERROR", e.toString());
           e.printStackTrace();
       }

    }

    public Cursor readData(String phone){
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_NAME,null,"PHONE = ? ",new String[]{phone},null,null,null);
    }

//    public Cursor readData1(String phone){
//            Cursor cursor = null;
//            try{
//                cursor = db.query(TABLE_NAME,null,"PHONE = ? ",new String[]{phone},null,null,null);
//                cursor.close();
//            }catch (SQLException e){
//                Log.e("DB Error", e.toString());
//                e.printStackTrace();
//            }
//            return cursor;
//    }

    }
