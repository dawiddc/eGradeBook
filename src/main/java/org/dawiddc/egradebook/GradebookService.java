package org.dawiddc.egradebook;

import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.model.Grade;
import org.dawiddc.egradebook.model.MockModel;
import org.dawiddc.egradebook.model.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Path("/")
public class GradebookService {

    private final List<Student> studentsList = MockModel.getInstance();

    @GET
    @Path("/students")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Student> getStudentList() {

        return studentsList;
    }

    @GET
    @Path("/students/{index}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Student getStudent(@PathParam("index") long index) {
        for (Student student : studentsList) {
            if (student.getIndex() == index)
                return student;
        }
        return null;
    }

    @GET
    @Path("/students/{index}/grades")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Grade> getStudentGrades(@PathParam("index") long index) {
        for (Student student : studentsList) {
            if (student.getIndex() == index)
                return student.getGrades();
        }
        return null;
    }

    @GET
    @Path("/students/{index}/grades/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
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
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Course> getCoursesList() {

        return null;
    }

    @GET
    @Path("/courses/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Course getCourse(@PathParam("id") long id) {

        return null;
    }

    @POST
    @Path("/students")
    public Response postStudent(Student student) {
        studentsList.add(student);

        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/courses")
    public Response postCourse(Course course) {
        String output = "POST: ";

        return Response.status(Response.Status.CREATED).entity(output).build();
    }

    @POST
    @Path("/students/{index}/grades")
    public Response postGrade(Grade grade) {
        String output = "POST: ";

        return Response.status(Response.Status.CREATED).entity(output).build();
    }

    @PUT
    @Path("/students/{index}")
    public Response updateStudent(@PathParam("index") long index, Student student) {
        //TODO put should preserve previous index
        int matchIndex = 0;
        Optional<Student> match = studentsList.stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            matchIndex = studentsList.indexOf(match.get());
            studentsList.set(matchIndex, student);
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    @PUT
    @Path("/courses/{id}")
    public Response updateCourse(Course course) {
        return null;
    }

    @PUT
    @Path("/students/{index}/grades/{id}")
    public Response updateGrade(Grade grade) {
        return null;

    }

    @DELETE
    @Path("/students/{index}")
    public Response deleteStudent(@PathParam("index") long index) {
        Predicate<Student> student = s -> s.getIndex() == index;
        if (studentsList.removeIf(student)) {
            return Response.status(Response.Status.OK).build();
        } else {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
    }

    @DELETE
    @Path("/courses/{id}")
    public Response deleteCourse(@PathParam("id") long id) {
        return null;
    }

    @DELETE
    @Path("/students/{index}/grades/{id}")
    public Response deleteGrade(@PathParam("id") long id) {
        return null;
    }

}
