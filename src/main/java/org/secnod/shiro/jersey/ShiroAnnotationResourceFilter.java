package org.secnod.shiro.jersey;

import java.lang.annotation.Annotation;

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

import com.sun.jersey.api.container.MappableContainerException;
import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerRequestFilter;
import com.sun.jersey.spi.container.ContainerResponseFilter;
import com.sun.jersey.spi.container.ResourceFilter;

/**
 * A filter that grants or denies access to a JAX-RS resource based on the Shiro annotations on it.
 *
 * @param <T> the type of the Shiro annotation.
 *
 * @see org.apache.shiro.authz.annotation
 */
public class ShiroAnnotationResourceFilter<T extends Annotation> implements ResourceFilter, ContainerRequestFilter {

    private final T authzSpec;
    private final AuthorizingAnnotationHandler handler;

    public ShiroAnnotationResourceFilter(T authzSpec, AuthorizingAnnotationHandler handler) {
        this.authzSpec = authzSpec;
        this.handler = handler;
    }

    public static <T extends Annotation> ShiroAnnotationResourceFilter<T> valueOf(T authzSpec) {
        return new ShiroAnnotationResourceFilter<T>(authzSpec, defaultHandler(authzSpec));
    }

    private static AuthorizingAnnotationHandler defaultHandler(Annotation annotation) {
        Class<?> t = annotation.annotationType();
        if (RequiresPermissions.class.equals(t)) return new PermissionAnnotationHandler();
        else if (RequiresRoles.class.equals(t)) return new RoleAnnotationHandler();
        else if (RequiresUser.class.equals(t)) return new UserAnnotationHandler();
        else if (RequiresGuest.class.equals(t)) return new GuestAnnotationHandler();
        else if (RequiresAuthentication.class.equals(t)) return new AuthenticatedAnnotationHandler();
        else throw new IllegalArgumentException("No default handler known for annotation " + t);
    }

    @Override
    public ContainerRequestFilter getRequestFilter() {
        return this;
    }

    @Override
    public ContainerResponseFilter getResponseFilter() {
        return null;
    }

    @Override
    public ContainerRequest filter(ContainerRequest request) {
        try {
            handler.assertAuthorized(authzSpec);
            return request;
        } catch (AuthorizationException e) {
            throw new MappableContainerException(e);
        }
    }
}