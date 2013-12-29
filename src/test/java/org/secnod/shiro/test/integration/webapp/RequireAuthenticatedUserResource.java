package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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