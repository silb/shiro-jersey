package org.secnod.shiro.test.integration;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.Response;

import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.glassfish.jersey.apache.connector.ApacheClientProperties;
import org.glassfish.jersey.apache.connector.ApacheConnectorProvider;
import org.glassfish.jersey.client.ClientConfig;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class AnnotationAuthTest {

    private static final String USER1_PASSWORD = "user1pw";
    private static final String USER1 = "user1";

    private String baseUrl;

    private static Client client;

    @Before
    public void setup() {
        Integer port = Integer.getInteger("org.secnod.shiro.test.port");
        baseUrl = "http://localhost:" + port + "/api/";
        logout();
    }

    @BeforeClass
    public static void setupClass() throws Exception {
        client = ClientBuilder.newClient();
    }

    @AfterClass
    public static void tearDownClass() {
        client.close();
    }

    private WebTarget webTarget(String relativeUrl) {
        return client.target(baseUrl + relativeUrl);
    }

    private Client newClient(Map<String, Object> props) {
        ClientConfig config = new ClientConfig().connectorProvider(new ApacheConnectorProvider());
        if (props != null)
            for (Entry<String, Object> entry : props.entrySet())
                config.property(entry.getKey(), entry.getValue());
        return ClientBuilder.newClient(config);
    }

    private Client newClient() {
        return newClient(null);
    }

    private void auth(String username, String password) {
        client.close();
        client = null;
        CredentialsProvider credentials = new BasicCredentialsProvider();
        credentials.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(username, password));
        Map<String, Object> props = new HashMap<>();
        props.put(ApacheClientProperties.PREEMPTIVE_BASIC_AUTHENTICATION, true);
        props.put(ApacheClientProperties.CREDENTIALS_PROVIDER, credentials);
        client = newClient(props);
    }

    private void loginUser() {
        auth(USER1, USER1_PASSWORD);
    }

    private void loginSuperUser() {
        auth("user2", "user2pw");
    }

    private void logout() {
        client.close();
        client = null;
        client = newClient();
    }

    private void assertGetStatus(int expectedStatus, WebTarget t) {
        assertStatus(expectedStatus, "GET", t, null);
    }

    private void assertPutStatus(int expectedStatus, WebTarget t) {
        assertStatus(expectedStatus, "PUT", t, "dummy entity");
    }

    private void assertStatus(int expectedStatus, String method, WebTarget t, String entity) {
        Response response = entity != null ? t.request().method(method, Entity.text(entity))
                                           : t.request().method(method);
        int status = response.getStatus();
        response.close();
        assertEquals("Unexpected HTTP status for " + method + " " + t, expectedStatus, status);
    }

    @Test
    public void publicAccess() {
        assertGetStatus(200, webTarget("public"));
        loginUser();
        assertGetStatus(200, webTarget("public"));
    }

    private void protectedReadWrite(String resourcePath) {
        assertGetStatus(401, webTarget(resourcePath));
        loginUser();
        assertGetStatus(200, webTarget(resourcePath));
        assertPutStatus(403, webTarget(resourcePath));
        loginSuperUser();
        assertGetStatus(200, webTarget(resourcePath));
        assertPutStatus(200, webTarget(resourcePath));
    }

    @Test
    public void protectedByPermission() {
        protectedReadWrite("protected/permission");
    }

    @Test
    public void protectedByRole() {
        protectedReadWrite("protected/role");
    }

    @Test
    public void protectedOnlyUsers() {
        assertGetStatus(401, webTarget("protected/user"));
        loginUser();
        assertGetStatus(200, webTarget("protected/user"));
    }

    @Test
    public void guestOnly() {
        assertGetStatus(200, webTarget("guestonly"));
        loginUser();
        assertGetStatus(401, webTarget("guestonly"));
    }

    void rememberMeUserSession() {
        WebTarget sessionResource = webTarget("session");
        Form form = new Form()
            .param("username", USER1)
            .param("password", USER1_PASSWORD)
            .param("rememberMe", "true");
        Response response = sessionResource.request().post(Entity.form(form));
        int loginStatus = response.getStatus();
        response.close();
        assertEquals(200, loginStatus);
    }

    void invalidateSession() {
        WebTarget sessionResource = webTarget("session");
        Response response = sessionResource.request().delete();
        int logoutStatus = response.getStatus();
        response.close();
        assertEquals(200, logoutStatus);
    }

    @Test
    public void noRememberMe() {
        assertGetStatus(401, webTarget("protected/noRememberMe"));
        loginUser();
        assertGetStatus(200, webTarget("protected/noRememberMe"));
        logout();
        rememberMeUserSession();
        assertGetStatus(200, webTarget("protected/noRememberMe"));
        invalidateSession();
        assertGetStatus(401, webTarget("protected/noRememberMe"));
        assertGetStatus(200, webTarget("protected/permission"));
    }

    @Test
    public void userAndSubjectInjection() {
        assertGetStatus(200, webTarget("inject/usersubject"));
        loginUser();
        assertGetStatus(200, webTarget("inject/usersubject"));
    }

    @Test
    public void fieldInjectionFails() {
        assertGetStatus(500, webTarget("inject/field"));
    }
}
