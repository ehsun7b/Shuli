package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class HTMLResource extends Resource {

    public HTMLResource(URL content) {
        super(content, "text/html");
    }

    public HTMLResource(File contentFile) throws MalformedURLException {
        super(contentFile, "text/html");
    }

    
}
