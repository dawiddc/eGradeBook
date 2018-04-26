package org.dawiddc.egradebook.auth;


import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.SecurityContext;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.Principal;

@Provider
@PreMatching
public class AuthFilter implements ContainerRequestFilter {
    @Inject
    private
    javax.inject.Provider<UriInfo> uriInfo;

    @Override
    public void filter(ContainerRequestContext filterContext) {
        User user = authenticate(filterContext);
        filterContext.setSecurityContext(new Authorizer(user));
    }

    private User authenticate(ContainerRequestContext filterContext) {
        // Extract authentication credentials
        String authentication = filterContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        if (authentication == null || !authentication.startsWith("Basic ")) {
            return new User("student", "student");
        }
        authentication = authentication.substring("Basic ".length());
        String[] values = new String(DatatypeConverter.parseBase64Binary(authentication), Charset.forName("ASCII")).split(":");
        if (values.length < 2) {
            throw new WebApplicationException(400);
            // "Invalid syntax for username and password"
        }
        String username = values[0];
        String password = values[1];
        if ((username == null) || (password == null)) {
            throw new WebApplicationException(400);
            // "Missing username or password"
        }
        // Validate the extracted credentials
        User user;

        if (username.equals("lecturer") && password.equals("password")) {
            user = new User("lecturer", "lecturer");
        } else {
            user = new User("student", "student");
        }
        return user;
    }
    class Authorizer implements SecurityContext {


        private final User user;
        private final Principal principal;

        Authorizer(final User user) {
            this.user = user;
            this.principal = () -> user.username;
        }

        public Principal getUserPrincipal() {
            return this.principal;
        }

        public boolean isUserInRole(String role) {
            return (role.equals(user.role));
        }

        public boolean isSecure() {
            return "https".equals(uriInfo.get().getRequestUri().getScheme());
        }

        public String getAuthenticationScheme() {
            return SecurityContext.BASIC_AUTH;
        }
    }

    class User {

        final String username;
        final String role;

        User(String username, String role) {
            this.username = username;
            this.role = role;
        }
    }
}