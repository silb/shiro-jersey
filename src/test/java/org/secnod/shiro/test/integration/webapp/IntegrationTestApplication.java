package org.secnod.shiro.test.integration.webapp;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.shiro.web.jaxrs.ShiroFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.secnod.example.webapp.UserFactory;
import org.secnod.shiro.jaxrs.ShiroExceptionMapper;
import org.secnod.shiro.jersey.AuthorizationFilterFeature;
import org.secnod.shiro.jersey.SubjectFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

/**
 * A JAX-RS application for running the integration tests.
 */
public class IntegrationTestApplication extends ResourceConfig {

    private static final Logger log = LoggerFactory.getLogger(IntegrationTestApplication.class);

    public IntegrationTestApplication() {
        super();
        if (Boolean.getBoolean("shiro.jersey")) {
            log.info("Using the shiro-jersey feature.");
            register(new AuthorizationFilterFeature());
            register(new ShiroExceptionMapper());
        } else {
            log.info("Using the native shiro-jaxrs feature.");
            register(ShiroFeature.class);
        }
        register(new SubjectFactory());
        register(new UserFactory());
        register(new JacksonJsonProvider());
        for (Object resource : createAllIntegrationTestResources()) {
            register(resource);
        }
        for (Class<?> resource : allIntegrationTestResourceClasses()) {
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

    public static Set<Class<?>> allIntegrationTestResourceClasses() {
        return Collections.singleton(FieldInjectionResource.class);
    }
}
