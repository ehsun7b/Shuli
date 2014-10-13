package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class GIFImageResource extends Resource {

  public GIFImageResource(URL contentUrl) {
    super(contentUrl, "image/gif");
  }

  public GIFImageResource(File contentFile) throws MalformedURLException {
    super(contentFile, "image/gif");
  }

}
