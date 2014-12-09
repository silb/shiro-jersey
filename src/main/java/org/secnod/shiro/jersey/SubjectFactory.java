package org.secnod.shiro.jersey;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.glassfish.hk2.api.PerLookup;

/**
 * Creates {@link Subject subjects} to be used as injected values.
 */
public class SubjectFactory extends TypeFactory<Subject> {

    public SubjectFactory() {
        super(Subject.class);
    }

    @PerLookup
    @Override
    public Subject provide() {
        return SecurityUtils.getSubject();
    }
}
