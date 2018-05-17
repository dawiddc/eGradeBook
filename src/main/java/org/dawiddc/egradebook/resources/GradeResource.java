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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@PermitAll
@Path("students/{index}/grades")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class GradeResource {

    @GET
    @RolesAllowed("lecturer")
    public Response getStudentGrades(@PathParam("index") long index,
                                     @QueryParam("courseName") String courseName,
                                     @QueryParam("value") Float value,
                                     @QueryParam("valueRelation") String valueRelation) {
        Student student = StudentDBService.getStudent(index);
        if (student == null)
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));

        List<Grade> grades = student.getGrades();
        if (grades == null || grades.isEmpty()) {
            throw new NotFoundException(new JsonError("Error", "This student has no grades"));
        }

        /* Filtering by grade's course name */
        if (courseName != null) {
            grades = grades.stream().filter(g -> g.getCourse().getName().equals(courseName)).collect(Collectors.toList());
        }

        /* Filtering by grade assign date */
        if (value != null && valueRelation != null) {
            switch (valueRelation.toLowerCase()) {
                case "grater":
                    grades = grades.stream().filter(gr -> gr.getValue() > value).collect(Collectors.toList());
                    break;
                case "lower":
                    grades = grades.stream().filter(gr -> gr.getValue() < value).collect(Collectors.toList());
                    break;
            }
        }

        GenericEntity<List<Grade>> entity = new GenericEntity<List<Grade>>(Lists.newArrayList(grades)) {
        };

        return Response.status(Response.Status.OK).entity(entity).build();
    }

    @GET
    @Path("/{id}")
    public Grade getStudentGrade(@PathParam("index") long index, @PathParam("id") long id) {
        Student student = StudentDBService.getStudent(index);
        if (student == null)
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
        if (student.getGrades() == null || student.getGrades().size() < 1)
            throw new NotFoundException(new JsonError("Error", "Grade list is empty"));
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
        if (studentGrades == null)
            studentGrades = new ArrayList<>();
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
        Course gradesCourse = CourseDBService.getCourseById(newGrade.getCourse().getId());
        if (gradesCourse == null) {
            throw new NotFoundException(new JsonError("Error", "Course " + newGrade.getCourse().getId() + " not found"));
        }
        Grade studentGrade = StudentDBService.getStudentGrade(index, id);
        /* If grade already exists, modify, else create it */

        if (studentGrade != null) {
            studentGrade.setStudentOwnerIndex(index);
            studentGrade.setValue(newGrade.getValue());
            studentGrade.setDate(newGrade.getDate());
            studentGrade.setCourse(gradesCourse);
            student.setGrades(student.getGrades().stream().map(g -> g.getId() == id ? studentGrade : g).collect(Collectors.toList()));
            StudentDBService.updateStudent(student);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            newGrade.setId(id);
            newGrade.setStudentOwnerIndex(index);
            newGrade.setCourse(gradesCourse);
            List<Grade> grades = StudentDBService.getStudentGrades(index);
            if (grades == null)
                grades = new ArrayList<>();
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