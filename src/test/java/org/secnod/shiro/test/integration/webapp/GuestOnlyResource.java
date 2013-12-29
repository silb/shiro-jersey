package org.secnod.shiro.test.integration.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.shiro.authz.annotation.RequiresGuest;

@Path("/guestonly")
@Produces(MediaType.TEXT_PLAIN)
@RequiresGuest
public class GuestOnlyResource {

    @GET
    public String get() {
        return "guest only";
    }
}