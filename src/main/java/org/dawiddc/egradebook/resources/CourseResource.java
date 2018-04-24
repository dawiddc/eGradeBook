package org.dawiddc.egradebook.resources;

import org.dawiddc.egradebook.exception.JsonError;
import org.dawiddc.egradebook.exception.NotFoundException;
import org.dawiddc.egradebook.model.Course;
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
@Path("courses")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CourseResource {

    private final GradebookDataService dataService = GradebookDataService.getInstance();

    @GET
    public List<Course> getCoursesList() {
        return dataService.getCoursesList();
    }

    @GET
    @Path("/{id}")
    public Course getCourse(@PathParam("id") long id) {
        for (Course course : dataService.getCoursesList()) {
            if (course.getId() == id)
                return course;
        }
        throw new NotFoundException(new JsonError("Error", "Course " + id + " not found"));
    }

    @POST
    @RolesAllowed("lecturer")
    public Response postCourse(Course course) {
        long createdIndex = dataService.addCourse(course);
        String stringUri = "www.localhost:8080/courses/" + createdIndex;
        URI url = null;
        try {
            url = new URI(stringUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.created(url).build();
    }


    @PUT
    @Path("/{id}")
    @RolesAllowed("lecturer")
    public Response updateCourse(@PathParam("id") long id, Course course) {
        int matchIndex;
        Optional<Course> match = dataService.getCoursesList().stream()
                .filter(c -> c.getId() == course.getId())
                .findFirst();
        if (match.isPresent()) {
            matchIndex = dataService.getCoursesList().indexOf(match.get());
            course.setId(id);
            dataService.getCoursesList().set(matchIndex, course);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            dataService.getCoursesList().add(course);
            return Response.status(Response.Status.CREATED).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("lecturer")
    public Response deleteCourse(@PathParam("id") long id) {
        Predicate<Course> course = c -> c.getId() == id;
        dataService.getCoursesList().removeIf(course);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
