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

@Path("/")
public class GradebookService {

    private final List<Student> studentsList = MockModel.getInstance();

    @GET
    @Path("/students")
    @Produces({MediaType.APPLICATION_XML})
    public List<Student> getStudentList() {

        return studentsList;
    }

    @GET
    @Path("/students/{index}")
    @Produces({MediaType.APPLICATION_XML})
    public Student getStudent(@PathParam("index") long index) {
        for (Student student : studentsList) {
            if (student.getIndex() == index)
                return student;
        }
        return null;
    }

    @GET
    @Path("/students/{index}/grades")
    @Produces({MediaType.APPLICATION_XML})
    public List<Grade> getStudentGrades(@PathParam("index") long index) {
        for (Student student : studentsList) {
            if (student.getIndex() == index)
                return student.getGrades();
        }
        return null;
    }

    @GET
    @Path("/students/{index}/grades/{id}")
    @Produces({MediaType.APPLICATION_XML})
    public Grade getStudentGrade(@PathParam("index") long index, @PathParam("id") long id) {
        for (Student student : studentsList) {
            if (student.getIndex() == index)
                for (Grade grade : student.getGrades())
                    if (grade.getId() == id)
                        return grade;

        }
        return null;
    }

    @GET
    @Path("/courses")
    public List<Course> getCoursesList() {

        return null;
    }

    @GET
    @Path("/courses/{id}")
    public Course getCourse(@PathParam("id") long id) {

        return null;
    }


}
