package org.secnod.shiro.test.integration.webapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/public")
@Produces(MediaType.TEXT_PLAIN)
public class PublicResource {

    @GET
    public String get() {
        return "a public resource";
    }
}