Shuli
=====

Very light HTTP Server for desktop applications.
It lets the developer develops desktop applications using HTML5 and REST services. JavaScript is supported at both server-side and client-side.

<h2 style="color: #8866ff">Simplest Application</h2>

```java
public class App extends Application {

  @Override
  protected void setup() throws Exception {
    addResource("/", new Resource(Thread.currentThread().getContextClassLoader().getResource("index.html"), "text/html"));
  }

  public static void main(String[] args) throws ApplicationException {
    App app = new App();

    Properties config = new Properties();
    config.setProperty("scan_package", "some.package");
    app.setConfig(config);

    app.start();
  }
}
```
<hr/>
<h2>Configuration by properties file</h2>

Include shuli.properties in your classpath

scan_package=some.package

```java
public class App extends Application {

  @Override
  protected void setup() throws Exception {
    addResource("/", new Resource(Thread.currentThread().getContextClassLoader().getResource("index.html"), "text/html"));
  }

  public static void main(String[] args) throws ApplicationException {
    App app = new App();
    app.start();
  }
}
```
