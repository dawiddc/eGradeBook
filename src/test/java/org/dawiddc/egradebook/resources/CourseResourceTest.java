//package org.dawiddc.egradebook.resources;
//
//import org.dawiddc.egradebook.Main;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.glassfish.jersey.client.authentication.HttpAuthenticationFeature;
//import org.junit.After;
//import org.junit.Before;
//import org.junit.Test;
//
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.WebTarget;
//
//import static org.junit.Assert.assertEquals;
//
//public class CourseResourceTest {
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
//
//    @Test
//    public void getCoursesHttp200() {
//        int response = target.path("/courses").request().get().getStatus();
//        assertEquals(200, response);
//
//    }
//
//    @Test
//    public void getCourseHttp200() {
//        int response = target.path("/courses/1").request().get().getStatus();
//        assertEquals(200, response);
//    }
//
////
////    @Test
////    public void createCourseHttp201() {
////        Course course = new Course.CourseBuilder().name("TestCourse").lecturer("Ross Geller").build();
////        Response response = target.path("/courses").request().post(Entity.json(course));
////        assertEquals(201, response.getStatus());
////        target.path(response.getHeaderString("Location")).request().delete();
////    }
////
////
////    @Test
////    public void updateCourseHttp200() {
////        Course course = new Course.CourseBuilder().name("TestCourse").lecturer("Ross Geller").build();
////        int response = target.path("/courses/0").request().put(Entity.json(course)).getStatus();
////        assertThat(response, Matchers.either(Matchers.is(201)).or(Matchers.is(204)));
////    }
////
////
////    @Test
////    public void deleteCourseHttp204() {
////        int response = target.path("/courses/0").request().delete().getStatus();
////        assertEquals(204, response);
////    }
//}