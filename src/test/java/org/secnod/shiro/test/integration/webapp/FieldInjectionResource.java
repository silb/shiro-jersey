package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.subject.Subject;
import org.secnod.shiro.jaxrs.Auth;

@Path("/inject/field")
@Produces(MediaType.TEXT_PLAIN)
public class FieldInjectionResource {

    @Auth Subject subject;

    @GET
    public Response sessionUser() {
        String principal;
        try {
            principal = subject.getPrincipal().toString();
        } catch (NullPointerException e) {
            return Response.status(Status.INTERNAL_SERVER_ERROR).entity("Excpected error").build();
        }
        return Response.status(Status.OK).entity(principal).build();
    }
}