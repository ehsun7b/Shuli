package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class JavaScriptResource extends Resource {

  public JavaScriptResource(URL contentUrl) {
    super(contentUrl, "application/javascript");
  }

  public JavaScriptResource(File contentFile) throws MalformedURLException {
    super(contentFile, "application/javascript");
  }

}
