package org.secnod.shiro.test.integration.webapp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.secnod.example.webapp.ExampleApplication;
import org.secnod.example.webapp.UserInjectableProvider;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * A JAX-RS application for running the integration tests.
 */
public class IntegrationTestApplication extends ExampleApplication {

    public IntegrationTestApplication() {
        super();
    }

    public static Set<Object> createAllIntegrationTestResources() {
        return new HashSet<Object>(Arrays.asList(
                new PublicResource(),
                new GuestOnlyResource(),
                new PermissionProtectedResource(),
                new RoleProtectedResource(),
                new RequireUserResource(),
                new RequireAuthenticatedUserResource(),
                new SessionResource(),
                new SubjectAuthResource(),
                new UserAuthResource(),
                new InjectionResource(),
                new FieldInjectionResource()));
    }

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = super.getSingletons();
        singletons.addAll(Arrays.asList(
                new JacksonJsonProvider(),
                new UserInjectableProvider()));
        singletons.addAll(createAllIntegrationTestResources());
        return singletons;
    }
}
