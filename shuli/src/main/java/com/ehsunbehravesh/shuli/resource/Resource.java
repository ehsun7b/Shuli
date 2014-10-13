package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class Resource {

  protected final URL contentUrl;
  protected final String contentType;

  public Resource(File contentFile, String contentType) throws MalformedURLException {
    this.contentUrl = contentFile.toURI().toURL();
    this.contentType = contentType;
  }

  public Resource(URL contentUrl, String contentType) {
    this.contentUrl = contentUrl;
    this.contentType = contentType;
  }

  public URL getContentUrl() {
    return contentUrl;
  }

  public String getContentType() {
    return contentType;
  }

}
