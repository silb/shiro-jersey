package org.secnod.shiro.test.integration.webapp;

import java.io.IOException;
import java.net.ServerSocket;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * An example standalone Jetty server.
 */
public class JettyServer {

    public static void main(String[] args) throws Exception {
        start(8080).join();
    }

    public static Server start(int port) throws Exception {
        Server server = new Server(port);

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        String resourcePath = JettyServer.class.getPackage().getName().replace('.', '/');
        webapp.setBaseResource(Resource.newClassPathResource(resourcePath));
        webapp.setParentLoaderPriority(true);

        server.setHandler(webapp);
        server.setStopTimeout(5000);
        server.start();
        return server;
    }

    public static int allocatePort() throws IOException {
        try (ServerSocket s = new ServerSocket(0)) {
            return s.getLocalPort();
        }
    }
}
