package org.dawiddc.egradebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Embedded;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@Embedded
@XmlRootElement(name = "grade")
@XmlType(propOrder = {"id", "course", "value", "date", "links"})
public class Grade {

    @InjectLinks({
            @InjectLink(value = "students/{studentOwnerIndex}/grades", rel = "parent"),
            @InjectLink(value = "students/{studentOwnerIndex}/grades/{id}", rel = "self")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;
    private long id;
    private float value;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date date;
    private Course course;
    private long studentOwnerIndex;

    private Grade(GradeBuilder builder) {
        this.value = builder.value;
        this.date = builder.date;
        this.course = builder.course;
        this.id = builder.id;
        this.studentOwnerIndex = builder.studentOwnerIndex;
    }

    public Grade() { }

    public List<Link> getLinks() {
        return links;
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
        this.course = course;
    }

    @XmlTransient
    public long getStudentOwnerIndex() {
        return studentOwnerIndex;
    }

    public void setStudentOwnerIndex(long studentOwnerIndex) {
        this.studentOwnerIndex = studentOwnerIndex;
    }

    public static class GradeBuilder {
        private float value;
        private Date date;
        private Course course;
        private long id;
        private long studentOwnerIndex;

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

        public GradeBuilder id(long id) {
            this.id = id;
            return this;
        }

        public GradeBuilder studentOwnerIndex(long studentOwnerIndex) {
            this.studentOwnerIndex = studentOwnerIndex;
            return this;
        }

        public Grade build() {
            return new Grade(this);
        }

    }


}
