package org.secnod.shiro.test.integration.webapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.secnod.shiro.jaxrs.Auth;

// TODO Delete?

@Path("/auth/subject")
@Produces(MediaType.TEXT_PLAIN)
@RequiresPermissions("protected:read")
public class SubjectAuthResource {

    @GET
    public String get(@Auth Subject subject) {
        if (!subject.isAuthenticated()) throw new UnauthenticatedException();

        return Double.toString(Math.random());
    }
}