package org.dawiddc.egradebook.resources;

import org.dawiddc.egradebook.exception.JsonError;
import org.dawiddc.egradebook.exception.NotFoundException;
import org.dawiddc.egradebook.model.Grade;
import org.dawiddc.egradebook.model.GradebookDataService;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@PermitAll
@Path("students/{index}/grades")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class GradeResource {

    private final GradebookDataService dataService = GradebookDataService.getInstance();

    @GET
    @RolesAllowed("lecturer")
    public List<Grade> getStudentGrades(@PathParam("index") long index) {
        if (dataService.getGradesList(index) != null)
            return dataService.getGradesList(index);
        else
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
    }

    @POST
    @RolesAllowed("lecturer")
    public Response postGrade(@PathParam("index") long index, Grade grade) {
        Long createdIndex = dataService.addGrade(index, grade);
        if (createdIndex != null) {
            String stringUri = "www.localhost:8080/students/" + index + "/grades/" + createdIndex;
            URI url = null;
            try {
                url = new URI(stringUri);
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            return Response.created(url).build();
        } else
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
    }

    @GET
    @Path("/{id}")
    public Grade getStudentGrade(@PathParam("index") long index, @PathParam("id") long id) {
        if (dataService.getGradesList(index) != null) {
            Optional<Grade> gradeMatch = dataService.getGradesList(index).stream()
                    .filter(g -> g.getId() == id).findFirst();
            if (gradeMatch.isPresent()) {
                return gradeMatch.get();
            }
            throw new NotFoundException(new JsonError("Error", "Grade " + id + " not found"));
        } else
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
    }

    @PUT
    @RolesAllowed("lecturer")
    @Path("/{id}")
    public Response updateGrade(@PathParam("index") long index, @PathParam("id") long id, Grade grade) {
        if (dataService.getGradesList(index) == null)
            throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));

        int matchIndex;
        Optional<Grade> match = dataService.getGradesList(index).stream()
                .filter(g -> g.getId() == grade.getId())
                .findFirst();
        if (match.isPresent()) {
            matchIndex = dataService.getGradesList(index).indexOf(match.get());
            grade.setId(id);
            dataService.getGradesList(index).set(matchIndex, grade);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            dataService.addGrade(index, grade);
            return Response.status(Response.Status.CREATED).build();
        }
    }

    @DELETE
    @RolesAllowed("lecturer")
    @Path("/{id}")
    public Response deleteGrade(@PathParam("index") long index, @PathParam("id") long id) {
        Predicate<Grade> grade = g -> g.getId() == id;
        if (dataService.getGradesList(index) != null) {
            dataService.getGradesList(index).removeIf(grade);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        throw new NotFoundException(new JsonError("Error", "Student " + index + " not found"));
    }

}
