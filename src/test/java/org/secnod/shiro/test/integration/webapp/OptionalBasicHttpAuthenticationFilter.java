package org.secnod.shiro.test.integration.webapp;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.BasicHttpAuthenticationFilter;

/**
 * Filter for optional HTTP Basic login for Apache Shiro.
 * Only sends the challenge response if a login attempt fails.
 */
public class OptionalBasicHttpAuthenticationFilter extends BasicHttpAuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        if (!isLoginAttempt(request, response)) return true;
        if (executeLogin(request, response)) return true;
        else return sendChallenge(request, response);
    }

}
