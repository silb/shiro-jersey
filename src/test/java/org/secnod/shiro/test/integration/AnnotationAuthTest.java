package org.secnod.shiro.test.integration;

import static org.junit.Assert.assertEquals;

import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import com.sun.jersey.client.apache.ApacheHttpClient;
import com.sun.jersey.client.apache.config.ApacheHttpClientConfig;
import com.sun.jersey.client.apache.config.DefaultApacheHttpClientConfig;

public class AnnotationAuthTest {

    private static final String USER1_PASSWORD = "user1pw";
    private static final String USER1 = "user1";

    private static String baseUrl = "http://localhost:8080/api/";

    private static ApacheHttpClient client;

    @Before
    public void setup() {
        logout();
    }

    @BeforeClass
    public static void setupClass() throws Exception {
        ApacheHttpClientConfig config = new DefaultApacheHttpClientConfig();
        config.getProperties().put(ApacheHttpClientConfig.PROPERTY_HANDLE_COOKIES, Boolean.TRUE);
        client = ApacheHttpClient.create(config);
    }

    @AfterClass
    public static void tearDownClass() {
        client.destroy();
    }

    private WebResource resource(String relativeUrl) {
        return client.resource(baseUrl + relativeUrl);
    }

    private void auth(String username, String password) {
        client.getClientHandler().getHttpClient().getParams().setAuthenticationPreemptive(true);
        client.getClientHandler().getHttpClient().getState().setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials(username, password));
    }

    private void loginUser() {
        auth(USER1, USER1_PASSWORD);
    }

    private void loginSuperUser() {
        auth("user2", "user2pw");
    }

    private void logout() {
        client.getClientHandler().getHttpClient().getState().clearCredentials();
        client.getClientHandler().getHttpClient().getState().clearCookies();
    }

    private void assertGetStatus(int expectedStatus, WebResource r) {
        assertStatus(expectedStatus, "GET", r);
    }

    private void assertPutStatus(int expectedStatus, WebResource r) {
        assertStatus(expectedStatus, "PUT", r);
    }

    private void assertStatus(int expectedStatus, String method, WebResource r) {
        ClientResponse response = r.entity("foobar").method(method, ClientResponse.class);
        int status = response.getStatus();
        response.close();
        assertEquals("Unexpected HTTP status for " + method + " " + r, expectedStatus, status);
    }

    @Test
    public void publicAccess() {
        WebResource r = resource("public");
        assertGetStatus(200, r);
        loginUser();
        assertGetStatus(200, r);
    }

    private void protectedReadWrite(WebResource r) {
        assertGetStatus(401, r);
        loginUser();
        assertGetStatus(200, r);
        assertPutStatus(403, r);
        loginSuperUser();
        assertGetStatus(200, r);
        assertPutStatus(200, r);
    }

    @Test
    public void protectedByPermission() {
        protectedReadWrite(resource("protected/permission"));
    }

    @Test
    public void protectedByRole() {
        protectedReadWrite(resource("protected/role"));
    }

    @Test
    public void protectedOnlyUsers() {
        WebResource r = resource("protected/user");
        assertGetStatus(401, r);
        loginUser();
        assertGetStatus(200, r);
    }

    @Test
    public void guestOnly() {
        WebResource r = resource("guestonly");
        assertGetStatus(200, r);
        loginUser();
        assertGetStatus(401, r);
    }

    void rememberMeUserSession() {
        WebResource sessionResource = resource("session");
        Form form = new Form();
        form.add("username", USER1);
        form.add("password", USER1_PASSWORD);
        form.add("rememberMe", "true");
        ClientResponse response = sessionResource.post(ClientResponse.class, form);
        int loginStatus = response.getStatus();
        response.close();
        assertEquals(200, loginStatus);
    }

    void invalidateSession() {
        WebResource sessionResource = resource("session");
        ClientResponse response = sessionResource.delete(ClientResponse.class);
        int logoutStatus = response.getStatus();
        response.close();
        assertEquals(200, logoutStatus);
    }

    @Test
    public void noRememberMe() {
        WebResource r = resource("protected/noRememberMe");
        assertGetStatus(401, r);
        loginUser();
        assertGetStatus(200, r);
        logout();
        rememberMeUserSession();
        assertGetStatus(200, r);
        invalidateSession();
        assertGetStatus(401, r);
        assertGetStatus(200, resource("protected/permission"));
    }

    @Test
    public void userAndSubjectInjection() {
        WebResource r = resource("inject/usersubject");
        assertGetStatus(200, r);
        loginUser();
        assertGetStatus(200, r);
    }

    @Test
    public void fieldInjectionFails() {
        WebResource r = resource("inject/field");
        assertGetStatus(500, r);
    }
}
