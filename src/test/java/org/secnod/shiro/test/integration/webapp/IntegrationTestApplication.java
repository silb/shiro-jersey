package org.secnod.shiro.test.integration.webapp;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.secnod.example.webapp.ExampleApplication;
import org.secnod.example.webapp.UserFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * A JAX-RS application for running the integration tests.
 */
public class IntegrationTestApplication extends ExampleApplication {

    public IntegrationTestApplication() {
        super();
        register(new UserFactory());
        register(new JacksonJsonProvider());
        for (Object resource : createAllIntegrationTestResources()) {
            register(resource);
        }
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
                new InjectionResource()
                ));
    }
}
