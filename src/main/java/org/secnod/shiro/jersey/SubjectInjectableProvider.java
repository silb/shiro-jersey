package org.secnod.shiro.jersey;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import com.sun.jersey.spi.inject.Injectable;

public class SubjectInjectableProvider extends AuthInjectableProvider<Subject> {

    public SubjectInjectableProvider() {
        super(Subject.class);
    }

    private final Injectable<Subject> subjectInjectable = new Injectable<Subject>() {
        @Override
        public Subject getValue() {
            return SecurityUtils.getSubject();
        }
    };

    @Override
    public Injectable<Subject> getInjectable() {
        return subjectInjectable;
    }
}
