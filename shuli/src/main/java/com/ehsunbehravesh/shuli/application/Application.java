package com.ehsunbehravesh.shuli.application;

import com.ehsunbehravesh.shuli.exception.ApplicationException;
import com.ehsunbehravesh.shuli.resource.Path;
import com.ehsunbehravesh.shuli.resource.Resource;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Properties;
import java.util.Set;
import org.reflections.Reflections;

/**
 *
 * @author Ehsun Behravesh
 */
public class Application {

    protected final Properties config;
    protected HashMap<String, Class<? extends Resource>> resources;

    public Application(InputStream inputStream) throws IOException, ApplicationException {
        this.config = new Properties();
        this.config.load(inputStream);

        init();
    }

    public Application(Properties config) throws ApplicationException {
        this.config = config;

        init();
    }

    protected final void init() throws ApplicationException {
        loadResources();
    }

    protected void loadResources() throws ApplicationException {
        String packag = config.getProperty(Config.SCAN_PACKAGE);

        if (packag == null) {
            throw new ApplicationException(MessageFormat.format(
                    "can not find the value for {0} in the application config file.",
                    Config.SCAN_PACKAGE), null);
        }

        Reflections reflections = new Reflections(packag);

        Set<Class<?>> resourceClasses = reflections.getTypesAnnotatedWith(Path.class);
        resources = new HashMap<>();
        
        for (Class<?> clasz : resourceClasses) {
            if (Resource.class.isAssignableFrom(clasz)) {
                Class<? extends Resource> resourceClass = (Class<? extends Resource>) clasz;
                Path path = resourceClass.getAnnotation(Path.class);
                String pathValue = path.value();
                
                resources.put(pathValue, resourceClass);
            } else {
                throw new ApplicationException(MessageFormat.format(
                        "Class {0} must extends {1}",
                        clasz.getCanonicalName(),
                        Resource.class.getCanonicalName()), null);
            }
        }
    }

    public static void main(String[] args) {
        Reflections reflections = new Reflections("com.ehsunbehravesh");

    }

}
