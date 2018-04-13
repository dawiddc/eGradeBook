package org.dawiddc.egradebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@XmlRootElement(name = "grade")
@XmlType(propOrder = {"id", "course", "value", "date", "links"})
public class Grade {

    private long id;
    private float value;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date date;
    private Course course;
    @InjectLinks({
            @InjectLink(resource = Grade.class, rel = "self")
    })
    private List<Link> links;

    private Grade(GradeBuilder builder) {
        this.value = builder.value;
        this.date = builder.date;
        this.course = builder.course;
    }

    public Grade() {
    }

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> getLinks() {
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
    private float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    @XmlElement
    private Date getDate() {
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
