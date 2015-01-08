Apache Shiro support for the Jersey 2.x JAX-RS implementation.

Jersey 1.x is supported by the 0.1.x releases maintained in the branch
`release-0.1`.

# Adding the shiro-jersey dependency

Add the following dependencies to `pom.xml` in an existing project already using Jersey:

```xml
<dependency>
  <groupId>org.secnod.shiro</groupId>
  <artifactId>shiro-jersey</artifactId>
  <version>0.2.0-SNAPSHOT</version>
</dependency>
```

If you are upgrading from version 0.1.0 which use Jersey 1.x, see the
[upgrade instructions](#mig-0.1.x).

# Configuring Shiro in a Jersey web application

An example web application is provided complete with [source code](src/test/java/org/secnod/example/webapp)
and [web content](src/test/resources/org/secnod/example/webapp).

The rest of this section describes how Shiro has been added to the example application.

Add the Shiro servlet filter in `web.xml`:

```xml

<context-param>
  <param-name>shiroConfigLocations</param-name>
  <param-value>classpath:shiro.ini</param-value>
</context-param>

<listener>
    <listener-class>org.apache.shiro.web.env.EnvironmentLoaderListener</listener-class>
</listener>

<filter>
  <filter-name>ShiroFilter</filter-name>
  <filter-class>org.apache.shiro.web.servlet.ShiroFilter</filter-class>
</filter>

<filter-mapping>
  <filter-name>ShiroFilter</filter-name>
  <url-pattern>/*</url-pattern>
  <dispatcher>REQUEST</dispatcher>
  <dispatcher>FORWARD</dispatcher>
  <dispatcher>INCLUDE</dispatcher>
  <dispatcher>ERROR</dispatcher>
</filter-mapping>
```

Then register the following components in the JAX-RS application:

```java
public class ApiApplication extends ResourceConfig {
    public ApiApplication() {
        register(new AuthorizationFilterFeature());
        register(new SubjectFactory());
        register(new AuthInjectionBinder());
    }
}
```

## <a name="configure-shiro"></a>Configuring Shiro
Finally configure `shiro.ini` in the default package on the classpath:

```ini
[main]

[users]
exampleuser = examplepassword, examplerole

[roles]
examplerole = something:readpermission

[urls]
/** = noSessionCreation, authcBasic
```

Real applications should of course not store users and passwords in the INI-file. See the
[Shiro configuration documentation](http://shiro.apache.org/configuration.html).

# <a name="using-shiro"></a>Using Shiro from JAX-RS

This section describes the different alternatives for how Shiro can be used from JAX-RS.

## Declarative authorization with annotations

JAX-RS resource classes and methods can be annotated with the
[standard Shiro annotations](http://shiro.apache.org/static/1.2.2/apidocs/org/apache/shiro/authz/annotation/package-summary.html).

The authorization requirements can for example be declared with `@RequiresPermissions` on JAX-RS resource classes /
methods:

```java
@Path("/auth")
@Produces(MediaType.TEXT_PLAIN)
@RequiresPermissions("protected:read")
public class AuthResource {

    @GET
    public String get() {
        return "OK";
    }

    @PUT
    @RequiresPermissions("protected:write")
    public String set(String value) {
        return value;
    }
}
```

The example above can be summarized as:

* HTTP GET access requires the user to have the permission `protected:read`
* HTTP PUT access requires the user to have both permissions `protected:read` and `protected:write`

## Programmatic authorization

Programmatic authorization is done by injecting the Shiro
[Subject](http://shiro.apache.org/static/1.2.2/apidocs/org/apache/shiro/subject/Subject.html) as a method parameter:

```java
@Path("/auth")
@Produces(MediaType.TEXT_PLAIN)
public class AuthResource {

    @GET
    public String get(@Auth Subject subject) {
        subject.checkPermission("protected:read");
        return "OK";
    }
}
```

Injecting the Subject is just a convenience over calling
[SecurityUtils.getSubject()](http://shiro.apache.org/static/1.2.2/apidocs/org/apache/shiro/SecurityUtils.html#getSubject()).

Declarative and programmatic authorization are often combined when some permissions are static and some are dynamic:

```java
@Path("/auth")
@Produces(MediaType.TEXT_PLAIN)
public class AuthResource {

    @GET
    @RequiresPermissions("static-permission")
    public String get(@Auth Subject subject) {
        subject.checkPermission(dynamicPermission());
        return "OK";
    }
}
```

## <a name="custom-user"></a>Optionally using an application specific user class

Instead of using the Shiro `Subject` class directly one can use an application specific user class for programmatic
authorization:

```java
@Path("/auth")
@Produces(MediaType.TEXT_PLAIN)
public class AuthResource {

    @GET
    public String get(@Auth User user) {
        user.checkBusinessRulePermission();
        return "OK";
    }
}
```

A custom `User` class is a convenient way of implementing application
specific authorization based on business rules on the user's data.

More authorization as rules means less authorization as permissions and hence fewer permissions to maintain.

See:
* The example [User](src/test/java/org/secnod/example/webapp/User.java) class.
* The example [UserFactory](src/test/java/org/secnod/example/webapp/UserFactory.java)
  which must be registered as a JAX-RS component.
  * The class [TypeFactory](src/main/java/org/secnod/shiro/jersey/TypeFactory.java)
     can be extended for injection of custom classes with the `@Auth` annotation.

## <a name="mig-0.1.x"></a>Migrating from 0.1.x

These instructions assume that the JAX-RS application is a subclass of
`org.glassfish.jersey.server.ResourceConfig`.

Note that JAX-RS component registration is done by `ResourceConfig.register()`
instead of `javax.ws.rs.core.Application.getSingletons()`.

* `AuthorizationFilterFeature` replaces `ShiroResourceFilterFactory`

    Remove the configuration of `ShiroResourceFilterFactory` from `web.xml` and
    register `AuthorizationFilterFeature` as a JAX-RS component.

* `SubjectFactory` replaces `SubjectInjectableProvider`
* `TypeFactory` replaces `AuthInjectableProvider`

# Development
## Running the integration tests

The integration tests for this project can be run as follows:

    mvn -Pintegration-tests test

