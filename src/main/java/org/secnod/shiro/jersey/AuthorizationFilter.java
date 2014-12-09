package org.secnod.shiro.jersey;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.authz.aop.AuthenticatedAnnotationHandler;
import org.apache.shiro.authz.aop.AuthorizingAnnotationHandler;
import org.apache.shiro.authz.aop.GuestAnnotationHandler;
import org.apache.shiro.authz.aop.PermissionAnnotationHandler;
import org.apache.shiro.authz.aop.RoleAnnotationHandler;
import org.apache.shiro.authz.aop.UserAnnotationHandler;
import org.glassfish.jersey.server.internal.process.MappableException;

/**
 * A filter that grants or denies access to a JAX-RS resource based on the Shiro annotations on it.
 *
 * @param <T> the type of the Shiro annotation.
 *
 * @see org.apache.shiro.authz.annotation
 */
public class AuthorizationFilter implements ContainerRequestFilter {

    private final Map<AuthorizingAnnotationHandler, Annotation> authzChecks;

    public AuthorizationFilter(Collection<Annotation> authzSpecs) {
        Map<AuthorizingAnnotationHandler, Annotation> authChecks = new HashMap<>(authzSpecs.size());
        for (Annotation authSpec : authzSpecs) {
            authChecks.put(createHandler(authSpec), authSpec);
        }
        this.authzChecks = Collections.unmodifiableMap(authChecks);
    }

    private static AuthorizingAnnotationHandler createHandler(Annotation annotation) {
        Class<?> t = annotation.annotationType();
        if (RequiresPermissions.class.equals(t)) return new PermissionAnnotationHandler();
        else if (RequiresRoles.class.equals(t)) return new RoleAnnotationHandler();
        else if (RequiresUser.class.equals(t)) return new UserAnnotationHandler();
        else if (RequiresGuest.class.equals(t)) return new GuestAnnotationHandler();
        else if (RequiresAuthentication.class.equals(t)) return new AuthenticatedAnnotationHandler();
        else throw new IllegalArgumentException("Cannot create a handler for the unknown for annotation " + t);
    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        try {
            for (Map.Entry<AuthorizingAnnotationHandler, Annotation> authzCheck : authzChecks.entrySet()) {
                AuthorizingAnnotationHandler handler = authzCheck.getKey();
                Annotation authzSpec = authzCheck.getValue();
                handler.assertAuthorized(authzSpec);
            }
        } catch (AuthorizationException e) {
            throw new MappableException(e); // TODO Try without wrapping
        }
    }

}
