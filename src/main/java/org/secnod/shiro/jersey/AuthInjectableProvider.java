package org.secnod.shiro.jersey;

import java.util.Objects;

import org.secnod.shiro.jaxrs.Auth;

import com.sun.jersey.api.model.Parameter;
import com.sun.jersey.core.spi.component.ComponentContext;
import com.sun.jersey.core.spi.component.ComponentScope;
import com.sun.jersey.spi.inject.Injectable;
import com.sun.jersey.spi.inject.InjectableProvider;

public abstract class AuthInjectableProvider<T> implements InjectableProvider<Auth, Parameter> {

    private final Class<T> type;

    public AuthInjectableProvider(Class<T> type) {
        super();
        this.type = Objects.requireNonNull(type);
    }

    @Override
    public ComponentScope getScope() {
        return ComponentScope.PerRequest;
    }

    @Override
    public Injectable<T> getInjectable(ComponentContext ic, final Auth a, Parameter c) {
        if (type.equals(c.getParameterClass())) return getInjectable();
        else return null;
    }

    protected abstract Injectable<T> getInjectable();
}
