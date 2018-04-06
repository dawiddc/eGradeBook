package org.dawiddc.egradebook.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.concurrent.atomic.AtomicLong;

@XmlRootElement(name = "course")
@XmlType(propOrder = {"id", "name", "lecturer"})
public class Course {

    private static AtomicLong idCounter = new AtomicLong();
    private long id;
    private String name;
    private String lecturer;

    private Course(CourseBuilder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.lecturer = builder.lecturer;
    }

    public Course() {
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    @XmlElement
    public String getLecturer() {
        return lecturer;
    }

    public static class CourseBuilder {
        private long id;
        private String name;
        private String lecturer;

        public CourseBuilder id() {
            this.id = Course.idCounter.getAndIncrement();
            return this;
        }

        public CourseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder lecturer(String lecturer) {
            this.lecturer = lecturer;
            return this;
        }

        public Course build() {
            return new Course(this);
        }

    }

}
