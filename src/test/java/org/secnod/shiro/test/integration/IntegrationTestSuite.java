package org.secnod.shiro.test.integration;

import org.eclipse.jetty.server.Server;
import org.junit.ClassRule;
import org.junit.rules.ExternalResource;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;
import org.secnod.shiro.test.integration.webapp.JettyServer;

@RunWith(Suite.class)
@SuiteClasses({AnnotationAuthTest.class})
public class IntegrationTestSuite {

    @ClassRule
    public static ExternalResource resource = new ExternalResource() {
        Server server;
        @Override
        protected void before() throws Throwable {
            int port = JettyServer.allocatePort();
            System.setProperty("org.secnod.shiro.test.port", Integer.toString(port));
            server = JettyServer.start(port);
        };

        @Override
        protected void after() {
            try {
                server.stop();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    };

}
