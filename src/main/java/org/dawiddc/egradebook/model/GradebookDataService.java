package org.dawiddc.egradebook.model;

import org.dawiddc.egradebook.utils.DatastoreHandler;
import org.mongodb.morphia.Datastore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class GradebookDataService {

    private static final AtomicLong gradeIdCounter = new AtomicLong();
    private static final GradebookDataService ourInstance = new GradebookDataService();
    private static final AtomicLong studentIdCounter = new AtomicLong();
    private static final AtomicLong courseIdCounter = new AtomicLong();
    private final List<Student> studentsList = new ArrayList<>();
    private final List<Course> coursesList = new ArrayList<>();
    private Datastore datastore = DatastoreHandler.getDatastore();

    public static GradebookDataService getInstance() {
        return ourInstance;
    }

    public long addStudent(Student student) {
        long index = studentIdCounter.getAndIncrement();
        for (Student existingStudent : studentsList) {
            if (existingStudent.getIndex() == index) {
                return addStudent(student);
            }
        }
        student.setIndex(index);
        for (Grade grade : student.getGrades()) {
            long id = gradeIdCounter.getAndIncrement();
            grade.setId(id);
            boolean alreadyExists = false;
            for (Course existingCourse : coursesList) {
                if (existingCourse.getName().equals(grade.getCourse().getName())) {
                    grade.getCourse().setId(existingCourse.getId());
                    alreadyExists = true;
                }
            }
            if (!alreadyExists)
                grade.getCourse().setId(courseIdCounter.getAndIncrement());
        }
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
                grade.setStudentOwnerIndex(index);
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
        if (datastore.getCount(studentsList) < 1) {
            /* Student 0 */
            addStudent(
                    new Student.StudentBuilder()
                            .birthday(new Date("1995/07/23"))
                            .firstName("John")
                            .lastName("Doe")
                            .grades(new ArrayList<>())
                            .build()
            );
            addGrade(0, new Grade.GradeBuilder()
                    .value(5)
                    .date(new Date("2018/04/04"))
                    .course(new Course.CourseBuilder().name("TPSI").lecturer("Tomasz Pawlak").build())
                    .build());
            addGrade(0, new Grade.GradeBuilder()
                    .value(4)
                    .date(new Date("2018/04/06"))
                    .course(new Course.CourseBuilder().name("Math").lecturer("Matt Jepard").build())
                    .build());
            /* Student 1 */
            addStudent(
                    new Student.StudentBuilder()
                            .birthday(new Date("1994/02/13"))
                            .firstName("Diane")
                            .lastName("Pittsburg")
                            .grades(new ArrayList<>())
                            .build()
            );
            addGrade(1, new Grade.GradeBuilder()
                    .value((float) 3.5)
                    .date(new Date("2018/04/02"))
                    .course(new Course.CourseBuilder().name("TPAL").lecturer("Adam Kotarski").build())
                    .build());
            addGrade(1, new Grade.GradeBuilder()
                    .value((float) 4.5)
                    .date(new Date("2018/04/01"))
                    .course(new Course.CourseBuilder().name("ABCD").lecturer("Jett Mall").build())
                    .build());
            /* Student 2 */
            addStudent(
                    new Student.StudentBuilder()
                            .birthday(new Date("1992/11/30"))
                            .firstName("Matthew")
                            .lastName("Pettigrew")
                            .grades(new ArrayList<>())
                            .build()
            );
            addGrade(2, new Grade.GradeBuilder()
                    .value((float) 4)
                    .date(new Date("2018/04/04"))
                    .course(new Course.CourseBuilder().name("TWO").lecturer("Andrzej Zarcha").build())
                    .build());
            addGrade(2, new Grade.GradeBuilder()
                    .value((float) 4.5)
                    .date(new Date("2018/04/05"))
                    .course(new Course.CourseBuilder().name("English").lecturer("Olivia Bolton").build())
                    .build());

            datastore.save(studentsList);
        }
    }


}
