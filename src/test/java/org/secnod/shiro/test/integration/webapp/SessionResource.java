package org.secnod.shiro.test.integration.webapp;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.secnod.example.webapp.User;
import org.secnod.shiro.jaxrs.Auth;

@Path("/session")
@Produces(MediaType.TEXT_PLAIN)
public class SessionResource {

    @Context
    HttpServletRequest request;

    @POST
    public String login(@FormParam("username") String username, @FormParam("password") String password,
            @FormParam("rememberMe") @DefaultValue("false") boolean rembemberMe) {
        SecurityUtils.getSubject().login(new UsernamePasswordToken(username, password.toCharArray(), rembemberMe));
        return "Logged in as " + username + "\n";
    }

    @GET
    public String sessionUser(@Auth User user) {
        return "Current user: " + user + "\n";
    }

    /**
     * Invalidate the session without logging out the Shiro subject. For testing the remember me token.
     */
    @DELETE
    public String invalidateHttpSession() {
        HttpSession session = request.getSession(false);
        if (session == null) throw new WebApplicationException(Status.BAD_REQUEST);

        session.invalidate();
        return "session invalidated";
    }
}