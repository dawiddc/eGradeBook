package org.dawiddc.egradebook.model;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class Course {

    private String id;
    private String name;
    private String lecturer;
    private static AtomicLong idCounter = new AtomicLong();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public Course() {
        this.setId(String.valueOf(idCounter.getAndIncrement()));
    }

}
