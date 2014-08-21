package org.secnod.example.webapp;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.subject.Subject;

public class User {

    private final Subject subject;

    public User(Subject subject) {
        super();
        if (subject == null)
            throw new NullPointerException();
        this.subject = subject;
    }

    public <T> T unwrap(Class<T> type) {
        if (Subject.class.equals(type)) return type.cast(subject);

        throw new IllegalArgumentException("User " + this + " cannot be unwrapped to " + type);
    }

    @Override
    public String toString() {
        String username = subject.getPrincipal() != null ? subject.getPrincipal().toString() : null;
        return username != null ? username : "anonymous";
    }

    public void checkPermissionBySomeRule() throws AuthorizationException {
        // Apply domain specific authorization rules based on data found in the user's data, subject principals etc.
        if (Math.random() < 0.5) throw new UnauthorizedException();
    }

    // Convenience delegate methods to the Subject

    public void checkPermission(String permission) throws AuthorizationException {
        subject.checkPermission(permission);
    }

    public void checkRole(String roleIdentifier) throws AuthorizationException {
        this.subject.checkRole(roleIdentifier);
    }
}
