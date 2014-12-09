package org.secnod.example.webapp;

import org.apache.shiro.SecurityUtils;
import org.secnod.shiro.jersey.TypeFactory;


public class UserFactory extends TypeFactory<User> {

    public UserFactory() {
        super(User.class);
    }

    @Override
    public User provide() {
        return new User(SecurityUtils.getSubject());
    }
}
