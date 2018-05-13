package org.dawiddc.egradebook.dbservice;

import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.utils.MorphiaDatastore;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.List;

public class CourseDBService {
    private static Datastore datastore = MorphiaDatastore.getDatastore();

    public static List<Course> getCourses() {
        Query<Course> query = datastore.find(Course.class);
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
        Query<Course> query = datastore.find(Course.class).field("id").equal(course.getId());
        UpdateOperations<Course> updateOperations = datastore.createUpdateOperations(Course.class)
                .set("name", course.getName())
                .set("lecturer", course.getLecturer());
        datastore.findAndModify(query, updateOperations);
    }

    public static void deleteCourse(long id) {
        Course course = getCourseById(id);
        if (course != null) {
            datastore.delete(course);
        }
    }


}
