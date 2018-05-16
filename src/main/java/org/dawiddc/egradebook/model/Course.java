package org.dawiddc.egradebook.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;

import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.List;

@Entity("courses")
@XmlRootElement(name = "course")
public class Course {
    @InjectLinks({
            @InjectLink(value = "/courses/{id}", rel = "self"),
            @InjectLink(value = "/courses", rel = "parent")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    List<Link> links;

    @Id
    @XmlTransient
//    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private ObjectId objectId;

    private long id;
    private String name;
    private String lecturer;

    private Course(CourseBuilder builder) {
        this.name = builder.name;
        this.lecturer = builder.lecturer;
        this.id = builder.id;
    }

    public Course() {
    }

    @XmlTransient
    public ObjectId getObjectId() {
        return objectId;
    }

    public void setObjectId(ObjectId objectId) {
        this.objectId = objectId;
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
    public String getLecturer() {
        return lecturer;
    }

    public void setLecturer(String lecturer) {
        this.lecturer = lecturer;
    }

    public static class CourseBuilder {
        private String name;
        private String lecturer;
        private long id;

        public CourseBuilder name(String name) {
            this.name = name;
            return this;
        }

        public CourseBuilder lecturer(String lecturer) {
            this.lecturer = lecturer;
            return this;
        }

        public CourseBuilder id(long id) {
            this.id = id;
            return this;
        }

        public Course build() {
            return new Course(this);
        }

    }

}
