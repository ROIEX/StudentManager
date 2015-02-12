package com.hassan.markchart;

/**
 * Created by Dan on 08.01.2015.
 */
public class Student {
    public long id;
    public String firstName;
    public String lastName;
    public Integer mark;
    public Integer rollNumber;
    public String dateOfBirth;
    public Student(String fn, String ln, Integer m, Integer rn, String dob){
        firstName=fn;
        lastName=ln;
        mark=m;
        rollNumber=rn;
        dateOfBirth=dob;
    }
    public Student(long _id, String fn, String ln, Integer m, Integer rn, String dob){
        id=_id;
        firstName=fn;
        lastName=ln;
        mark=m;
        rollNumber=rn;
        dateOfBirth=dob;
    }
}
