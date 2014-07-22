package com.ehsunbehravesh.shuli.resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class JSONResource extends Resource {

    public JSONResource(URL contentUrl) {
        super(contentUrl, "application/json");
    }

    public JSONResource(File contentFile) throws MalformedURLException {
        super(contentFile, "application/json");
    }
    
    
}
