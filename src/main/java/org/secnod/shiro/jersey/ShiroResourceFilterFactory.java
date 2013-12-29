package org.secnod.shiro.jersey;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;

import com.sun.jersey.api.model.AbstractMethod;
import com.sun.jersey.spi.container.ResourceFilter;
import com.sun.jersey.spi.container.ResourceFilterFactory;

public class ShiroResourceFilterFactory implements ResourceFilterFactory {

    private static List<Class<? extends Annotation>> shiroAnnotations = Collections.unmodifiableList(Arrays.asList(
            RequiresPermissions.class,
            RequiresRoles.class,
            RequiresAuthentication.class,
            RequiresUser.class,
            RequiresGuest.class));

    @Override
    public List<ResourceFilter> create(AbstractMethod am) {
        List<ResourceFilter> filters = new ArrayList<>();

        for (Class<? extends Annotation> annotationClass : shiroAnnotations) {
            // XXX What is the performance of getAnnotation vs getAnnotations?
            Annotation classAuthzSpec = am.getResource().getAnnotation(annotationClass);
            Annotation methodAuthzSpec = am.getAnnotation(annotationClass);

            if (classAuthzSpec != null) filters.add(ShiroAnnotationResourceFilter.valueOf(classAuthzSpec));
            if (methodAuthzSpec != null) filters.add(ShiroAnnotationResourceFilter.valueOf(methodAuthzSpec));
        }

        return filters;
    }
}
