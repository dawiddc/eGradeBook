package org.dawiddc.egradebook.resources;

import jersey.repackaged.com.google.common.collect.Lists;
import org.dawiddc.egradebook.dbservice.CourseDBService;
import org.dawiddc.egradebook.dbservice.GradebookDataService;
import org.dawiddc.egradebook.exception.JsonError;
import org.dawiddc.egradebook.exception.NotFoundException;
import org.dawiddc.egradebook.model.Course;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@PermitAll
@Path("courses")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class CourseResource {

    private static final Logger LOGGER = Logger.getLogger(CourseResource.class.getName());

    /**
     * returns courses list, can be filtered by lecturer
     *
     * @param lecturer filtering parameter
     * @return list of courses (filtered if lecturer is present)
     */
    @GET
    public Response getCoursesList(@QueryParam("lecturer") String lecturer,
                                   @QueryParam("name") String name) {
        List<Course> courses = CourseDBService.getCourses(lecturer, name);

        if (courses == null || courses.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND).entity("No courses found").build();
        }

        GenericEntity<List<Course>> entity = new GenericEntity<List<Course>>(Lists.newArrayList(courses)) {
        };
        return Response.status(Response.Status.OK).entity(entity).build();
    }

    /**
     * +     * Allows retrieving course by id.
     * +     *
     * +     * @param id unique value of course id we want to get.
     * +     * @return response of successful operation.
     * +
     */
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
            LOGGER.log(Level.SEVERE, "URI cast Exception", e);
        }

        return Response.created(url).entity(course).build();
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
            LOGGER.log(Level.SEVERE, "URI cast Exception", e);
        }
        return Response.created(url).entity(newCourse).build();
    }

    @DELETE
    @Path("/{id}")
    @RolesAllowed("lecturer")
    public Response deleteCourse(@PathParam("id") long id) {
        Course course = getCourse(id);
        CourseDBService.deleteCourse(id);
        return Response.status(Response.Status.NO_CONTENT).entity(course).build();
    }

}
