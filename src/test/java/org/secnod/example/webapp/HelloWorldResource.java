package org.secnod.example.webapp;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/hello-world")
@Produces(MediaType.TEXT_PLAIN)
public class HelloWorldResource {

    @GET
    public String get() {
        return "Hello, world!";
    }
}