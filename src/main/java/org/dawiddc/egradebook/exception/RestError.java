package org.dawiddc.egradebook.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RestError implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = Logger.getLogger(RestError.class.getName());

    @Override
    public Response toResponse(Throwable ex) {
        LOGGER.log(Level.SEVERE, "Rest Error Exception", ex);
        return Response.serverError().entity(ex.getMessage()).build();
    }
}
