package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

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