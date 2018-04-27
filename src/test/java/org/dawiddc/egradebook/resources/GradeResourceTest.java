package org.dawiddc.egradebook.resources;

import org.dawiddc.egradebook.Main;
import org.dawiddc.egradebook.model.Course;
import org.dawiddc.egradebook.model.Grade;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GradeResourceTest {
    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() {
        server = Main.startServer();

        Client c = ClientBuilder.newClient();
        c.register(HttpAuthenticationFeature.basic("lecturer", "password"));
        target = c.target(Main.BASE_URI);
//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;
    }

    @After
    public void tearDown() {
        server.shutdownNow();
    }

    @Test
    public void getStudentGradesHttp200() {
        int response = target.path("/students/1/grades").request().get().getStatus();
        assertEquals(200, response);
    }

    @Test
    public void getStudentGradeHttp200() {
        int response = target.path("/students/1/grades/2").request().get().getStatus();
        assertEquals(200, response);
    }

    @Test
    public void createGradeHttp201() {
        Grade grade = new Grade.GradeBuilder()
                .value((float) 4.5)
                .date(new Date("2018/04/05"))
                .course(new Course.CourseBuilder().name("Anatomy").lecturer("Joey Tribbiani").build())
                .build();
        int response = target.path("/students/1/grades").request().post(Entity.json(grade)).getStatus();
        assertEquals(201, response);
    }

    @Test
    public void updateGradeHttp200() {
        Grade grade = new Grade.GradeBuilder()
                .value((float) 4.5)
                .date(new Date("2018/04/05"))
                .course(new Course.CourseBuilder().name("Anatomy").lecturer("Joey Tribbiani").build())
                .build();
        int response = target.path("/students/1/grades/0").request().put(Entity.json(grade)).getStatus();
        assertThat(response, Matchers.either(Matchers.is(201)).or(Matchers.is(204)));
    }

    @Test
    public void deleteGradeHttp204() {
        int response = target.path("/students/1/grades/0").request().delete().getStatus();
        assertEquals(204, response);
    }
}