package com.ehsunbehravesh.shuli.resource;

import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public class JSONResource extends Resource {

    public JSONResource(URL contentUrl) {
        super(contentUrl, "application/json");
    }
    
}
