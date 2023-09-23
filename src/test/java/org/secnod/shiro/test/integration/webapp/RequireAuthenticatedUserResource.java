package org.secnod.shiro.test.integration.webapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.shiro.authz.annotation.RequiresAuthentication;

@Path("/protected/noRememberMe")
@Produces(MediaType.TEXT_PLAIN)
@RequiresAuthentication
public class RequireAuthenticatedUserResource {

    @GET
    public String get() {
        return "for non anonymous user users only, remember me not allowed";
    }
}