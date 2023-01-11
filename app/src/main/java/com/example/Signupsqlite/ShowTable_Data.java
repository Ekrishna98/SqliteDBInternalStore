package com.example.Signupsqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signupsqlite.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShowTable_Data extends AppCompatActivity {

        Button ClearData;
        RegisterDB sqliteDB;
//    String[] items = new String[] {"Default", TABLE_NAME, TABLE_NAME1};

        public List<String> GetTableNames;
        Spinner tableSelection;
        ArrayAdapter<String> GetTableList;
        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_table_data);

            getSupportActionBar().setTitle("Show Table Data");

            sqliteDB = new RegisterDB(ShowTable_Data.this);
            ClearData = findViewById(R.id.BtnClear);

            try {
                GetTabelNames();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            tableSelection = findViewById(R.id.spinner);
            GetTableList = new ArrayAdapter<>( ShowTable_Data.this,android.R.layout.simple_spinner_item, GetTableNames);
            GetTableList.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            tableSelection.setAdapter(GetTableList);

            //        GetColumNames();


            tableSelection.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Object item = parent.getItemAtPosition(position);
                    Log.v("","krishna item Names: "+item);
                    try {
                        if(position>0) {
                                ShowData_From_Table(item);
                            }else{
                            if(MainLayout != null){
                                MainLayout.removeAllViews();
                            }
                        }
                    }
                    catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            ClearData.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (MainLayout != null) {
                        MainLayout.removeAllViews();
                        MainLayout =null;
                        tableSelection.setAdapter(GetTableList);
                    }else {
                        Log.v("","ClearData :");
                        Toast.makeText(ShowTable_Data.this, "Please get Data First!...", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        }

            /** Another Way Get Table Names
             *
        //    private void GetTabelNames() throws JSONException {
        //
        //        Cursor c = sqliteDB.readTableNames();
        //        arrTblNames = new ArrayList<String>();
        //
        //        String[] table = null;
        //
        //
        //        if (c.moveToFirst()) {
        //            while (!c.isAfterLast()) {
        //                arrTblNames.add(c.getString(0));
        //                c.moveToNext();
        //            }
        //        }
        //
        //        Log.v("","krishna table Names: "+arrTblNames.toString());
        //    }
            **/


        private void GetTabelNames() throws JSONException {

            Cursor c = sqliteDB.readTableNames();
            GetTableNames = new ArrayList<>();
            GetTableNames.add("Select-Table");
            for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                for (int i = 0; i < c.getCount()-1; i++) {
                    GetTableNames.add(c.getString(i));
                }
            }
            Log.v("", "krishna table Names: " + GetTableNames);
        }

            /** GetAll Table Column Names
             *
        //    private void GetColumNames(Object item) {
        //            Cursor c = sqliteDB.ReadTable_ColumnNames(item);
        //            String[] columnNames;
        //            try {
        //                columnNames = c.getColumnNames();
        //            } finally {
        //                c.close();
        //            }
        //            Log.v("", "Krishna columnNames: " + Arrays.toString(columnNames));
        //        }
                    **/

        LinearLayout MainLayout;

     private void ShowData_From_Table(Object item) throws JSONException {
            JSONArray arraydata =ReadTableData(item);

            MainLayout =null ;

           MainLayout = findViewById(R.id.MainLinearLayout);
            LinearLayout.LayoutParams MainlayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            if(MainLayout != null) {
                Log.v("","MainLayoout View");
                MainLayout.removeAllViews();
            }

             LinearLayout horizalLinear;
            for (int rows = 0; rows < arraydata.length(); rows++) {  //rows
//            for(int column=0; column<arraydata.length() ;column++) {  //column
                horizalLinear = new LinearLayout(this);
                horizalLinear.setOrientation(LinearLayout.HORIZONTAL);

                LinearLayout.LayoutParams ChildlayoutParams = new LinearLayout.LayoutParams(230, ViewGroup.LayoutParams.WRAP_CONTENT);
                ChildlayoutParams.setMargins(5, 5, 5, 5);
                horizalLinear.setLayoutParams(MainlayoutParams);
//                horizalLinear.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);

                TextView textView1;
                for (int columns = 0; columns < arraydata.getJSONArray(rows).length(); columns++) {
                    System.out.print(arraydata.getJSONArray(rows).getString(columns));

                    if (rows == 0) {
                        textView1 = new TextView(this);
                        textView1.setLayoutParams(ChildlayoutParams);
                        textView1.setText(arraydata.getJSONArray(rows).getString(columns)); //get data from db and set here
                        textView1.setTextColor(getColor(R.color.black));
                        textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        textView1.setTextSize(16);
                        textView1.setBackgroundResource(R.drawable.background_headers);
                        textView1.setPadding(20, 20, 20, 20);
                        horizalLinear.addView(textView1);
                    } else {
                        textView1 = new TextView(this);
                        textView1.setLayoutParams(ChildlayoutParams);
                        textView1.setText(arraydata.getJSONArray(rows).getString(columns)); //get data from db and set here
                        textView1.setTextColor(getColor(android.R.color.holo_green_dark));
                        textView1.setBackgroundResource(R.drawable.background_style);
                        textView1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                        textView1.setGravity(Gravity.CENTER | Gravity.CENTER_VERTICAL);
                        textView1.setTextSize(16);
                        textView1.setPadding(10, 10, 10, 10);
                        horizalLinear.addView(textView1);
                    }
                }
                //        }
                MainLayout.addView(horizalLinear);
            }

     }

        private JSONArray ReadTableData(Object item) throws JSONException {

            JSONArray arrayData1 = new JSONArray();

            Cursor c = sqliteDB.ReadTableData(item);
            Cursor c1 = sqliteDB.ReadTable_ColumnNames(item);

            String[] columnNames;
            try {
                columnNames = c1.getColumnNames();
            } finally {
                c1.close();
            }
            Log.v("", "Table columnNames: " + Arrays.toString(columnNames));

            c.getCount();
            c.getColumnCount();
            Log.v("","rows : "+ c.getCount() + " & columns : "+ c.getColumnCount());

            if (c.getCount() != 0) {
                c.moveToNext();
                JSONArray arrayColumn = new JSONArray(columnNames);
                arrayData1.put(arrayColumn);

                for (int i = 0; i < c.getCount(); i++) {
                    JSONArray arrayData = new JSONArray();
                    for (int j = 0; j < c.getColumnCount(); j++) {
                        arrayData.put(c.getString(j));
                    }
                    c.moveToNext();
                    arrayData1.put(arrayData);
                }
                Log.d("adfasdc", arrayData1.toString());
            }
            return arrayData1;
        }


//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (MainLayout != null) {
//            MainLayout.removeAllViews();
//            MainLayout =null;
//            tableSelection.setAdapter(GetTableList);
//        }
//    }
}


        /**

         public void getDatabaseStructure(SQLiteDatabase db) {

         Cursor c = db.rawQuery(
         "SELECT name FROM sqlite_master WHERE type='table'", null);
         ArrayList<String[]> result = new ArrayList<String[]>();
         int i = 0;
         result.add(c.getColumnNames());
         for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
         String[] temp = new String[c.getColumnCount()];
         for (i = 0; i < temp.length; i++) {
         temp[i] = c.getString(i);
         System.out.println("TABLE - " + temp[i]);


         Cursor c1 = db.rawQuery(
         "SELECT * FROM " + temp[i], null);
         c1.moveToFirst();
         String[] COLUMNS = c1.getColumnNames();
         for (int j = 0; j < COLUMNS.length; j++) {
         c1.move(j);
         System.out.println("    COLUMN - " + COLUMNS[j]);
         }
         }
         result.add(temp);
         }

         Log.v("","table kkkk :"+result);


         }

 **/