package org.secnod.shiro.test.integration.webapp;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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