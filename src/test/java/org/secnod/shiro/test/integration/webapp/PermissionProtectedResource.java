package org.secnod.shiro.test.integration.webapp;

import java.util.concurrent.atomic.AtomicReference;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

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