package org.secnod.example.webapp;

import org.apache.shiro.SecurityUtils;
import org.secnod.shiro.jersey.AuthInjectableProvider;

import com.sun.jersey.spi.inject.Injectable;

public class UserInjectableProvider extends AuthInjectableProvider<User> {

    public UserInjectableProvider() {
        super(User.class);
    }

    private static final Injectable<User> userInjectable = new Injectable<User>() {
        @Override
        public User getValue() {
            return new User(SecurityUtils.getSubject());
        }
    };

    @Override
    public Injectable<User> getInjectable() {
        return userInjectable;
    }
}
