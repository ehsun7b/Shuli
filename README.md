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
  
  public static void main(String[] args) {
    try {
      App app = new App();

      Properties config = new Properties();
      config.setProperty("scan_package", "package.to.be.scanned.for.services");
      app.setConfig(config);

      app.start();
    } catch (ApplicationException | IOException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, "Error! {0}", ex.getMessage());
    }
  }
}
```

