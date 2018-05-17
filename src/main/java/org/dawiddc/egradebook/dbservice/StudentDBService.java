package org.dawiddc.egradebook.dbservice;

import org.dawiddc.egradebook.model.Grade;
import org.dawiddc.egradebook.model.Student;
import org.dawiddc.egradebook.utils.MorphiaDatastore;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class StudentDBService {
    private static Datastore datastore = MorphiaDatastore.getDatastore();

    /**
     * Getting all students from database
     *
     * @return list of students from database
     */
    public static List<Student> getAllStudents(String firstName, String lastName, Date birthday, String dateRelation) {

        Query<Student> query = datastore.createQuery(Student.class);

        if (firstName != null) {
            query = query.field("firstName").containsIgnoreCase(firstName);
        }
        if (lastName != null) {
            query = query.field("lastName").containsIgnoreCase(lastName);
        }
        if (birthday != null && dateRelation != null) {
            switch (dateRelation.toLowerCase()) {
                case "equal":
                    query.filter("birthday ==", birthday);
                    break;
                case "after":
                    query.filter("birthday >", birthday);
                    break;
                case "before":
                    query.filter("birthday <", birthday);
                    break;
            }
        }

        return query.asList();
    }

    public static Student getStudent(long index) {
        return datastore.createQuery(Student.class).field("index").equal(index).get();
    }

    public static List<Grade> getStudentGrades(long index) {
        return datastore.createQuery(Student.class).field("index").equal(index).get().getGrades();
    }

    public static Grade getStudentGrade(long index, long id) {
        List<Grade> grades = datastore.find(Student.class).field("index").equal(index).get().getGrades();
        Optional<Grade> matchingGrade = grades.stream().filter(g -> g.getId() == id).findFirst();
        return matchingGrade.orElse(null);
    }

    public static void addStudent(Student student) {
        datastore.save(student);
    }

    public static void updateStudent(Student student) {
        Query<Student> query = datastore.createQuery(Student.class).field("index").equal(student.getIndex());
        UpdateOperations<Student> updateOperations = datastore.createUpdateOperations(Student.class)
                .set("firstName", student.getFirstName())
                .set("lastName", student.getLastName())
                .set("birthday", student.getBirthday())
                .set("grades", student.getGrades());
        datastore.update(query, updateOperations);
    }

    public static void deleteStudent(long index) {
        Student student = getStudent(index);
        if (student != null) {
            datastore.delete(student);
        }
    }

    public static void deleteGrade(long index, long id) {
        Student student = getStudent(index);
        if (student != null) {
            student.setGrades(student.getGrades().stream().filter(grade -> grade.getId() != id).collect(Collectors.toList()));
            updateStudent(student);
        }
    }

}
