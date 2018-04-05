package org.dawiddc.egradebook.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockModel {
    private static final List<Student> studentsList = new ArrayList<>();

    static {

        Student student1 = new Student();
        student1.setBirthday(new Date("23-07-1995"));
        student1.setFirstName("John");
        student1.setLastName("Doe");
        Grade grade1 = new Grade();
        grade1.setDate(new Date("12-03-1789"));
        Grade grade2 = new Grade();
        Grade grade3 = new Grade();
        Grade grade4 = new Grade();
        student1.setGrades(new ArrayList<Grade>() {new Grade(), new Grade()});

        Student student2 = new Student();
        student2.setBirthday(new Date("16-03-1993"));
        student2.setFirstName("Matt");
        student2.setLastName("Truckerson");
        student2.setGrades(new ArrayList<Grade>() {new Grade(), new Grade()});

        Student student3 = new Student();
        student3.setBirthday(new Date("10-09-1998"));
        student3.setFirstName("Jane");
        student3.setLastName("Dove");
        student3.setGrades(new ArrayList<Grade>() {new Grade(), new Grade()});

        studentsList.add(student1);

    }

    public static List<Student> getInstance(){
        return studentsList;
    }
}
