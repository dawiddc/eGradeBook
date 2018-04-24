package org.dawiddc.egradebook.dao.interfaces;

import org.dawiddc.egradebook.model.Student;

import java.util.Date;
import java.util.List;

public interface IStudentDao {
    List<Student> getStudents(int index, String firstName, String lastName, Date birthday, int birthdayCompareType);

    Student getStudent(int index);

    boolean updateStudent(Student student, int index);

    boolean deleteStudent(int index);

    Student addStudent(Student student);
}
