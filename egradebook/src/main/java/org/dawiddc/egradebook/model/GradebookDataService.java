package org.dawiddc.egradebook.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GradebookDataService {

    private static final AtomicLong gradeIdCounter = new AtomicLong();
    private static GradebookDataService ourInstance = new GradebookDataService();
    private static AtomicLong studentIdCounter = new AtomicLong();
    private static AtomicLong courseIdCounter = new AtomicLong();
    private List<Student> studentsList = new ArrayList<>();
    private List<Course> coursesList = new ArrayList<>();

    public static GradebookDataService getInstance() {
        return ourInstance;
    }

    public long addStudent(Student student) {
        long index = studentIdCounter.getAndIncrement();
        student.setIndex(index);
        studentsList.add(student);
        return index;
    }

    public List<Student> getStudentsList() {
        return studentsList;
    }

    public Long addGrade(long index, Grade grade) {
        long id = gradeIdCounter.getAndIncrement();
        for (Student student : studentsList) {
            if (student.getIndex() == index) {
                grade.setId(id);
                student.getGrades().add(grade);
                return id;
            }
        }
        return null;
    }

    public List<Grade> getGradesList(long index) {
        for (Student student : studentsList) {
            if (student.getIndex() == index)
                return student.getGrades();
        }
        return null;
    }


    public long addCourse(Course course) {
        for (Course existingCourse : coursesList) {
            if (existingCourse.getName().equals(course.getName())) {
                return existingCourse.getId();
            }
        }
        long id = courseIdCounter.getAndIncrement();
        course.setId(id);
        coursesList.add(course);
        return id;
    }

    public List<Course> getCoursesList() {
        return coursesList;
    }

    public void createMockModel() {
        List<Grade> grades = new ArrayList<>();
        grades.add(
                new Grade.GradeBuilder().id()
                        .value(4)
                        .date(new Date("2018/04/06"))
                        .course(new Course.CourseBuilder().name("Math").lecturer("Matt Jepard").build())
                        .build()
        );
        grades.add(
                new Grade.GradeBuilder().id()
                        .value(5)
                        .date(new Date("2018/04/04"))
                        .course(new Course.CourseBuilder().name("TPSI").lecturer("Tomasz Pawlak").build())
                        .build()
        );
        addStudent(
                new Student.StudentBuilder()
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
                        .course(new Course.CourseBuilder().name("TPAL").lecturer("Adam Kotarski").build())
                        .build()
        );
        grades.add(
                new Grade.GradeBuilder().id()
                        .value((float) 4.5)
                        .date(new Date("2018/04/01"))
                        .course(new Course.CourseBuilder().name("ABCD").lecturer("Jett Mall").build())
                        .build()
        );

        addStudent(
                new Student.StudentBuilder()
                        .birthday(new Date("1994/02/13"))
                        .firstName("Diane")
                        .lastName("Pittsburg")
                        .grades(grades)
                        .build()
        );

        grades = new ArrayList<>();

        grades.add(
                new Grade.GradeBuilder().id()
                        .value((float) 4)
                        .date(new Date("2018/04/04"))
                        .course(new Course.CourseBuilder().name("TWO").lecturer("Andrzej Zarcha").build())
                        .build()
        );
        grades.add(
                new Grade.GradeBuilder().id()
                        .value((float) 4.5)
                        .date(new Date("2018/04/05"))
                        .course(new Course.CourseBuilder().name("English").lecturer("Olivia Bolton").build())
                        .build()
        );

        addStudent(
                new Student.StudentBuilder()
                        .birthday(new Date("1992/11/30"))
                        .firstName("Matthew")
                        .lastName("Pettigrew")
                        .grades(grades)
                        .build()
        );
    }
}
