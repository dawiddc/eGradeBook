package org.dawiddc.egradebook.resources;

import org.dawiddc.egradebook.dbservice.CourseDBService;
import org.dawiddc.egradebook.dbservice.GradebookDataService;
import org.dawiddc.egradebook.exception.JsonError;
import org.dawiddc.egradebook.exception.NotFoundException;
import org.dawiddc.egradebook.model.Course;

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
@Path("courses")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CourseResource {

    @GET
    public List<Course> getCoursesList() {
        return CourseDBService.getCourses();
    }

    @GET
    @Path("/{id}")
    public Course getCourse(@PathParam("id") long id) {
        Course course = CourseDBService.getCourseById(id);
        if (course != null) {
            return course;
        }
        throw new NotFoundException(new JsonError("Error", "Course " + id + " not found"));
    }

    @POST
    @RolesAllowed("lecturer")
    public Response postCourse(Course course) {
        course.setId(GradebookDataService.getFirstAvailableCourseId());
        CourseDBService.addCourse(course);

        String stringUri = "www.localhost:8080/students/" + course.getId();
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
    public Response updateCourse(@PathParam("id") long id, @NotNull Course newCourse) {
        Course oldCourse = CourseDBService.getCourseById(id);
        if (oldCourse != null) {
            oldCourse.setName(newCourse.getName());
            oldCourse.setLecturer(newCourse.getLecturer());
            CourseDBService.updateCourse(oldCourse);

            return Response.status(Response.Status.NO_CONTENT).build();
        }
        newCourse.setId(id);
        CourseDBService.addCourse(newCourse);
        String stringUri = "www.localhost:8080/students/" + newCourse.getId();
        URI url = null;
        try {
            url = new URI(stringUri);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return Response.created(url).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("lecturer")
    public Response deleteCourse(@PathParam("id") long id) {
        CourseDBService.deleteCourse(id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
