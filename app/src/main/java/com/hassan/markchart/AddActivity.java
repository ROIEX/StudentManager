package com.hassan.markchart;

import android.app.DatePickerDialog;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;


public class AddActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener {
    String firstName;
    String lastName;
    Integer marks;
    Integer rollNum;
    String birthDate;
    EditText firstName_edit;
    EditText lastName_edit;
    EditText marks_edit;
    EditText rollnum_edit;
    Button dob_pick;
    Button add_button;
    //DB
    SimpleDBHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        helper=SimpleDBHelper.getInstance(this);
        firstName_edit=(EditText)findViewById(R.id.first_name_edit);
        lastName_edit=(EditText)findViewById(R.id.last_name_edit);
        marks_edit=(EditText)findViewById(R.id.marks_edit);
        add_button=(Button)findViewById(R.id.add_button);
        rollnum_edit=(EditText)findViewById(R.id.rollnum_edit);
        dob_pick=(Button)findViewById(R.id.pick_add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    firstName = firstName_edit.getText().toString();
                    if (firstName.isEmpty()) throw new Exception("Empty field");
                    lastName = lastName_edit.getText().toString();
                    if (lastName.isEmpty()) throw new Exception("Empty field");
                    marks = Integer.parseInt(marks_edit.getText().toString());
                    rollNum = Integer.parseInt(rollnum_edit.getText().toString());
                    if(birthDate==null)throw new Exception("Empty field");
                    helper.addStudent(new Student(firstName, lastName, marks, rollNum, birthDate));
                    Toast.makeText(AddActivity.this,"Record added",Toast.LENGTH_LONG).show();
                    finish();
                }catch(NumberFormatException e){
                    Toast.makeText(AddActivity.this, "Incorrect input",Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(AddActivity.this, "Empty fields",Toast.LENGTH_LONG).show();
                }
                //birthDate=dob_edit.getText().toString();


                //SQLiteDatabase database=openOrCreateDatabase("marks_db",MODE_PRIVATE,null);
                //database.execSQL("CREATE TABLE IF NOT EXISTS Marks(firstname VARCHAR,lastname VARCHAR, marks INTEGER, rollnum INTEGER, birth VARCHAR);");
                //database.execSQL("INSERT INTO Marks VALUES('"+firstName+"','"+lastName+"','"+marks.toString()+"','"+rollNum.toString()+"','"+birthDate+"');");
            }
        });
        dob_pick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment newFragment=new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickerFragment");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        birthDate=day+"/"+(month+1)+"/"+year;
    }
}
