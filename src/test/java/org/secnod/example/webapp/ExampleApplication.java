package org.secnod.example.webapp;

import org.apache.shiro.web.jaxrs.ShiroFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.secnod.shiro.jersey.SubjectFactory;

/**
 * An example JAX-RS application using Apache Shiro.
 */
public class ExampleApplication extends ResourceConfig {

    public ExampleApplication() {
        super();
        register(ShiroFeature.class);
        register(new SubjectFactory());
        register(new HelloWorldResource());
    }
}
