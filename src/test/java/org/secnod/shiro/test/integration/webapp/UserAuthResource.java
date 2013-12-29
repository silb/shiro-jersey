package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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