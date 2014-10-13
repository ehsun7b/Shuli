package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class XMLResource extends Resource {

  public XMLResource(URL contentUrl) {
    super(contentUrl, "application/xml");
  }

  public XMLResource(File contentFile) throws MalformedURLException {
    super(contentFile, "application/xml");
  }

}
