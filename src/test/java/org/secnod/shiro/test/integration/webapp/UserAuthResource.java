package org.secnod.shiro.test.integration.webapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.secnod.example.webapp.User;
import org.secnod.shiro.jaxrs.Auth;

//TODO Delete?

@Path("/auth/user")
@Produces(MediaType.TEXT_PLAIN)
@RequiresPermissions("protected:read")
public class UserAuthResource {

    @GET
    public String get(@Auth User user) {
        user.checkPermissionBySomeRule();
        return Double.toString(Math.random());
    }
}