package org.dawiddc.egradebook.resources;

import org.dawiddc.egradebook.dbservice.StudentDBService;
import org.dawiddc.egradebook.exception.JsonError;
import org.dawiddc.egradebook.exception.NotFoundException;
import org.dawiddc.egradebook.model.GradebookDataService;
import org.dawiddc.egradebook.model.Student;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@PermitAll
@Path("students")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class StudentResource {

    private final GradebookDataService dataService = GradebookDataService.getInstance();
    private final List<Student> studentsList = dataService.getStudentsList();

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
        String createdIndex = String.valueOf(StudentDBService.addStudent(student));
        String stringUri = "www.localhost:8080/students/" + createdIndex;
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
    public Response updateStudent(@PathParam("index") long index, @NotNull Student student) {
        int matchIndex;
        Optional<Student> match = studentsList.stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            matchIndex = studentsList.indexOf(match.get());
            student.setIndex(index);
            studentsList.set(matchIndex, student);
            studentsList.sort(Comparator.comparingLong(Student::getIndex));
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            student.setIndex(index);
            studentsList.add(student);
            studentsList.sort(Comparator.comparingLong(Student::getIndex));
            return Response.status(Response.Status.CREATED).build();
        }
    }

    @DELETE
    @Path("/{index}")
    @RolesAllowed("lecturer")
    public Response deleteStudent(@PathParam("index") long index) {
        Predicate<Student> student = s -> s.getIndex() == index;
        studentsList.removeIf(student);

        return Response.status(Response.Status.NO_CONTENT).build();
    }


}
