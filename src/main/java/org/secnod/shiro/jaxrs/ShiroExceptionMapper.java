package org.secnod.shiro.jaxrs;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.ext.ExceptionMapper;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;

/**
 * @deprecated replaced by the native Shiro exception mapper {@link org.apache.shiro.web.jaxrs.ExceptionMapper}
 *
 * @see org.apache.shiro.web.jaxrs.ShiroFeature
 */
@Deprecated
public class ShiroExceptionMapper implements ExceptionMapper<AuthorizationException> {

    @Override
    public Response toResponse(AuthorizationException exception) {

        Status status;

        if (exception instanceof UnauthorizedException) {
            status = Status.FORBIDDEN;
        } else {
            status = Status.UNAUTHORIZED;
        }

        return Response.status(status).build();
    }
}
