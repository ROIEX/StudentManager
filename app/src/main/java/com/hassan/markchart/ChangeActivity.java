package com.hassan.markchart;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Intent;
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


public class ChangeActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener {
    public long id;
    public String firstName;
    public String lastName;
    public Integer mark;
    public Integer rollNumber;
    public String dateOfBirth;
    Button changeButton;
    Button pickDateOfBirth;

    EditText changeFirstName;
    EditText changeLastName;
    EditText changeMark;
    EditText changeRollNumber;

    SimpleDBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        Intent intent=getIntent();
        helper=SimpleDBHelper.getInstance(this);
        changeButton=(Button)findViewById(R.id.button_change);
        changeFirstName=(EditText)findViewById(R.id.first_name_change_edit);
        changeLastName=(EditText)findViewById(R.id.last_name_change_edit);
        changeMark=(EditText)findViewById(R.id.marks_change_edit);
        changeRollNumber=(EditText)findViewById(R.id.rollnum_change_edit);
        pickDateOfBirth=(Button)findViewById(R.id.pick_change_button);
        id=intent.getLongExtra("id",0);
        firstName=intent.getStringExtra("firstName");
        lastName=intent.getStringExtra("lastName");
        mark=intent.getIntExtra("mark",0);
        rollNumber=intent.getIntExtra("rollNumber",0);
        dateOfBirth=intent.getStringExtra("dateOfBirth");
        changeFirstName.setText(firstName);
        changeLastName.setText(lastName);
        changeMark.setText(mark.toString());
        changeRollNumber.setText(rollNumber.toString());
        pickDateOfBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                android.support.v4.app.DialogFragment newFragment=new DatePickerFragment();
                newFragment.show(getSupportFragmentManager(), "datePickerFragment");

            }
        });

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    firstName=changeFirstName.getText().toString();
                    if(firstName.isEmpty())throw new Exception("Empty field");
                    lastName=changeLastName.getText().toString();
                    if(lastName.isEmpty())throw new Exception("Empty field");
                    mark=Integer.parseInt(changeMark.getText().toString());
                    rollNumber=Integer.parseInt(changeRollNumber.getText().toString());
                    //dateOfBirth=changeDateOfBirth.getText().toString();
                    helper.updateStudent(id,
                            firstName,
                            lastName,
                            mark,
                            rollNumber,
                            dateOfBirth);
                    finish();
                }catch(NumberFormatException e){
                    Toast.makeText(ChangeActivity.this, "Incorrect input",Toast.LENGTH_LONG).show();
                }catch(Exception e){
                    Toast.makeText(ChangeActivity.this, "Empty fields",Toast.LENGTH_LONG).show();
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_change, menu);
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
            setResult(Activity.CONTEXT_INCLUDE_CODE);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day){
        dateOfBirth=day+"/"+(month+1)+"/"+year;
    }
}
