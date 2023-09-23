package org.secnod.shiro.test.integration.webapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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