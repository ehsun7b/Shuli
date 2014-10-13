package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class JPGImageResource extends Resource {

  public JPGImageResource(URL contentUrl) {
    super(contentUrl, "image/jpeg");
  }

  public JPGImageResource(File contentFile) throws MalformedURLException {
    super(contentFile, "image/jpeg");
  }

}
