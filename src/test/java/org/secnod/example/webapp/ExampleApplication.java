package org.secnod.example.webapp;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import org.secnod.shiro.jaxrs.ShiroExceptionMapper;
import org.secnod.shiro.jersey.SubjectInjectableProvider;

/**
 * An example JAX-RS application using Apache Shiro.
 */
public class ExampleApplication extends Application {

    public ExampleApplication() {
        super();
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<Object>();
        singletons.add(new SubjectInjectableProvider());
        singletons.add(new ShiroExceptionMapper());
        singletons.add(new HelloWorldResource());
        return singletons;
    }
}
