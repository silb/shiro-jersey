package org.secnod.shiro.jersey;

import java.util.function.Function;

import org.glassfish.hk2.api.Factory;
import org.glassfish.jersey.server.ContainerRequest;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueParamProvider;
import org.secnod.shiro.jaxrs.Auth;

/**
 * Base class for factories that can instantiate object of a given type.
 *
 * @param <T> the type of the objects that the factory creates
 */
public abstract class TypeFactory<T> implements Factory<T>, ValueParamProvider, Function<ContainerRequest, T> {
    public final Class<T> type;

    public TypeFactory(Class<T> type) {
        this.type = type;
    }

    // org.glassfish.hk2.api.Factory<T>

    @Override
    public void dispose(T instance) {}

    // org.glassfish.jersey.server.spi.internal.ValueParamProvider

    @Override
    public Function<ContainerRequest, ?> getValueProvider(Parameter parameter) {
        if (type.equals(parameter.getRawType()) && parameter.isAnnotationPresent(Auth.class)) {
            return this;
        }
        return null;
    }

    @Override
    public PriorityType getPriority() {
        return Priority.NORMAL;
    }

    // java.util.Function<ContainerRequest, T>

    @Override
    public T apply(ContainerRequest request) {
        return provide();
    }
}