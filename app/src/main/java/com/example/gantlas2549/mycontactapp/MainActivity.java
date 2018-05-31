package com.example.gantlas2549.mycontactapp;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper myDb;
    EditText editName, editPhone, editAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editName = findViewById(R.id.editText_name);
        editAddress = findViewById(R.id.editText_address);
        editPhone = findViewById(R.id.editText_phone);

        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated myDb");
    }

    public void addData(View view) {
        Log.d("MyContactApp", "MainActivity: Add contact button pressed");

        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(), editAddress.getText().toString());
        if (isInserted) {
            Toast.makeText(this, "Success - Contact inserted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Failed - Contact not inserted", Toast.LENGTH_LONG).show();
        }
    }

    public void viewData(View view) {
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: viewData: received cursor");

        if (res.getCount() == 0) {
            showMessage("Error", "No data found in database");
            return;
        }

        StringBuffer sb = new StringBuffer();
        while(res.moveToNext()) {
            // append res column 0,1,2,3 to buffer - see StringBuffer and Cursor API
            // delimit each of the "appends" with line feed "\n"
            for (int i = 0; i < 4; i++) {
                sb.append(res.getColumnName(i) + ": " + res.getString(i) + "\n");
            }
            sb.append("\n");
        }

        showMessage("Data", sb.toString());
    }

    private void showMessage(String title, String message) {
        Log.d("MyContactApp", "MainActivity: showMessage: assembling AlertDialogue");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public static final String EXTRA_MESSAGE = "com.example.gantlas2549.mycontactapp.MESSAGE";

    public void SearchRecord(View view) {
       Log.d("MyContactApp", "MainActivity: launching my SearchActivity");
       Intent intent = new Intent(this, SearchActivity.class);
       intent.putExtra(EXTRA_MESSAGE, getRecords());
       startActivity(intent);

    }

    private String getRecords() {
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: getRecords: received cursor");
        StringBuffer sb = new StringBuffer();
        int counter = 0;
        while (res.moveToNext()) {
            if (res.getString(1).equals(editName.getText().toString())) {
                for (int i = 1; i < 4; i++) {
                    sb.append(res.getColumnName(i) + ": " + res.getString(i) + "\n");
                }
                sb.append("\n");
                counter++;
            }
        }


            return sb.toString();

    }
}
