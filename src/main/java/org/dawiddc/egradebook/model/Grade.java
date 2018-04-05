package org.dawiddc.egradebook.model;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Grade {

    private String id;
    private float value;
    private Date date;
    private Course Course;
    private static AtomicLong idCounter = new AtomicLong();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = (float) (0.5 * Math.round(value * 2));
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Course getCourse() {
        return Course;
    }

    public void setCourse(Course course) {
        Course = course;
    }

    public Grade() {
        this.setId(String.valueOf(idCounter.getAndIncrement()));
    }

}
