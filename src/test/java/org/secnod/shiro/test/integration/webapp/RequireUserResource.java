package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.shiro.authz.annotation.RequiresUser;

@Path("/protected/user")
@Produces(MediaType.TEXT_PLAIN)
@RequiresUser
public class RequireUserResource {

    @GET
    public String get() {
        return "for non-anonymous users only";
    }
}