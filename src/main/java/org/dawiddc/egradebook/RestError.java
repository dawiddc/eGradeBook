package org.dawiddc.egradebook;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;

class RestError implements ExceptionMapper<Throwable> {


    @Override
    public Response toResponse(Throwable ex) {
        ex.printStackTrace();
        return Response.serverError().entity(ex.getMessage()).build();
    }
}
