package com.example.signupsqlite;



import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    EditText firstname, lastname, fathername, phonenumber, mail, password;
    EditText EnterPhone;
    Button button_register, buttonReadData, importDBFile, exportDB;
    RegisterDB sqliteDB;
    TextView ReadName, ReadMail;
    int PERMISSION_CODE = 100;
    File source;

    int PICKFILE_RESULT_CODE = 1;
    public static final String DATABASE_NAME = "testDB.db";
    public static final String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/SignUpDatabase/"+DATABASE_NAME;
    public static final String DB_NEWPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+DATABASE_NAME;
    File DB = new File(DB_PATH);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstname = findViewById(R.id.FirstName);
        lastname = findViewById(R.id.LastName);
        fathername = findViewById(R.id.FName);
        phonenumber = findViewById(R.id.Phone);
        mail = findViewById(R.id.Mail);
        password = findViewById(R.id.Password);
        EnterPhone = findViewById(R.id.ReadPhone);

        ReadName =  findViewById(R.id.tvName);
        ReadMail =  findViewById(R.id.tvmail);
        importDBFile = findViewById(R.id.buttonGetFile);
        exportDB = findViewById(R.id.buttonExpoortDB);


        checkPermissions();

        // Exporting DB File........
        exportDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB1();
//                Toast.makeText(MainActivity.this, "Click Export DB", Toast.LENGTH_SHORT).show();
            }
        });


        // Importing DB File........
        importDBFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                importDB();
                PickFile_Db();
//                Toast.makeText(MainActivity.this, "Click ImportDBFile Button", Toast.LENGTH_SHORT).show();
            }
        });


        sqliteDB = new RegisterDB(MainActivity.this);

        buttonReadData = findViewById(R.id.buttonReadData);
        button_register = findViewById(R.id.buttonRegister);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Submit();
//                Toast.makeText(MainActivity.this, "Click Register Button", Toast.LENGTH_SHORT).show();
            }
        });

        buttonReadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!DB.exists()) {
                    Toast.makeText(MainActivity.this, "File Not Found ", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ReadData();
                }
            }
        });

    }

    private void PickFile_Db() {
        Intent chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFile.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFile.setType("*/*");
        startActivityForResult(
                Intent.createChooser(chooseFile, "Choose a file"),
                PICKFILE_RESULT_CODE
        );
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            String src = uri.getPath();
            source = new File(src);

            Log.v("Getpath", "getpath:" + source.getPath());

            if (source.exists()) {
                FileChannel source1 = null;
                FileChannel destination = null;

                File backupDB = new File(DB_PATH);
                Log.v("Before import", "Before import");
                try {
                    source1 = new FileInputStream(source).getChannel();
                    destination = new FileOutputStream(backupDB).getChannel();
                    destination.transferFrom(source1, 0, source1.size());
                    source1.close();
                    destination.close();
                    Log.v("After import", "After import");
                    Toast.makeText(this, "DB Imported!", Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            Toast.makeText(this, "File Not Found Exception !", Toast.LENGTH_SHORT).show();
        }


    }

    private boolean checkPermissions(){

            // If you have access to the external storage, do whatever you need
            if (Environment.isExternalStorageManager()){
                // If you don't have access, launch a new activity to show the user the system's dialog
                // to allow access to the external storage
            }else{
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                Uri uri = Uri.fromParts("package", this.getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }



            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                return true;
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
                return false;
            }
        }


    private void importDB() {
        File currentDB = new File(DB_NEWPATH);
        if(currentDB.exists()) {
            FileChannel source = null;
            FileChannel destination = null;

            File backupDB = new File(DB_PATH);
            Log.v("Before import", "Before import");
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Log.v("After import", "After import");
                Toast.makeText(this, "DB Imported!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Toast.makeText(this, "File Not Found Directory !", Toast.LENGTH_SHORT).show();
        }
    }


    private void exportDB1() {

        FileChannel source=null;
        FileChannel destination=null;

        File currentDB = new File(DB_PATH);
        File backupDB = new File(DB_NEWPATH);
        Log.v("Before export","Before export");
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Log.v("After export","After export");
            Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void ReadData() {
        String phone = EnterPhone.getText().toString();
        Cursor res = sqliteDB.readData(phone);
//       if(phone.isEmpty()){
//           EnterPhone.setError("Please Enter PhoneNumber");
//           EnterPhone.requestFocus();
//           return;
//       }
            if (res.getCount() == 0) {
                EnterPhone.setError("Please Enter PhoneNumber");
                EnterPhone.requestFocus();

//                Toast.makeText(MainActivity.this, "No data is available", Toast.LENGTH_LONG).show();
                return;
            } else {

                if (res.moveToNext()) {

                    String Name = res.getString(1);
                    String Mail = res.getString(5);

                    ReadName.setText(Name);
                    ReadMail.setText(Mail);
                }
                EnterPhone.setText("");
            }
        }


    public void Submit(){
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String fatherName = fathername.getText().toString();
        String email = mail.getText().toString();
        String phone = phonenumber.getText().toString();
        String pass = password.getText().toString();

        if (fname.isEmpty() || lname.isEmpty() || fatherName.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "please enter all the data...", Toast.LENGTH_SHORT).show();
            return;
        }else {

            //*** SQLite data store code **//
            sqliteDB.addEmpData(fname, lname, fatherName, phone, email, pass);
            Toast.makeText(this, "Register Details added to sqliteDB", Toast.LENGTH_SHORT).show();
            firstname.setText("");
            lastname.setText("");
            fathername.setText("");
            mail.setText("");
            phonenumber.setText("");
            password.setText("");

        }
    }
}










//        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/"+"testDB.db");
//        if (!f.exists()) {
//            f.mkdirs();
//        }