package com.ehsunbehravesh.shuli.sample1;

import com.ehsunbehravesh.shuli.application.Application;
import com.ehsunbehravesh.shuli.exception.ApplicationException;
import com.ehsunbehravesh.shuli.resource.Resource;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ehsun Behravesh
 */
public class App extends Application {

  @Override
  protected void setup() throws Exception {
    addResource("/", new Resource(Thread.currentThread().getContextClassLoader().getResource("index.html"), "text/html"));
  }
  
  public static void main(String[] args) {
    try {
      App app = new App();

      //Properties config = new Properties();
      //config.setProperty("scan_package", "com.ehsunbehravesh");
      //app.setConfig(config);

      app.start();
    } catch (ApplicationException | IOException ex) {
      Logger.getLogger(App.class.getName()).log(Level.SEVERE, "Error! {0}", ex.getMessage());
    }
  }
}
