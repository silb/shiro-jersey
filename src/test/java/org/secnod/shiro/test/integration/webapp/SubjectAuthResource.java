package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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