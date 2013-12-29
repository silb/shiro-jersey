package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.subject.Subject;
import org.secnod.example.webapp.User;
import org.secnod.shiro.jaxrs.Auth;

@Path("/inject")
@Produces(MediaType.TEXT_PLAIN)
public class InjectionResource {

    @Path("usersubject")
    @GET
    public String sessionUser(@Auth Subject subject, @Auth User user) {
        if (subject != user.unwrap(Subject.class)) {
            throw new WebApplicationException(Status.INTERNAL_SERVER_ERROR);
        }
        return "User and Subject method param injection works.\n";
    }
}