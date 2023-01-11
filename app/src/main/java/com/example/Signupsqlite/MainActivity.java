package com.example.Signupsqlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
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

import com.example.signupsqlite.R;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {

    EditText firstname, fathername, phonenumber, mail, password;
    EditText EnterPhone;
    Button button_register, buttonReadData, importDBFile, exportDB, ShowTableData;
    RegisterDB sqliteDB;
    TextView ReadName, ReadMail;

    int PERMISSION_CODE = 100;
    int PICKFILE_RESULT_CODE = 1001;

    public static final String DATABASE_NAME = "testDB.db";
    public static final String DB_PATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/SignUpDatabase/" + DATABASE_NAME;
    public static final String DB_NEWPATH = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/SignUpDatabase/" + DATABASE_NAME;

    File currentDB = new File(DB_PATH);
    File backupDB = new File(DB_NEWPATH);


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        firstname = findViewById(R.id.FirstName);
        fathername = findViewById(R.id.FName);
        phonenumber = findViewById(R.id.Phone);
        mail = findViewById(R.id.Mail);
        password = findViewById(R.id.Password);
        EnterPhone = findViewById(R.id.ReadPhone);

        ReadName = findViewById(R.id.tvName);
        ReadMail = findViewById(R.id.tvmail);
        importDBFile = findViewById(R.id.buttonGetFile);
        exportDB = findViewById(R.id.buttonExpoortDB);
        ShowTableData = findViewById(R.id.ShowTableData);


        ShowTableData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(MainActivity.this,ShowTable_Data.class));
            }
        });


        Log.v("packageName", "PackageName :" + getPackageName());

        checkPermissions();

        // Exporting DB File........
        exportDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exportDB1();
//                exportDB2();
            }
        });


        // Importing DB File........
        importDBFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS),"SignUpDatabase");
                if (! directory.exists()){
                    directory.mkdir();
                    // If you require it to make the entire directory path including parents,
                    // use directory.mkdirs(); here instead.
                }else {
                    PickFile_Db();
//                    importDB();
                }
            }
        });


        sqliteDB = new RegisterDB(MainActivity.this);

        buttonReadData = findViewById(R.id.buttonReadData);
        button_register = findViewById(R.id.buttonRegister);

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fname = firstname.getText().toString();
                String fatherName = fathername.getText().toString();
                String email = mail.getText().toString();
                String phone = phonenumber.getText().toString();
                String pass = password.getText().toString();

                if (fname.isEmpty()  || fatherName.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
                    Toast.makeText(MainActivity.this, "please enter all the data...", Toast.LENGTH_SHORT).show();
                } else {
//                    Submit();
//                    Table2Data();

                    Submit(fname,fatherName,email,phone,pass);
                    Tabl2Data(fname,phone,email);

                    firstname.setText("");
                    fathername.setText("");
                    mail.setText("");
                    phonenumber.setText("");
                    password.setText("");
                }
            }
        });

        buttonReadData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!currentDB.exists()) {
                    Toast.makeText(MainActivity.this, "File Not Found ", Toast.LENGTH_SHORT).show();
                } else {
                    ReadData();
                }
            }
        });

      }

    /**
     * Store User Data into Local .DB (RegisterData table (1))
     * **/
        private void Submit(String fname, String fatherName, String email, String phone, String pass) {
            //*** SQLite data store code **//
            sqliteDB.addEmpData(fname, fatherName, phone, email, pass);
            Toast.makeText(this, "Register Details added to sqliteDB", Toast.LENGTH_SHORT).show();

        }

    /**
     * Store User Data into Local .DB (Student table (2))
     * **/
        public void Tabl2Data(String fname,String phone, String email){
            //*** SQLite data store code **//
            sqliteDB.addTable2Data(fname, phone, email);
        }

    /**
     * Import .db file into existing Folder (using file picker)
     * **/
    private void PickFile_Db() {

        Intent newIntent = new Intent(Intent.ACTION_GET_CONTENT);
//        newIntent.setType("application/*");
        newIntent.setType("application/octet-stream");
        startActivityForResult(newIntent,PICKFILE_RESULT_CODE);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICKFILE_RESULT_CODE && resultCode == RESULT_OK) {
            try {
                onSelectFromDBFile(data);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void onSelectFromDBFile(Intent data) throws IOException {
        if (data != null) {
            try {
                InputStream in = null;
                OutputStream out = null;
                try {
                    // open the user-picked file for reading:
                    in = getContentResolver().openInputStream(data.getData());
                    // open the output-file:
                    out = new FileOutputStream(new File(DB_PATH));
                    // copy the content:
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = in.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                    }
                    // Contents are copied!
                } finally {
                    if (in != null) {
                        in.close();
                    }
                    if (out != null) {
                        out.close();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            Toast.makeText(this, "DB Imported!", Toast.LENGTH_SHORT).show();
        }
    }



    private void checkPermissions() {

        // If you have access to the external storage, do whatever you need
        if (Environment.isExternalStorageManager()) {
            // If you don't have access, launch a new activity to show the user the system's dialog
            // to allow access to the external storage
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            Uri uri = Uri.fromParts("package", this.getPackageName(), null);
            intent.setData(uri);
            startActivity(intent);
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
        }
    }

    /**
     * Import .db file into existing Folder (Manual enter path)

    private void importDB() {
        File currentDB = new File(DB_NEWPATH);
        if (currentDB.exists()) {
            FileChannel source ;
            FileChannel destination ;

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
        } else {
            Toast.makeText(this, "File Not Found Directory !", Toast.LENGTH_SHORT).show();
        }
    }
     **/


    /**
     * Export .db file into New Folder
     * **/
    private void exportDB1() {

        FileChannel source ;
        FileChannel destination;

        if (currentDB.exists()) {
            Log.v("Before export", "Before export");
            File directory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"SignUpDatabase");
            if (! directory.exists()) {
                directory.mkdir();
            }
            try {
                source = new FileInputStream(currentDB).getChannel();
                destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Log.v("After export", "After export");
                Toast.makeText(this, "DB Exported!", Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(this, "File Not Found Exception !", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Get Name & Mail From .db check PhoneNumber
     * **/
    private void ReadData() {
        String phone = EnterPhone.getText().toString();
        Cursor res = sqliteDB.readData(phone);

        if(phone.isEmpty()){
            Toast.makeText(this, "Please Enter PhoneNumber", Toast.LENGTH_SHORT).show();
        }
        if (res.getCount() == 0) {
            EnterPhone.setError("Invalid PhoneNumber!..");
            EnterPhone.requestFocus();
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


    @Override
    protected void onResume() {
        super.onResume();
        ReadName.setText("");
        ReadMail.setText("");

    }
}



    /**
     * Store User Data into DB file
     *
    public void Submit() {
        String fname = firstname.getText().toString();
        String fatherName = fathername.getText().toString();
        String email = mail.getText().toString();
        String phone = phonenumber.getText().toString();
        String pass = password.getText().toString();

        if (fname.isEmpty()  || fatherName.isEmpty() || email.isEmpty() || phone.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "please enter all the data...", Toast.LENGTH_SHORT).show();
        } else {
            sqliteDB.addEmpData(fname, fatherName, phone, email, pass);
            Toast.makeText(this, "Register Details added to sqliteDB", Toast.LENGTH_SHORT).show();
            firstname.setText("");
            fathername.setText("");
            mail.setText("");
            phonenumber.setText("");
            password.setText("");

        }
    }


//    public void Table2Data() {
//        String fname = firstname.getText().toString();
//        String email = mail.getText().toString();
//        String phone = phonenumber.getText().toString();
//        if (fname.isEmpty() || email.isEmpty() || phone.isEmpty()){
//
//        }else {
//            sqliteDB.addTable2Data(fname, phone, email);
//        }
//    }
    **/


    /**
     * Another way to Export File

          private void exportDB2() {
          File currentDB = new File(DB_PATH);
          File backupDB = new File(DB_NEWPATH);
          if(currentDB.exists()) {
          try {
          InputStream inputStream = new FileInputStream(currentDB);
          OutputStream outputStream = new FileOutputStream(backupDB);

          byte[] byteArrayBuffer = new byte[1024];
          int intLength;
          while ((intLength = inputStream.read(byteArrayBuffer)) > 0) {
          outputStream.write(byteArrayBuffer, 0, intLength);
          }

          inputStream.close();
          outputStream.close();

         Toast.makeText(this, "Successful !.......", Toast.LENGTH_SHORT).show();

          } catch (FileNotFoundException e) {
          e.printStackTrace();
          } catch (IOException e) {
          e.printStackTrace();
          }
          }
          }
     **/



//        File f = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/"+"testDB.db");
//        if (!f.exists()) {
//            f.mkdirs();
//        }