package org.dawiddc.egradebook.dbservice;

import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.model.Grade;
import org.dawiddc.egradebook.model.IdGenerator;
import org.dawiddc.egradebook.model.Student;
import org.dawiddc.egradebook.utils.MorphiaDatastore;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GradebookDataService {

    private static final GradebookDataService ourInstance = new GradebookDataService();
    private final List<Student> studentsList = new ArrayList<>();
    private final List<Course> coursesList = new ArrayList<>();
    private static List<Grade> grades = new ArrayList<>();

    private static Datastore datastore = MorphiaDatastore.getDatastore();

    public static GradebookDataService getInstance() {
        return ourInstance;
    }

    private static long getStudentIndex() {
        Query<IdGenerator> query = datastore.find(IdGenerator.class);
        long index = query.get().getStudentIndex() + 1;
        UpdateOperations<IdGenerator> updateOperations = datastore.createUpdateOperations(IdGenerator.class).set("studentIndex", index);
        datastore.findAndModify(query, updateOperations);

        return index;
    }

    private static int getCourseId() {
        Query<IdGenerator> query = datastore.find(IdGenerator.class);
        int id = query.get().getCourseId() + 1;
        UpdateOperations<IdGenerator> updateOperations = datastore.createUpdateOperations(IdGenerator.class).set("courseId", id);
        datastore.findAndModify(query, updateOperations);

        return id;
    }

    public static int getGradeId() {
        Query<IdGenerator> query = datastore.find(IdGenerator.class);
        int id = query.get().getGradeId() + 1;
        UpdateOperations<IdGenerator> updateOperations = datastore.createUpdateOperations(IdGenerator.class).set("gradeId", id);
        datastore.findAndModify(query, updateOperations);

        return id;
    }

    public static long getFirstAvailableStudentIndex() {
        boolean isFound = true;
        long index = 0;
        while (isFound) {
            isFound = false;
            index = getStudentIndex();
            if (StudentDBService.getStudent(index) != null)
                isFound = true;
        }
        return index;
    }

    public static int getFirstAvailableCourseId() {
        boolean isFound = true;
        int id = 0;
        while (isFound) {
            isFound = false;
            id = getCourseId();
            if (CourseDBService.getCourseById(id) != null)
                isFound = true;
        }
        return id;
    }

    public static int getFirstAvailableGradeId() {
        return getGradeId();
    }

    public void createMockModel() {

        if (!(datastore.getCount(IdGenerator.class) > 0)) {
            IdGenerator idGenerator = new IdGenerator();
            idGenerator.setStudentIndex(0);
            idGenerator.setCourseId(0);
            idGenerator.setGradeId(0);
            datastore.save(idGenerator);
        }

        if (datastore.getCount(Student.class) < 1) {
            coursesList.add(new Course.CourseBuilder().name("TPSI").lecturer("Tomasz Pawlak").id(getCourseId()).build());
            coursesList.add(new Course.CourseBuilder().name("Math").lecturer("Matt Jepard").id(getCourseId()).build());
            coursesList.add(new Course.CourseBuilder().name("TPAL").lecturer("Adam Kotarski").id(getCourseId()).build());
            datastore.save(coursesList);

            /* Student 1 */
            grades.add(new Grade.GradeBuilder()
                    .value(5)
                    .date(new Date("2018/04/04"))
                    .course(CourseDBService.getCourseByName("TPSI"))
                    .id(getGradeId())
                    .studentOwnerIndex(1)
                    .build());
            grades.add(new Grade.GradeBuilder()
                    .value(4)
                    .date(new Date("2018/04/06"))
                    .course(CourseDBService.getCourseByName("Math"))
                    .id(getGradeId())
                    .studentOwnerIndex(1)
                    .build());
            studentsList.add(
                    new Student.StudentBuilder()
                            .birthday(new Date("1995/07/23"))
                            .firstName("John")
                            .lastName("Doe")
                            .grades(grades)
                            .index(getStudentIndex())
                            .build()
            );

            /* Student 2 */
            grades = new ArrayList<>();
            grades.add(new Grade.GradeBuilder()
                    .value((float) 3.5)
                    .date(new Date("2018/04/02"))
                    .course(CourseDBService.getCourseByName("TPSI"))
                    .id(getGradeId())
                    .studentOwnerIndex(2)
                    .build());
            grades.add(new Grade.GradeBuilder()
                    .value((float) 4.5)
                    .date(new Date("2018/04/01"))
                    .course(new Course.CourseBuilder().name("ABCD").lecturer("Jett Mall").id(getCourseId()).build())
                    .id(getGradeId())
                    .studentOwnerIndex(2)
                    .build());
            studentsList.add(
                    new Student.StudentBuilder()
                            .birthday(new Date("1994/02/13"))
                            .firstName("Diane")
                            .lastName("Pittsburg")
                            .grades(grades)
                            .index(getStudentIndex())
                            .build()
            );

            /* Student 3 */
            grades = new ArrayList<>();
            grades.add(new Grade.GradeBuilder()
                    .value((float) 4)
                    .date(new Date("2018/04/04"))
                    .course(new Course.CourseBuilder().name("TWO").lecturer("Andrzej Zarcha").id(getCourseId()).build())
                    .id(getGradeId())
                    .studentOwnerIndex(3)
                    .build());
            grades.add(new Grade.GradeBuilder()
                    .value((float) 4.5)
                    .date(new Date("2018/04/05"))
                    .course(new Course.CourseBuilder().name("English").lecturer("Olivia Bolton").id(getCourseId()).build())
                    .id(getGradeId())
                    .studentOwnerIndex(3)
                    .build());
            studentsList.add(
                    new Student.StudentBuilder()
                            .birthday(new Date("1992/11/30"))
                            .firstName("Matthew")
                            .lastName("Pettigrew")
                            .grades(grades)
                            .index(getStudentIndex())
                            .build()
            );

            datastore.save(studentsList);
        }
    }

}
