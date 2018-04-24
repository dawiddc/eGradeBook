package org.dawiddc.egradebook.dao;

import org.dawiddc.egradebook.dao.interfaces.IStudentDao;
import org.dawiddc.egradebook.model.Student;

import java.util.Date;
import java.util.List;

public class StudentDaoImpl implements IStudentDao {

//    private Datastore datastore = Context.getInstance().getDatastore();

    @Override
    public List<Student> getStudents(int index, String firstName, String lastName, Date birthday, int birthdayCompareType) {
        return null;
    }

    @Override
    public Student getStudent(int index) {
        return null;
    }

    @Override
    public boolean updateStudent(Student student, int index) {
        return false;
    }

    @Override
    public boolean deleteStudent(int index) {
        return false;
    }

    @Override
    public Student addStudent(Student student) {
        return null;
    }
}
