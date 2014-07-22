package com.ehsunbehravesh.shuli.resource;

import java.net.URL;

/**
 *
 * @author Ehsun Behravesh
 */
public abstract class Resource {
    
    protected final URL contentUrl;
    
    protected final String contentType;

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
