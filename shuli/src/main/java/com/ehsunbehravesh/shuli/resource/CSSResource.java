package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class CSSResource extends Resource {

  public CSSResource(URL contentUrl) {
    super(contentUrl, "text/css");
  }

  public CSSResource(File contentFile) throws MalformedURLException {
    super(contentFile, "text/css");
  }

}
