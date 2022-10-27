package com.example.loginsqllite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.os.Environment;
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
    Button button_register, buttonReadData, buttonExport, buttonImport;
    RegisterDB sqliteDB;
//    DatabaseOpenHelper sqliteDB;
    TextView ReadName, ReadMail;

    public static final String DATABASE_NAME = "RegisterData.db";
    public static final String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/LoginDatabase/" + DATABASE_NAME;
    public static final String DB_NEWPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/NewDatabase/" + DATABASE_NAME;

    @SuppressLint("MissingInflatedId")
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


        sqliteDB = new RegisterDB(MainActivity.this);


        buttonReadData = findViewById(R.id.buttonReadData);
        button_register = findViewById(R.id.buttonRegister);
        buttonExport = findViewById(R.id.buttonExport);
        buttonImport = findViewById(R.id.buttonImport);

        buttonImport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImportDB();
//                Toast.makeText(MainActivity.this, "Click on ImportButton", Toast.LENGTH_SHORT).show();
            }
        });

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
                ReadData();
            }
        });
        
        buttonExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExportDB();
            }
        });

    }

    private void ImportDB() {
        FileChannel source=null;
        FileChannel destination=null;

        File currentDB = new File(DB_NEWPATH);
        File backupDB = new File(DB_PATH);
        Log.v("Before import","Before import");
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            Log.v("After import","After import");
            Toast.makeText(this, "DB Imported!", Toast.LENGTH_LONG).show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }

    private void ExportDB() {
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
        if (res.getCount() == 0) {

            Toast.makeText(MainActivity.this, "No data is available", Toast.LENGTH_LONG).show();

        } else {

            if (res.moveToNext()) {

                String Name = res.getString(1);
                String Mail = res.getString(5);

                ReadName.setText(Name);
                ReadMail.setText(Mail);
            }
        }
    }

    public void Submit(){
        String fname = firstname.getText().toString();
        String lname = lastname.getText().toString();
        String fatherName = fathername.getText().toString();
        String email = mail.getText().toString();
        String phone = phonenumber.getText().toString();
        String pass = password.getText().toString();

//        if (fname.isEmpty() || lname.isEmpty() || fatherName.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
//            Toast.makeText(this, "please enter all the data...", Toast.LENGTH_SHORT).show();
//            return;
//        }else {

            //*** SQLite data store code **//
            sqliteDB.addEmpData(fname, lname, fatherName, phone, email, pass);
            Toast.makeText(this, "Register Details added to sqliteDB", Toast.LENGTH_SHORT).show();
            firstname.setText("");
            lastname.setText("");
            fathername.setText("");
            mail.setText("");
            phonenumber.setText("");
            password.setText("");

//        }
    }
}