package org.secnod.example.webapp;

import org.glassfish.jersey.server.ResourceConfig;
import org.secnod.shiro.jaxrs.ShiroExceptionMapper;
import org.secnod.shiro.jersey.AuthorizationFilterFeature;
import org.secnod.shiro.jersey.SubjectFactory;

/**
 * An example JAX-RS application using Apache Shiro.
 */
public class ExampleApplication extends ResourceConfig {

    public ExampleApplication() {
        super();
        register(new AuthorizationFilterFeature());
        register(new SubjectFactory());
        register(new ShiroExceptionMapper());

        register(new HelloWorldResource());
    }
}
