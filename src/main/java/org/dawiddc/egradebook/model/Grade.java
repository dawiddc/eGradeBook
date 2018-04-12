package org.dawiddc.egradebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;

@XmlRootElement(name = "grade")
@XmlType(propOrder = {"id", "course", "value", "date"})
public class Grade {

    private long id;
    private float value;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date date;
    private Course course;

    private Grade(GradeBuilder builder) {
        this.value = builder.value;
        this.date = builder.date;
        this.course = builder.course;
    }

    public Grade() {
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement
    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @XmlElement
    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @XmlElement
    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        GradebookDataService.getInstance().addCourse(course);
        this.course = course;
    }

    public static class GradeBuilder {
        private float value;
        private Date date;
        private Course course;

        public GradeBuilder value(float value) {
            this.value = value;
            return this;
        }

        public GradeBuilder date(Date date) {
            this.date = date;
            return this;
        }

        public GradeBuilder course(Course course) {
            this.course = course;
            GradebookDataService.getInstance().addCourse(course);
            return this;
        }

        public Grade build() {
            return new Grade(this);
        }

    }


}
