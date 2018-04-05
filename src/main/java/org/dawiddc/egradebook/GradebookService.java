package org.dawiddc.egradebook;

import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.model.Grade;
import org.dawiddc.egradebook.model.MockModel;
import org.dawiddc.egradebook.model.Student;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.UUID;

@Path("/")
@Produces({MediaType.APPLICATION_XML})
public class GradebookService {

    private final List<Student> studentsList = MockModel.getInstance();

    @GET
    @Path("/students")
    public List<Student> getStudentList() {

        return studentsList;
    }

    @GET
    @Path("/students/{index}")
    public Student getStudent(@PathParam("index") int index) {

        return null;
    }

    @GET
    @Path("/students/{index}/grades")
    public List<Grade> getStudentGrades() {

        return null;
    }

    @GET
    @Path("/students/{index}/grades/{id}")
    public Grade getStudentGrade(@PathParam("id") UUID id) {

        return null;
    }

    @GET
    @Path("/courses")
    public List<Course> getCoursesList() {

        return null;
    }

    @GET
    @Path("/courses/{id}")
    public Course getCourse(@PathParam("id") UUID id) {

        return null;
    }


 }
