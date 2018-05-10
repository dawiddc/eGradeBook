package org.dawiddc.egradebook.dbservice;

import org.dawiddc.egradebook.model.Student;
import org.dawiddc.egradebook.utils.MorphiaDatastore;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;

import java.util.List;

public class StudentDBService {
    private static Datastore datastore = MorphiaDatastore.getDatastore();

    /**
     * Getting all students from database
     *
     * @return list of students from database
     */
    public static List<Student> getAllStudents() {
        Query<Student> query = datastore.createQuery(Student.class);
        List<Student> students = query.asList();
        return students;
    }

    public static Student getStudent(long index) {
        return datastore.createQuery(Student.class).field("index").equal(index).get();
    }

    // TODO: Add id generating
    public static long addStudent(Student student) {
        IdGeneratorService generator = new IdGeneratorService();
        long newIndex = generator.generateStudentIndex();
        student.setIndex(newIndex);

        datastore.save(student);
        return newIndex;
    }


}
