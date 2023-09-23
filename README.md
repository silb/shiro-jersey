Apache Shiro support for the Jersey JAX-RS implementation.

# News

[Shiro 1.4](http://shiro.apache.org/news.html#1.4.0-RC2-released) has been
released and includes a new official JAX-RS module `shiro-jaxrs` based on `shiro-jersey`.

The official `shiro-jaxrs` module offers feature parity with the generic JAX-RS
functionality of `shiro-jersey`. The main difference is that `shiro-jaxrs` does
not support the Jersey specific injections of `shiro-jersey`.

See:
* [Apache Shiro JAX-RS Support](https://shiro.apache.org/jaxrs.html)
* [the source code](https://github.com/apache/shiro/tree/shiro-root-1.4.0/support/jaxrs/src/main/java/org/apache/shiro/web/jaxrs)
* [SHIRO-392](https://issues.apache.org/jira/browse/SHIRO-392)
* [Example usage of the Shiro JAX-RS module](https://stormpath.com/blog/protecting-jax-rs-resources-rbac-apache-shIro) ([Wayback Machine](http://web.archive.org/web/20230129054611/https://stormpath.com/blog/protecting-jax-rs-resources-rbac-apache-shIro))

# Adding the shiro-jersey dependency

Add the following dependencies to `pom.xml` in an existing project already using Jersey:

```xml
<dependency>
  <groupId>org.secnod.shiro</groupId>
  <artifactId>shiro-jersey</artifactId>
  <version>0.2.0</version>
</dependency>
```

Version compatibility:

|Jersey  |Shiro Jersey|
|--------|------------|
|2.26-   |0.3.0-SNAPSHOT|
|2.0-2.25|0.2.0       |
|1.x     |0.1.1       |

If you are upgrading from:

* Jersey 2.0-2.25, see the [upgrade instructions](#mig-0.2.x).
* Jersey 1.x, see the [upgrade instructions](#mig-0.1.x).

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
        register(org.apache.shiro.web.jaxrs.ShiroFeature.class);
        register(new SubjectFactory());
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
[standard Shiro annotations](http://shiro.apache.org/static/1.4.2/apidocs/org/apache/shiro/authz/annotation/package-summary.html).

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
[Subject](http://shiro.apache.org/static/1.4.2/apidocs/org/apache/shiro/subject/Subject.html) as a method parameter:

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
[SecurityUtils.getSubject()](http://shiro.apache.org/static/1.4.2/apidocs/org/apache/shiro/SecurityUtils.html#getSubject()).

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

## <a name="mig-0.2.x"></a>Migrating from 0.2.x

`AuthInjectionBinder` has been deleted. Remove its registration in
`ResourceConfig.register()`.

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

    mvn -Pintegration-tests test -Dshiro.jersey=false

The default is to run the tests using the Apache Shiro [JAX-RS support](https://repo1.maven.org/maven2/org/apache/shiro/shiro-jaxrs/1.4.2/).
Alternatively, the old `shiro-jersey` features can be enabled instead by setting `shiro.jersey` to `true`.
