package org.dawiddc.egradebook.utils;

import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.model.Student;
import org.mongodb.morphia.Datastore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class InitializeDatabase {
    private static List<Student> studentList = new ArrayList<>();
    private static List<Course> coursesList = new ArrayList<>();

    public static void initializeStudents() throws ParseException {
        Datastore datastore = DatastoreHandler.getInstance().getDatastore();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        studentList.add(new Student.StudentBuilder()
                .birthday(formatter.parse("1995-07-23"))
                .firstName("John")
                .lastName("Doe")
                .grades(new ArrayList<>())
                .build());
        studentList.add(new Student.StudentBuilder()
                .birthday(formatter.parse("1994-02-13"))
                .firstName("Diane")
                .lastName("Pittsburg")
                .grades(new ArrayList<>())
                .build()
        );
        studentList.add(
                new Student.StudentBuilder()
                        .birthday(formatter.parse("1992-11-30"))
                        .firstName("Matthew")
                        .lastName("Pettigrew")
                        .grades(new ArrayList<>())
                        .build()
        );

        datastore.save(studentList);
    }
}
