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

@XmlRootElement(name = "student")
@XmlType(propOrder = {"index", "firstName", "lastName", "birthday", "grades", "links"})
public class Student {

    private long index;
    private String firstName;
    private String lastName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "CET")
    private Date birthday;
    private List<Grade> grades;
    @InjectLinks({
            @InjectLink(resource = Student.class, rel = "self")
    })
    private List<Link> links;

    private Student(StudentBuilder builder) {
        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.birthday = builder.birthday;
        this.grades = builder.grades;
    }

    public Student() {
    }

    @XmlElement(name = "link")
    @XmlElementWrapper(name = "links")
    @JsonProperty("links")
    @XmlJavaTypeAdapter(Link.JaxbAdapter.class)
    private List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
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
