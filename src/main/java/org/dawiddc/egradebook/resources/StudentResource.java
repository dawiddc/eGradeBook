package org.dawiddc.egradebook.resources;

import jersey.repackaged.com.google.common.collect.Lists;
import org.dawiddc.egradebook.dbservice.CourseDBService;
import org.dawiddc.egradebook.dbservice.GradebookDataService;
import org.dawiddc.egradebook.dbservice.StudentDBService;
import org.dawiddc.egradebook.exception.JsonError;
import org.dawiddc.egradebook.exception.NotFoundException;
import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.model.Grade;
import org.dawiddc.egradebook.model.Student;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@PermitAll
@Path("students")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class StudentResource {

    private static final Logger LOGGER = Logger.getLogger(StudentResource.class.getName());


    @GET
    public Response getStudentList(@QueryParam("firstName") String firstName,
                                   @QueryParam("lastName") String lastName,
                                   @QueryParam("birthday") Date birthday,
                                   @QueryParam("dateRelation") String dateRelation) {
        List<Student> students = StudentDBService.getAllStudents(firstName, lastName, birthday, dateRelation);

        if ((students == null || students.isEmpty())) {
            throw new NotFoundException(new JsonError("Error", "Student list is empty"));
        }

        GenericEntity<List<Student>> entity = new GenericEntity<List<Student>>(Lists.newArrayList(students)) {
        };

        return Response.status(Response.Status.OK).entity(entity).build();
    }

    @GET
    @Path("/{index}")
    public Student getStudent(@PathParam("index") long index) {
        Student student = StudentDBService.getStudent(index);
        if (student != null) {
            return student;
        }
        throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
    }

    @RolesAllowed("lecturer")
    @POST
    public Response postStudent(@NotNull Student student) {
        student.setIndex(GradebookDataService.getFirstAvailableStudentIndex());
        if (student.getGrades() == null) {
            student.setGrades(new ArrayList<>());
        }
        assignCourses(student);
        StudentDBService.addStudent(student);

        String stringUri = "www.localhost:8080/students/" + student.getIndex();
        URI url = null;
        try {
            url = new URI(stringUri);
        } catch (URISyntaxException e) {
            LOGGER.log(Level.SEVERE, "URI cast Exception", e);
        }

        return Response.created(url).entity(student).build();
    }

    private void assignCourses(@NotNull Student student) {
        Course searchedCourse;
        for (Grade grade : student.getGrades()) {
            searchedCourse = CourseDBService.getCourseById(grade.getCourse().getId());
            if (searchedCourse == null) {
                throw new NotFoundException(new JsonError("Error", "Course " + grade.getCourse().getId() + " not found"));
            }
            grade.setCourse(searchedCourse);
        }
    }

    @PUT
    @Path("/{index}")
    @RolesAllowed("lecturer")
    public Response updateStudent(@PathParam("index") long index, @NotNull Student newStudent) {
        Student oldStudent = StudentDBService.getStudent(index);

        if (newStudent.getGrades() == null) {
            newStudent.setGrades(new ArrayList<>());
        }
        assignCourses(newStudent);

        if (oldStudent != null) {
            oldStudent.setGrades(newStudent.getGrades());
            oldStudent.setBirthday(newStudent.getBirthday());
            oldStudent.setFirstName(newStudent.getFirstName());
            oldStudent.setLastName(newStudent.getLastName());
            StudentDBService.updateStudent(oldStudent);

            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            newStudent.setIndex(index);
            StudentDBService.addStudent(newStudent);

            String stringUri = "www.localhost:8080/students/" + newStudent.getIndex();
            URI url = null;
            try {
                url = new URI(stringUri);
            } catch (URISyntaxException e) {
                LOGGER.log(Level.SEVERE, "URI cast Exception", e);
            }
            return Response.created(url).entity(newStudent).build();
        }
    }

    @DELETE
    @Path("/{index}")
    @RolesAllowed("lecturer")
    public Response deleteStudent(@PathParam("index") long index) {
        StudentDBService.deleteStudent(index);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
