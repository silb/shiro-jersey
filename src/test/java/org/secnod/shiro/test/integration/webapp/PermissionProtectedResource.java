package org.secnod.shiro.test.integration.webapp;

import java.util.concurrent.atomic.AtomicReference;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

import org.apache.shiro.authz.annotation.RequiresPermissions;

@Path("/protected/permission")
@Produces(MediaType.TEXT_PLAIN)
@RequiresPermissions("protected:read")
public class PermissionProtectedResource {

    private AtomicReference<String> value = new AtomicReference<String>("a permission protected resource");

    @GET
    public String get() {
        return value.get();
    }

    @PUT
    @RequiresPermissions("protected:write")
    public String set(String newValue) {
        value.set(newValue);
        return newValue;
    }

}