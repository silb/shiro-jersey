package org.secnod.shiro.test.integration.webapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

import org.apache.shiro.subject.Subject;
import org.secnod.shiro.jaxrs.Auth;

@Path("/inject/field")
@Produces(MediaType.TEXT_PLAIN)
public class FieldInjectionResource {

    @Auth Subject subject;

    @GET
    public String sessionUser() {
        try {
            return subject.getPrincipal().toString();
        } catch (NullPointerException e) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
    }
}