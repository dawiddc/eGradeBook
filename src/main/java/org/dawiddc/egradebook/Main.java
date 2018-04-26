package org.dawiddc.egradebook;

import org.dawiddc.egradebook.auth.AuthFilter;
import org.dawiddc.egradebook.exception.RestError;
import org.dawiddc.egradebook.model.GradebookDataService;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

class Main {

    public static final String BASE_URI = "http://localhost:8080/";

    public static HttpServer startServer() {

        final ResourceConfig rc = new ResourceConfig()
                .packages("org.dawiddc.egradebook")
                .packages("org.glassfish.jersey.examples.linking")
                .registerClasses(
                        DeclarativeLinkingFeature.class,
                        RestError.class,
                        AuthFilter.class);
        /* Create inital model objects */
        GradebookDataService.getInstance().createMockModel();
        return GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
    }

    /**
     * Created by Dawid C. on 05/04/2018.
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println(String.format("Jersey app started with WADL available at "
                + "%sapplication.wadl\nHit enter to stop it...", BASE_URI));
        //noinspection ResultOfMethodCallIgnored
        System.in.read();
        server.shutdownNow();
    }
}
