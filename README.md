Apache Shiro support for the Jersey JAX-RS implementation.

# Adding the shiro-jersey dependency

1. Clone this repository.
2. Run `mvn clean install`
3. Add the following dependencies to an existing project already using Jersey:

```xml
<dependency>
  <groupId>org.secnod.shiro</groupId>
  <artifactId>shiro-jersey</artifactId>
  <version>0.0.1-SNAPSHOT</version>
</dependency>
```

# Configuring Shiro in a Jersey web application

An example web application is provided complete with [source code](tree/master/src/test/java/org/secnod/example/webapp)
and [web content](tree/master/src/test/resources/org/secnod/example/webapp).

The rest of this section describes how Shiro has been added to the example application.

Add the `ShiroResourceFilterFactory` to the Jersey servlet in `web.xml`:

```xml
<servlet>
  <servlet-name>rest-application</servlet-name>
  <servlet-class>com.sun.jersey.spi.container.servlet.ServletContainer</servlet-class>
  <init-param>
    <param-name>com.sun.jersey.spi.container.ResourceFilters</param-name>
    <param-value>org.secnod.shiro.jersey.ShiroResourceFilterFactory</param-value>
  </init-param>
</servlet>
```

Add the Shiro servlet filter in `web.xml`:

```xml
<filter>
  <filter-name>ShiroFilter</filter-name>
  <filter-class>org.apache.shiro.web.servlet.IniShiroFilter</filter-class>
  <init-param>
    <param-name>configPath</param-name>
    <param-value>classpath:shiro.ini</param-value>
  </init-param>
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

Then add `SubjectInjectableProvider` and `ShiroExceptionMapper` as singletons in the JAX-RS application:

```java
public class ApiApplication extends Application {

    @Override
    public Set<Object> getSingletons() {
        Set<Object> singletons = new HashSet<>();
        singletons.add(new SubjectInjectableProvider());
        singletons.add(new ShiroExceptionMapper());
        return singletons;
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
[SecurityUtils.getSubject()](http://shiro.apache.org/static/1.2.2/apidocs/org/apache/shiro/SecurityUtils.html#getSubject(\)).

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
* The example [User](blob/master/src/test/java/org/secnod/example/webapp/User.java) class.
* The example [UserInjectableProvider](blob/master/src/test/java/org/secnod/example/webapp/UserInjectableProvider.java)
  which must be added as a singleton.
  * The class [AuthInjectableProvider](blob/master/src/main/java/org/secnod/shiro/jersey/AuthInjectableProvider.java)
     can be extended for injection of custom classes with the `@Auth` annotation.

# Development
## Running the integration tests

The integration tests for this project can be run as follows:

1. Start the Jetty server:

    ```bash
    mvn clean package exec:exec
    ```

2. Then test the authorization with
    mvn -Pintegration-tests test

