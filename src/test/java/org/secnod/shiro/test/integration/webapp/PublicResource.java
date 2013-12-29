package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/public")
@Produces(MediaType.TEXT_PLAIN)
public class PublicResource {

    @GET
    public String get() {
        return "a public resource";
    }
}