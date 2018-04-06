package org.dawiddc.egradebook.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@XmlRootElement(name = "student")
@XmlType(propOrder = {"index", "firstName", "lastName", "birthday", "grades"})
public class Student {

    private static AtomicLong idCounter = new AtomicLong();
    private long index;
    private String firstName;
    private String lastName;
    private Date birthday;
    private List<Grade> grades;

    private Student(StudentBuilder builder) {
        this.index = builder.index;
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthday = builder.birthday;
        this.grades = builder.grades;
    }

    public Student() {
    }

    @XmlAttribute
    public long getIndex() {
        return index;
    }

    @XmlElement
    public String getFirstName() {
        return firstName;
    }

    @XmlElement
    public String getLastName() {
        return lastName;
    }

    @XmlElement
    public Date getBirthday() {
        return birthday;
    }

    @XmlElement
    public List<Grade> getGrades() {
        return grades;
    }

    public static class StudentBuilder {
        private long index;
        private String firstName;
        private String lastName;
        private Date birthday;
        private List<Grade> grades;

        public StudentBuilder index() {
            this.index = Student.idCounter.getAndIncrement();
            return this;
        }

        public StudentBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder birthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }

        public StudentBuilder grades(List<Grade> grades) {
            this.grades = grades;
            return this;
        }

        public Student build() {
            return new Student(this);
        }

    }

}
