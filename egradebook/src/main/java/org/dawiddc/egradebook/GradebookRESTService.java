package org.dawiddc.egradebook;

import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.model.Grade;
import org.dawiddc.egradebook.model.GradebookDataService;
import org.dawiddc.egradebook.model.Student;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

@Path("/")
public class GradebookRESTService {

    private final GradebookDataService dataService = GradebookDataService.getInstance();
    private final List<Student> studentsList = dataService.getStudentsList();

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
        throw new NotFoundException(new JsonError("Error", "Student " + String.valueOf(index) + " not found"));
    }

    @GET
    @Path("/students/{index}/grades")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Grade> getStudentGrades(@PathParam("index") long index) {
        if (dataService.getGradesList(index) != null)
            return dataService.getGradesList(index);
        else
            throw new NotFoundException(new JsonError("Error", "Student " + String.valueOf(index) + " not found"));
    }

    @GET
    @Path("/students/{index}/grades/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Grade getStudentGrade(@PathParam("index") long index, @PathParam("id") long id) {
        if (dataService.getGradesList(index) != null) {
            for (Grade grade : dataService.getGradesList(index)) {
                if (grade.getId() == id)
                    return grade;
            }
            throw new NotFoundException(new JsonError("Error", "Grade " + String.valueOf(id) + " not found"));
        } else
            throw new NotFoundException(new JsonError("Error", "Student " + String.valueOf(index) + " not found"));
    }

    @GET
    @Path("/courses")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public List<Course> getCoursesList() {
        return dataService.getCoursesList();
    }

    @GET
    @Path("/courses/{id}")
    @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON})
    public Course getCourse(@PathParam("id") long id) {
        for (Course course : dataService.getCoursesList()) {
            if (course.getId() == id)
                return course;
        }
        throw new NotFoundException(new JsonError("Error", "Course " + String.valueOf(id) + " not found"));
    }

    @POST
    @Path("/students")
    public Response postStudent(Student student) {
        dataService.addStudent(student);

        return Response.status(Response.Status.CREATED).build();
    }

    @POST
    @Path("/students/{index}/grades")
    public Response postGrade(@PathParam("index") long index, Grade grade) {
        if (dataService.addGrade(index, grade) != null)
            return Response.status(Response.Status.CREATED).build();
        else
            throw new NotFoundException(new JsonError("Error", "Student " + String.valueOf(index) + " not found"));
    }

    @POST
    @Path("/courses")
    public Response postCourse(Course course) {
        dataService.addCourse(course);

        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/students/{index}")
    public Response updateStudent(@PathParam("index") long index, Student student) {
        int matchIndex;
        Optional<Student> match = studentsList.stream()
                .filter(s -> s.getIndex() == index)
                .findFirst();
        if (match.isPresent()) {
            matchIndex = studentsList.indexOf(match.get());
            student.setIndex(index);
            studentsList.set(matchIndex, student);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            studentsList.add(student);
            return Response.status(Response.Status.CREATED).build();
        }
    }

    @PUT
    @Path("/courses/{id}")
    public Response updateCourse(@PathParam("id") long id, Course course) {
        int matchIndex;
        Optional<Course> match = dataService.getCoursesList().stream()
                .filter(c -> c.getId() == course.getId())
                .findFirst();
        if (match.isPresent()) {
            matchIndex = studentsList.indexOf(match.get());
            course.setId(id);
            dataService.getCoursesList().set(matchIndex, course);
            return Response.status(Response.Status.NO_CONTENT).build();
        } else {
            dataService.getCoursesList().add(course);
            return Response.status(Response.Status.CREATED).build();
        }
    }

    @PUT
    @Path("/students/{index}/grades/{id}")
    public Response updateGrade(@PathParam("index") long index, @PathParam("id") long id, Grade grade) {
        if (dataService.getGradesList(index) == null)
            throw new NotFoundException(new JsonError("Error", "Student " + String.valueOf(index) + " not found"));

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
            dataService.getGradesList(index).add(grade);
            return Response.status(Response.Status.CREATED).build();
        }
    }

    @DELETE
    @Path("/students/{index}")
    public Response deleteStudent(@PathParam("index") long index) {
        Predicate<Student> student = s -> s.getIndex() == index;
        studentsList.removeIf(student);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/courses/{id}")
    public Response deleteCourse(@PathParam("id") long id) {
        Predicate<Course> course = c -> c.getId() == id;
        dataService.getCoursesList().removeIf(course);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

    @DELETE
    @Path("/students/{index}/grades/{id}")
    public Response deleteGrade(@PathParam("index") long index, @PathParam("id") long id) {
        Predicate<Grade> grade = g -> g.getId() == id;
        if (dataService.getGradesList(index) != null) {
            dataService.getGradesList(index).removeIf(grade);
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        throw new NotFoundException(new JsonError("Error", "Student " + String.valueOf(index) + " not found"));
    }
}
