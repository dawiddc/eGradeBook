package org.dawiddc.egradebook.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MockModel {
    private static final List<Student> studentsList = new ArrayList<>();

    static {
        List<Grade> grades = new ArrayList<>();
        grades.add(
                new Grade.GradeBuilder().id()
                        .value(4)
                        .date(new Date("2018/04/06"))
                        .course(new Course.CourseBuilder().id().name("Math").lecturer("Matt Jepard").build())
                        .build()
        );
        grades.add(
                new Grade.GradeBuilder().id()
                        .value(5)
                        .date(new Date("2018/04/04"))
                        .course(new Course.CourseBuilder().id().name("TPSI").lecturer("Tomasz Pawlak").build())
                        .build()
        );
        studentsList.add(
                new Student.StudentBuilder().index()
                        .birthday(new Date("1995/07/23"))
                        .firstName("John")
                        .lastName("Doe")
                        .grades(grades)
                        .build()
        );

        grades = new ArrayList<>();

        grades.add(
                new Grade.GradeBuilder().id()
                        .value((float) 3.5)
                        .date(new Date("2018/04/02"))
                        .course(new Course.CourseBuilder().id().name("TPAL").lecturer("Adam Kotarski").build())
                        .build()
        );
        grades.add(
                new Grade.GradeBuilder().id()
                        .value((float) 4.5)
                        .date(new Date("2018/04/01"))
                        .course(new Course.CourseBuilder().id().name("ABCD").lecturer("Jett Mall").build())
                        .build()
        );

        studentsList.add(
                new Student.StudentBuilder().index()
                        .birthday(new Date("1994/02/13"))
                        .firstName("John")
                        .lastName("Doe")
                        .grades(grades)
                        .build()
        );

        grades = new ArrayList<>();

        grades.add(
                new Grade.GradeBuilder().id()
                        .value((float) 4)
                        .date(new Date("2018/04/04"))
                        .course(new Course.CourseBuilder().id().name("TWO").lecturer("Andrzej Zarcha").build())
                        .build()
        );
        grades.add(
                new Grade.GradeBuilder().id()
                        .value((float) 4.5)
                        .date(new Date("2018/04/05"))
                        .course(new Course.CourseBuilder().id().name("English").lecturer("Olivia Bolton").build())
                        .build()
        );

        studentsList.add(
                new Student.StudentBuilder().index()
                        .birthday(new Date("1992/11/30"))
                        .firstName("John")
                        .lastName("Doe")
                        .grades(grades)
                        .build()
        );

    }

    private MockModel() {}

    public static List<Student> getInstance() {
        return studentsList;
    }
}
