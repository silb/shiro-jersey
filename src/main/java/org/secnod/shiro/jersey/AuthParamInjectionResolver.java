package org.secnod.shiro.jersey;

import org.glassfish.jersey.server.internal.inject.ParamInjectionResolver;
import org.secnod.shiro.jaxrs.Auth;

/**
 * For method parameter injection with the {@linkplain Auth} annotation.
 */
public class AuthParamInjectionResolver extends ParamInjectionResolver<Auth> {

    public AuthParamInjectionResolver() {
        super(TypeFactory.class);
    }
}
