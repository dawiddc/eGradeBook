package org.dawiddc.egradebook.resources;

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
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@PermitAll
@Path("students/{index}/grades")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class GradeResource {

    @GET
    @RolesAllowed("lecturer")
    public List<Grade> getStudentGrades(@PathParam("index") long index) {
        Student student = StudentDBService.getStudent(index);
        if (student == null)
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        List<Grade> gradesList = student.getGrades();
        return gradesList;
    }

    @GET
    @Path("/{id}")
    public Grade getStudentGrade(@PathParam("index") long index, @PathParam("id") long id) {
        Student student = StudentDBService.getStudent(index);
        if (student == null)
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        Optional<Grade> grade = student.getGrades().stream().filter(g -> g.getId() == id).findFirst();
        if (!grade.isPresent())
            throw new NotFoundException(new JsonError("Error", "Grade " + id + " not found"));
        return grade.get();
    }

    @POST
    @RolesAllowed("lecturer")
    public Response postGrade(@PathParam("index") long index, Grade grade) {
        Student student = StudentDBService.getStudent(index);
        if (student == null)
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        Course course = CourseDBService.getCourseById(grade.getCourse().getId());
        if (course == null)
            throw new NotFoundException(new JsonError("Error", "Grade's course " + grade.getCourse().getId() + " not found"));
        List<Grade> studentGrades = student.getGrades();
        grade.setId(GradebookDataService.getFirstAvailableGradeId());
        grade.setStudentOwnerIndex(student.getIndex());
        grade.setCourse(course);
        studentGrades.add(grade);
        student.setGrades(studentGrades);
        StudentDBService.updateStudent(student);

        String stringUri = "www.localhost:8080/students/" + student.getIndex() + "/grades/" + grade.getId();
        URI url = null;
        try {
            url = new URI(stringUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return Response.created(url).build();
    }

    @PUT
    @RolesAllowed("lecturer")
    @Path("/{id}")
    public Response updateGrade(@PathParam("index") long index, @PathParam("id") long id, @NotNull Grade newGrade) {
        Student student = StudentDBService.getStudent(index);
        if (student == null)
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        Optional<Grade> gradeMatch = student.getGrades().stream().filter(g -> g.getId() == id).findFirst();
        if (gradeMatch.isPresent()) {
            gradeMatch.get().setStudentOwnerIndex(index);
            gradeMatch.get().setValue(newGrade.getValue());
            gradeMatch.get().setCourse(newGrade.getCourse());
            gradeMatch.get().setDate(newGrade.getDate());
            StudentDBService.updateStudent(student);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            newGrade.setId(GradebookDataService.getFirstAvailableGradeId());
            List<Grade> grades = StudentDBService.getStudentGrades(index);
            grades.add(newGrade);
            student.setGrades(grades);
            StudentDBService.updateStudent(student);

            String stringUri = "www.localhost:8080/students/" + student.getIndex() + "/grades/" + newGrade.getId();
            URI url = null;
            try {
                url = new URI(stringUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

            return Response.created(url).build();
        }
    }

    @DELETE
    @RolesAllowed("lecturer")
    @Path("/{id}")
    public Response deleteGrade(@PathParam("index") long index, @PathParam("id") long id) {
        StudentDBService.deleteGrade(index, id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}