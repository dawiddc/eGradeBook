package org.dawiddc.egradebook.dbservice;

import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.model.Student;
import org.dawiddc.egradebook.utils.MorphiaDatastore;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CourseDBService {
    private static final Datastore datastore = MorphiaDatastore.getDatastore();

    private CourseDBService() {
        throw new IllegalStateException("Utility class");
    }

    public static List<Course> getCourses(String lecturer, String name) {
        Query<Course> query = datastore.find(Course.class);

        if (lecturer != null) {
            query = query.field("lecturer").containsIgnoreCase(lecturer);
        }

        if (name != null) {
            query = query.field("name").containsIgnoreCase(name);
        }

        return query.asList();
    }

    public static Course getCourseById(long id) {
        Query<Course> query = datastore.find(Course.class).field("id").equal(id);
        return query.get();
    }

    public static Course getCourseByName(String name) {
        Query<Course> query = datastore.find(Course.class).field("name").equal(name);
        return query.get();
    }

    public static void addCourse(Course course) {
        datastore.save(course);
    }

    public static void updateCourse(Course course) {
        Query<Course> query = datastore.createQuery(Course.class).field("id").equal(course.getId());
        UpdateOperations<Course> updateOperations = datastore.createUpdateOperations(Course.class)
                .set("name", course.getName())
                .set("lecturer", course.getLecturer());
        datastore.update(query, updateOperations);
    }

    public static void deleteCourse(long id) {
        Course course = getCourseById(id);
        for (Student student : StudentDBService.getAllStudents(null, null, null, null)) {
            if (student.getGrades() == null) {
                student.setGrades(new ArrayList<>());
            }
            student.setGrades(student.getGrades().stream().filter(grade -> grade.getCourse().getId() != id).collect(Collectors.toList()));
            StudentDBService.updateStudent(student);
        }
        if (course != null) {
            datastore.delete(course);
        }
    }


}
