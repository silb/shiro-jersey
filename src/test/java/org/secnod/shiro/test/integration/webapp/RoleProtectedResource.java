package org.secnod.shiro.test.integration.webapp;

import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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