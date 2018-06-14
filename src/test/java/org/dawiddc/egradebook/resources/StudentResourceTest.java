//package org.dawiddc.egradebook.resources;
//
//import org.dawiddc.egradebook.Main;
//import org.dawiddc.egradebook.model.Student;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import java.util.Collections;
//import java.util.Date;
//
//import static org.junit.Assert.assertEquals;
//
//public class StudentResourceTest {
//    private HttpServer server;
//    private WebTarget target;
//
//    @Before
//    public void setUp() {
//        server = Main.startServer();
//
//        Client c = ClientBuilder.newClient();
//        c.register(HttpAuthenticationFeature.basic("lecturer", "password"));
//        target = c.target(Main.BASE_URI);
////        RestAssured.baseURI = "http://localhost";
////        RestAssured.port = 8080;
//    }
//
//    @After
//    public void tearDown() {
//        server.shutdownNow();
//    }
//
//    @Test
//    public void getStudentListHttp200() {
//        int response = target.path("/students").request().get().getStatus();
//        assertEquals(200, response);
//    }
//
//    @Test
//    public void getStudentHttp200() {
//        int response = target.path("/students/1").request().get().getStatus();
//        assertEquals(200, response);
//    }
//
//    @Test
//    public void createStudentHttp201() {
//        Student student = new Student.StudentBuilder()
//                .firstName("John")
//                .lastName("Tester")
//                .birthday(new Date("1999/02/02"))
//                .grades(Collections.emptyList()).build();
//        int response = target.path("/students").request().post(Entity.json(student)).getStatus();
//        assertEquals(201, response);
//    }
////
////    @Test
////    public void updateStudentHttp200() {
////        Student student = new Student.StudentBuilder()
////                .firstName("John")
////                .lastName("Tester")
////                .birthday(new Date("1999/02/02"))
////                .grades(Collections.emptyList()).build();
////        int response = target.path("/students/0").request().put(Entity.json(student)).getStatus();
////        assertThat(response, Matchers.either(Matchers.is(201)).or(Matchers.is(204)));
////    }
////
////    @Test
////    public void deleteStudentHttp204() {
////        int response = target.path("/students/0").request().delete().getStatus();
////        assertEquals(204, response);
////    }
////
////    @Test(expected = NotFoundException.class)
////    public void shouldThrowNotFoundException() {
////        StudentResource studentResource = new StudentResource();
////        studentResource.getStudent(999);
////    }
//
//}