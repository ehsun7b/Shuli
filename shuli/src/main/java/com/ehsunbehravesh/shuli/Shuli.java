package com.ehsunbehravesh.shuli;

import com.ehsunbehravesh.shuli.application.Application;
import com.ehsunbehravesh.shuli.exception.ApplicationException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ehsun Behravesh
 */
public class Shuli extends Application {

  
  
  public static void main(String[] args) {
    try {
      Shuli shuli = new Shuli();
      Properties config = new Properties();
      config.setProperty("scan_package", "com.ehsunbehravesh");
      shuli.setConfig(config);
      shuli.start();
    } catch (ApplicationException | IOException ex) {
      Logger.getLogger(Shuli.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
