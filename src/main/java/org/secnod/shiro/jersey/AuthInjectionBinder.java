package org.secnod.shiro.jersey;

import javax.inject.Singleton;

import org.glassfish.hk2.api.InjectionResolver;
import org.glassfish.hk2.api.TypeLiteral;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.secnod.shiro.jaxrs.Auth;

/**
 * Enable injection with the {@link Auth} annotation.
 */
public class AuthInjectionBinder extends AbstractBinder {

    @Override
    protected void configure() {
        bind(AuthParamInjectionResolver.class).in(Singleton.class)
            .to(new TypeLiteral<InjectionResolver<Auth>>() {});
    }
}
