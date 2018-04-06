package org.dawiddc.egradebook;

import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;

import static org.junit.Assert.assertEquals;

public class GradebookServiceTest {
    private HttpServer server;
    private WebTarget target;

    @Before
    public void setUp() {
        server = Main.startServer();

        Client c = ClientBuilder.newClient();
        target = c.target(Main.BASE_URI);

//        RestAssured.baseURI = "http://localhost";
//        RestAssured.port = 8080;
    }

    @After
    public void tearDown() {
        server.shutdownNow();
    }

    @Test
    public void getStudentListHttp200() {
        int response = target.path("/students").request().get().getStatus();
        assertEquals(200, response);
    }

    @Test
    public void getStudentHttp200() {
        int response = target.path("/students/0").request().get().getStatus();
        assertEquals(200, response);
    }

    @Test
    public void getStudentGradesHttp200() {
        int response = target.path("/students/0/grades").request().get().getStatus();
        assertEquals(200, response);
    }

    @Test
    public void getStudentGradeHttp200() {
        int response = target.path("/students/0/grades/0").request().get().getStatus();
        assertEquals(200, response);
    }

//    @Test
//    public void getCoursesList() {
//        int response = target.path("/courses").request().get().getStatus();
//        assertEquals(200, response);
//    }
//
//    @Test
//    public void getCourse() {
//        int response = target.path("/courses/Math").request().get().getStatus();
//        assertEquals(200, response);
//    }
}