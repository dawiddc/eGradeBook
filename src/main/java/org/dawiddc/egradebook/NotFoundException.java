package org.dawiddc.egradebook;


import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


public class NotFoundException extends WebApplicationException {

    public NotFoundException(JsonError jse) {
        super(Response.status(Response.Status.NOT_FOUND).entity(jse).type(MediaType.APPLICATION_JSON).build());
    }

}