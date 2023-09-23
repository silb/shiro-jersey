package org.secnod.shiro.test.integration.webapp;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response.Status;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.secnod.example.webapp.User;
import org.secnod.shiro.jaxrs.Auth;

@Path("/session")
@Produces(MediaType.TEXT_PLAIN)
public class SessionResource {

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
    public String invalidateHttpSession(@Context HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) throw new WebApplicationException(Status.BAD_REQUEST);

        session.invalidate();
        return "session invalidated";
    }
}