package org.dawiddc.egradebook.dao.interfaces;

import org.dawiddc.egradebook.model.Course;

import java.util.List;

public interface ICourseDao {
    List<Course> getCourses(String name, String teacher);

    Course getCourse(String id);

    boolean updateCourse(Course course, String id);

    boolean deleteCourse(String id);

    Course addCourse(Course course);
}
