package com.ehsunbehravesh.shuli.application;

import com.ehsunbehravesh.shuli.exception.ApplicationException;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author Ehsun Behravesh
 */
public class InternalResourceApp extends Application {

    public InternalResourceApp(InputStream inputStream) throws IOException, ApplicationException {
        super(inputStream);
    }
    
}
