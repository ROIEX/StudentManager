package com.hassan.markchart;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SimpleDBHelper extends SQLiteOpenHelper {
    //version
    private static final int DATABASE_VERSION = 1;
    //db name
    private static SimpleDBHelper mInstance=null;

    private static final String DATABASE_NAME="marks_db";
    //columns
    private static final String TABLE_MARKS="Marks";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRST_NAME = "first_name";
    private static final String KEY_LAST_NAME = "last_name";
    private static final String KEY_MARK = "mark";
    private static final String KEY_ROLL_NUMBER = "roll_number";
    private static final String KEY_DOB = "dob";

    public static SimpleDBHelper getInstance(Context ctx){
        if(mInstance==null){
            mInstance = new SimpleDBHelper(ctx.getApplicationContext());
        }
        return mInstance;
    }

    public SimpleDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE = "CREATE TABLE " + TABLE_MARKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_FIRST_NAME + " TEXT,"
                + KEY_LAST_NAME + " TEXT,"
                + KEY_MARK + " INTEGER,"
                + KEY_ROLL_NUMBER + " INTEGER,"
                + KEY_DOB + " TEXT)";
        db.execSQL(SQL_CREATE);
    }

    public long addStudent(Student student){
        long rv=0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put(KEY_FIRST_NAME, student.firstName);
            cv.put(KEY_LAST_NAME, student.lastName);
            cv.put(KEY_MARK, student.mark);
            cv.put(KEY_ROLL_NUMBER, student.rollNumber);
            cv.put(KEY_DOB, student.dateOfBirth);
            rv = db.insert(TABLE_MARKS, null, cv);
            db.close();
        }finally {
            return rv;
        }
    }

    public Student getStudent(Long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.query(TABLE_MARKS,
                new String[]{KEY_ID, KEY_FIRST_NAME, KEY_LAST_NAME, KEY_MARK, KEY_ROLL_NUMBER, KEY_DOB},
                KEY_ID+"=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null,
                null);
        if(cursor!=null){
            cursor.moveToFirst();
        }
        Student student=new Student(cursor.getInt(0),cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
        db.close();
        return student;
    }

    public void removeStudent(Long id){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(TABLE_MARKS,"id=?",new String[] {id.toString()});
        db.close();
    }

    public void updateStudent(Long id, String firstName, String lastName, Integer mark, Integer rollNumber,String dateOfBirth){
        SQLiteDatabase db = this.getWritableDatabase();
        try {
            ContentValues cv = new ContentValues();
            cv.put(KEY_FIRST_NAME, firstName);
            cv.put(KEY_LAST_NAME, lastName);
            cv.put(KEY_MARK, mark);
            cv.put(KEY_ROLL_NUMBER, rollNumber);
            cv.put(KEY_DOB, dateOfBirth);
            String where = "id=?";
            String[] whereArgs = {Long.toString(id)};
            db.update(TABLE_MARKS, cv, where, whereArgs);

        }finally {
            db.close();
        }
    }

    public List<Student> search(String query) {
        List<Student> studentList = new ArrayList<Student>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_MARKS,
                new String[]{KEY_ID, KEY_FIRST_NAME, KEY_LAST_NAME, KEY_MARK, KEY_ROLL_NUMBER, KEY_DOB},
                KEY_FIRST_NAME + "=?",
                new String[]{query},
                null,
                null,
                null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Student student = new Student(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
                    studentList.add(student);
                } while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
            db.close();
        }

        return studentList;
    }

    public List<Student> getAllStudents()
    {
        List<Student> studentList=new ArrayList<Student>();
        String selectQuery = "SELECT  * FROM " + TABLE_MARKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_MARKS,new String[]{KEY_ID,KEY_FIRST_NAME,KEY_LAST_NAME,KEY_MARK,KEY_ROLL_NUMBER,KEY_DOB}, null, null, null, null, null);
        try {
            if (cursor.moveToFirst()) {
                do {
                    Student student = new Student(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getInt(3), cursor.getInt(4), cursor.getString(5));
                    studentList.add(student);
                } while (cursor.moveToNext());
            }
        }finally {
            cursor.close();
        }

        return studentList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MARKS);
        onCreate(db);
    }
}
