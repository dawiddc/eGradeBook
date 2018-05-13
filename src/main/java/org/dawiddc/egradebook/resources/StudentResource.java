package org.dawiddc.egradebook.resources;

import org.dawiddc.egradebook.dbservice.GradebookDataService;
import org.dawiddc.egradebook.dbservice.StudentDBService;
import org.dawiddc.egradebook.exception.JsonError;
import org.dawiddc.egradebook.exception.NotFoundException;
import org.dawiddc.egradebook.model.Student;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@PermitAll
@Path("students")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class StudentResource {

    @GET
    public List<Student> getStudentList() {
        List<Student> students = StudentDBService.getAllStudents();
        if (!(students == null || students.size() == 0))
            return students;
        throw new NotFoundException(new JsonError("Error", "Student list is empty"));
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
        StudentDBService.addStudent(student);

        String stringUri = "www.localhost:8080/students/" + student.getIndex();
        URI url = null;
        try {
            url = new URI(stringUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return Response.created(url).build();
    }

    @PUT
    @Path("/{index}")
    @RolesAllowed("lecturer")
    public Response updateStudent(@PathParam("index") long index, @NotNull Student newStudent) {
        Student oldStudent = StudentDBService.getStudent(index);
        if (oldStudent != null) {
            oldStudent.setGrades(newStudent.getGrades());
            oldStudent.setBirthday(newStudent.getBirthday());
            oldStudent.setFirstName(newStudent.getFirstName());
            oldStudent.setLastName(newStudent.getLastName());
            StudentDBService.updateStudent(oldStudent);

            return Response.status(Response.Status.NO_CONTENT).build();
        }
        newStudent.setIndex(index);
        StudentDBService.addStudent(newStudent);
        String stringUri = "www.localhost:8080/students/" + newStudent.getIndex();
        URI url = null;
        try {
            url = new URI(stringUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.created(url).build();
    }

    @DELETE
    @Path("/{index}")
    @RolesAllowed("lecturer")
    public Response deleteStudent(@PathParam("index") long index) {
        StudentDBService.deleteStudent(index);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
