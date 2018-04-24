package org.dawiddc.egradebook.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.bson.types.ObjectId;
import org.dawiddc.egradebook.utils.ObjectIdJaxbAdapter;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;

import javax.validation.constraints.NotNull;
import javax.ws.rs.core.Link;
import javax.xml.bind.annotation.*;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;
import java.util.List;

@Entity("students")
@XmlRootElement(name = "student")
@XmlType(propOrder = {"index", "firstName", "lastName", "birthday", "grades", "links"})
public class Student {

    @Indexed(name = "index", unique = true)
    private long index;
    @NotNull
    private String firstName;
    @NotNull

    private String lastName;
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date birthday;
    private List<Grade> grades;
    @InjectLinks({
            @InjectLink(value = "/students/{index}", rel = "self"),
            @InjectLink(value = "/students", rel = "parent"),
            @InjectLink(value = "/students/{index}/grades", rel = "grades")
    })
    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> links;
    @Id
    @XmlJavaTypeAdapter(ObjectIdJaxbAdapter.class)
    private ObjectId id;

    private Student(StudentBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthday = builder.birthday;
        this.grades = builder.grades;
    }

    public Student() { }

    @XmlTransient
    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    @XmlAttribute
    public long getIndex() {
        return index;
    }

    public void setIndex(long index) {
        this.index = index;
    }

    @XmlElement
    private String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @XmlElement
    private String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @XmlElement
    private Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    @XmlElement(name = "grade")
    @XmlElementWrapper(name = "grades")
    @JsonProperty("grades")
    public List<Grade> getGrades() {
        return grades;
    }

    public void setGrades(List<Grade> grades) {
        this.grades = grades;
    }

    public static class StudentBuilder {
        private String firstName;
        private String lastName;
        private Date birthday;

        private List<Grade> grades;

        public StudentBuilder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public StudentBuilder lastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public StudentBuilder birthday(Date birthday) {
            this.birthday = birthday;
            return this;
        }

        public StudentBuilder grades(List<Grade> grades) {
            this.grades = grades;
            return this;
        }

        public Student build() {
            return new Student(this);
        }

    }

}
