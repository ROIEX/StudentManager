package com.hassan.markchart;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ViewActivity extends ActionBarActivity {
    GridView studentGrid;
    StudentAdapter studentAdapter;
    Button searchButton;
    Button exportButton;
    EditText searchEdit;
    List<Student> studentList;
    SimpleDBHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        studentAdapter=new StudentAdapter(this);
        searchButton=(Button)findViewById(R.id.button_search);
        exportButton=(Button)findViewById(R.id.button_export);
        searchEdit=(EditText)findViewById(R.id.search_field);
        helper=SimpleDBHelper.getInstance(this);
        studentList=helper.getAllStudents();
        studentGrid=(GridView)findViewById(R.id.gridView);
        studentGrid.setAdapter(studentAdapter);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!searchEdit.getText().equals("")) {
                    studentList = helper.search(searchEdit.getText().toString());
                    studentAdapter.notifyDataSetChanged();
                    studentGrid.setAdapter(studentAdapter);
                }
            }
        });
        exportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HSSFWorkbook hwb=new HSSFWorkbook();
                HSSFSheet studentsSheet=hwb.createSheet("students");
                {
                    HSSFRow row = studentsSheet.createRow(0);
                    HSSFCell cell = row.createCell(0);
                    cell.setCellValue(getString(R.string.firstname_string));
                    cell = row.createCell(1);
                    cell.setCellValue(getString(R.string.lastname_string));
                    cell = row.createCell(2);
                    cell.setCellValue(getString(R.string.marks_string));
                    cell = row.createCell(3);
                    cell.setCellValue(getString(R.string.rollnum_string));
                    cell = row.createCell(4);
                    cell.setCellValue(getString(R.string.dob_string));
                }
                for(int i=0;i<studentList.size();i++)
                {
                    Student s=studentList.get(i);
                    HSSFRow row=studentsSheet.createRow(i+1);
                    HSSFCell cell=row.createCell(0);
                    cell.setCellValue(s.firstName);
                    cell=row.createCell(1);
                    cell.setCellValue(s.lastName);
                    cell=row.createCell(2);
                    cell.setCellValue(s.mark);
                    cell=row.createCell(3);
                    cell.setCellValue(s.rollNumber);
                    cell=row.createCell(4);
                    cell.setCellValue(s.dateOfBirth);
                }
                try{
                    FileOutputStream fos=new FileOutputStream(Environment.getExternalStorageDirectory()+"/export.xls");
                    hwb.write(fos);
                    fos.close();
                }catch(FileNotFoundException e){
                    e.printStackTrace();
                }
                catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
        studentGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent myIntent=new Intent(ViewActivity.this, ChangeActivity.class);
                myIntent.putExtra("id", studentList.get(position).id);
                myIntent.putExtra("firstName", studentList.get(position).firstName);
                myIntent.putExtra("lastName", studentList.get(position).lastName);
                myIntent.putExtra("mark", studentList.get(position).mark);
                myIntent.putExtra("rollNumber", studentList.get(position).rollNumber);
                myIntent.putExtra("dateOfBirth", studentList.get(position).dateOfBirth);
                ViewActivity.this.startActivityForResult(myIntent, 0);
            }
        });
        studentGrid.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                final int removal_position=position;
                AlertDialog.Builder alert=new AlertDialog.Builder(ViewActivity.this);
                alert.setTitle("Do you want to delete this entry?");
                alert.setMessage("Do you want to delete this entry?");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        helper.removeStudent(studentList.get(removal_position).id);
                        studentList.remove(removal_position);
                        studentAdapter.notifyDataSetChanged();
                        studentGrid.setAdapter(studentAdapter);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                alert.show();
                return true;
            }
        });
        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count == 0){
                    studentList=helper.getAllStudents();
                    studentAdapter.notifyDataSetChanged();
                    studentGrid.setAdapter(studentAdapter);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public class StudentAdapter extends BaseAdapter{
        Context MyContext;

        public StudentAdapter(Context _MyContext){
            MyContext=_MyContext;
        }


        public int getCount() {
            return studentList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            View v;
            if(convertView==null){
                LayoutInflater li=getLayoutInflater();
                v= li.inflate(R.layout.gridview_item, null);
                TextView tvFirstName=(TextView)v.findViewById(R.id.grid_first_name);
                TextView tvLastName=(TextView)v.findViewById(R.id.grid_last_name);
                TextView tvMark=(TextView)v.findViewById(R.id.grid_mark);
                TextView tvRollName=(TextView)v.findViewById(R.id.grid_roll_name);
                TextView tvDOB=(TextView)v.findViewById(R.id.grid_dob);
                tvFirstName.setText(studentList.get(position).firstName);
                tvLastName.setText(studentList.get(position).lastName);
                tvMark.setText(studentList.get(position).mark.toString());
                tvRollName.setText(studentList.get(position).rollNumber.toString());
                tvDOB.setText(studentList.get(position).dateOfBirth);
            }
            else
            {
                v=convertView;
            }
            return v;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        if(resultCode == Activity.CONTEXT_INCLUDE_CODE){
            finish();
        }else{
            studentList=helper.getAllStudents();
            studentAdapter.notifyDataSetChanged();
            studentGrid.setAdapter(studentAdapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view, menu);
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
}
