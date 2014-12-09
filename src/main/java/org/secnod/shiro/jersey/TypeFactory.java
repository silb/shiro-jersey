package org.secnod.shiro.jersey;

import javax.inject.Singleton;

import org.glassfish.hk2.api.DynamicConfiguration;
import org.glassfish.hk2.api.Factory;
import org.glassfish.hk2.utilities.Binder;
import org.glassfish.jersey.internal.inject.Injections;
import org.glassfish.jersey.server.model.Parameter;
import org.glassfish.jersey.server.spi.internal.ValueFactoryProvider;

/**
 * Base class for factories that can instantiate object of a given type.
 *
 * Able to {@link ValueFactoryProvider provide} and {@link Binder bind} itself.
 *
 * @param <T> the type of the objects that the factory creates
 */
public abstract class TypeFactory<T> implements Factory<T>, ValueFactoryProvider, Binder {
    public final Class<T> type;

    public TypeFactory(Class<T> type) {
        this.type = type;
    }

    // org.glassfish.hk2.api.Factory<T>

    @Override
    public void dispose(T instance) {}

    // org.glassfish.jersey.server.spi.internal.ValueFactoryProvider

    @Override
    public Factory<?> getValueFactory(Parameter parameter) {
        if (type.equals(parameter.getRawType())) {
            return this;
        }
        return null;
    }

    @Override
    public PriorityType getPriority() {
        return Priority.NORMAL;
    }

    // org.glassfish.hk2.utilities.Binder

    @Override
    public void bind(DynamicConfiguration config) {
      Injections.addBinding(
              Injections.newFactoryBinder(this).to(type).in(Singleton.class),
              config);
      Injections.addBinding(
              Injections.newBinder(this).to(ValueFactoryProvider.class),
              config);
    }
}