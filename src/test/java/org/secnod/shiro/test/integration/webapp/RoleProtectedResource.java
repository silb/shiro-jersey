package org.secnod.shiro.test.integration.webapp;

import java.util.concurrent.atomic.AtomicReference;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.shiro.authz.annotation.RequiresRoles;

@Path("/protected/role")
@Produces(MediaType.TEXT_PLAIN)
@RequiresRoles("user")
public class RoleProtectedResource {

    private AtomicReference<String> value = new AtomicReference<String>("a role protected resource");

    @GET
    public String get() {
        return value.get();
    }

    @PUT
    @RequiresRoles("superuser")
    public String set(String newValue) {
        value.set(newValue);
        return newValue;
    }

}