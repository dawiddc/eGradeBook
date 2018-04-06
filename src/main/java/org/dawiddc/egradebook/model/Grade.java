package org.dawiddc.egradebook.model;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@XmlRootElement(name = "grade")
@XmlType(propOrder = {"id", "course", "value", "date"})
public class Grade {

    private static AtomicLong idCounter = new AtomicLong();
    private long id;
    private float value;
    private Date date;
    private Course course;

    private Grade(GradeBuilder builder) {
        this.id = builder.id;
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

    @XmlElement
    public float getValue() {
        return value;
    }

    @XmlElement
    public Date getDate() {
        return date;
    }

    @XmlElement
    public Course getCourse() {
        return course;
    }

    public static class GradeBuilder {
        private long id;
        private float value;
        private Date date;
        private Course course;

        public GradeBuilder id() {
            this.id = Grade.idCounter.getAndIncrement();
            return this;
        }

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
            return this;
        }

        public Grade build() {
            return new Grade(this);
        }

    }


}
