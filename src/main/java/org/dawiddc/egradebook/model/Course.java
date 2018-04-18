package org.dawiddc.egradebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@SuppressWarnings("unused")
@XmlRootElement(name = "course")
@XmlType(propOrder = {"id", "name", "lecturer", "links"})
public class Course {

    private long id;
    private String name;
    private String lecturer;
    @InjectLinks({
            @InjectLink(value = "/courses/{id}", rel = "self"),
            @InjectLink(value = "/courses", rel = "parent")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;

    private Course(CourseBuilder builder) {
        this.name = builder.name;
        this.lecturer = builder.lecturer;
    }


    public Course() {
    }

    @XmlAttribute
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @XmlElement
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlElement
    private String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public static class CourseBuilder {
        private String name;
        private String lecturer;

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
