package com.example.Signupsqlite;

import static com.example.Signupsqlite.RegisterDB.TABLE_NAME;
import static com.example.Signupsqlite.RegisterDB.TABLE_NAME1;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.signupsqlite.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class ShowTable_Data extends AppCompatActivity {

    JSONObject dataa;
    JSONArray arrayData1 ;

    RegisterDB sqliteDB;
    String[] items = new String[] {"Default", TABLE_NAME, TABLE_NAME1};
    ArrayList<String> arrTblNames;

    String[] temp;
    String str;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_table_data);

        sqliteDB = new RegisterDB(ShowTable_Data.this);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(ShowTable_Data.this,android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        GetColumNames();

        try {
            GetTabelNames();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Object item = parent.getItemAtPosition(position);
                if(item == TABLE_NAME) {
                    try {
                        ShowData_From_Table();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(ShowTable_Data.this, "click on", Toast.LENGTH_SHORT).show();
                }else {
                    if (item == TABLE_NAME1) {
                        Toast.makeText(ShowTable_Data.this, "Krishna", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void GetTabelNames() throws JSONException {

        Cursor c = sqliteDB.readTableNames();
        arrTblNames = new ArrayList<String>();


        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                arrTblNames.add(c.getString(0));
                c.moveToNext();
            }
        }
        Log.v("","krishna table Names: "+arrTblNames.toString());
    }

    private void GetColumNames() {
            Cursor c = sqliteDB.ReadTable_ColumnNames();
            String[] columnNames;
            try {
                columnNames = c.getColumnNames();
            } finally {
                c.close();
            }

            Log.v("", "Krishna columnNames: " + Arrays.toString(columnNames));


        }


        private void ReadTableData() throws JSONException {

            dataa  = new JSONObject();
            arrayData1 = new JSONArray();

            Cursor c = sqliteDB.ReadTableData();
            Cursor c1 = sqliteDB.ReadTable_ColumnNames();
            String[] columnNames;
            try {
                columnNames = c1.getColumnNames();
            } finally {
                c1.close();
            }
            Log.v("", "Krishna columnNames: " + Arrays.toString(columnNames));

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


    }

    private void ShowData_From_Table() throws JSONException {
        try {
            ReadTableData();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.LinearTxtView);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(10, 10, 10, 10);

        LinearLayout horizalLinear=null;


        for (int rows = 0; rows < arrayData1.length(); rows++) {  //rows

//            for(int column=0; column<arrayData1.length() ;column++) {  //column

                horizalLinear=new LinearLayout(this);
                horizalLinear.setOrientation(LinearLayout.HORIZONTAL);

                TextView textView1 = null;

                for(int columns = 0; columns < arrayData1.getJSONArray(rows).length(); columns++) {

                    System.out.print(arrayData1.getJSONArray(rows).getString(columns));

                    if(rows == 0){
                        textView1 = new TextView(this);
                        textView1.setLayoutParams(layoutParams);
                        textView1.setText(arrayData1.getJSONArray(rows).getString(columns)); //get data from db and set here
                        textView1.setTextColor(getColor(android.R.color.holo_red_dark));
                        textView1.setBackgroundResource(R.drawable.background_style);
                        textView1.setPadding(20, 20, 20, 20);
                        horizalLinear.addView(textView1);
                    }
                    else {
                        textView1 = new TextView(this);
                        textView1.setLayoutParams(layoutParams);
                        textView1.setText(arrayData1.getJSONArray(rows).getString(columns)); //get data from db and set here
                        textView1.setTextColor(getColor(android.R.color.holo_green_dark));
                        textView1.setBackgroundResource(R.drawable.background_style);
                        textView1.setPadding(20, 20, 20, 20);
                        horizalLinear.addView(textView1);
                    }
                }

//            }

            linearLayout.addView(horizalLinear);
        }
    }
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