package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class PNGImageResource extends Resource {

    public PNGImageResource(URL contentUrl) {
        super(contentUrl, "image/png");
    }

    public PNGImageResource(File contentFile) throws MalformedURLException {
        super(contentFile, "image/png");
    }

    
}
