package org.dawiddc.egradebook.model;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Student {

    private String index;
    private String firstName;
    private String lastName;
    private Date birthday;
    private List<Grade> grades;
    private static AtomicLong idCounter = new AtomicLong();

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) { this.index = index; }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public Student() {
        this.setIndex(String.valueOf(idCounter.getAndIncrement()));
    }

}
